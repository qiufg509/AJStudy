package com.qiufengguang.ajstudy.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qiufengguang.ajstudy.global.Constant;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 高性能 SharedPreferences 工具类
 * 特点：内存缓存、异步写入、线程安全、类型安全、优雅关闭
 * 用法：SpUtils.init(context); SpUtils.getInstance().putString("key", "value");
 *
 * @author qiufengguang
 * @since 2025/11/30 2:15
 */
public final class SpUtils {
    private static final String TAG = "SpUtils";

    private static volatile SpUtils instance;

    private final Context appContext;

    private volatile boolean isShuttingDown = false;

    /**
     * 初始化（必须在 Application 中调用）
     */
    public static void init(@NonNull Context context) {
        if (instance == null) {
            synchronized (SpUtils.class) {
                if (instance == null) {
                    instance = new SpUtils(context);
                }
            }
        }
    }

    /**
     * 获取实例
     */
    public static SpUtils getInstance() {
        if (instance == null) {
            Log.i(TAG, "SpUtils 未初始化，请先在 Application 中调用 init()");
        }
        return instance;
    }

    private SpUtils(@NonNull Context context) {
        this.appContext = context.getApplicationContext();
        startWriteWorker();
    }

    // ==================== 内存缓存 ====================
    private final Map<String, Object> memoryCache = new ConcurrentHashMap<>(64);
    private final Map<String, SharedPreferences> spCache = new ConcurrentHashMap<>(4);

    /**
     * 构建缓存键
     */
    @NonNull
    private String buildCacheKey(@NonNull String spName, @NonNull String key) {
        return spName + "::" + key;
    }

    // ==================== 写入队列和线程 ====================
    private static final int MAX_PENDING_WRITES = 1000;
    private final LinkedBlockingQueue<WriteTask> writeQueue = new LinkedBlockingQueue<>(MAX_PENDING_WRITES);

    /**
     * 写入任务封装
     */
    private static class WriteTask {
        final String spName;
        final String key;
        final Object value;
        final Operation operation;

        WriteTask(String spName, String key, Object value, Operation operation) {
            this.spName = spName;
            this.key = key;
            this.value = value;
            this.operation = operation;
        }

        enum Operation {
            PUT,
            REMOVE,
            CLEAR
        }
    }

    /**
     * 自定义线程工厂
     */
    private static class SPThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        SPThreadFactory() {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            namePrefix = "SP-Worker-" + poolNumber.getAndIncrement() + "-";
        }

        @Override
        public Thread newThread(@NonNull Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
            t.setDaemon(true);
            t.setPriority(Thread.MIN_PRIORITY + 1); // 稍高于最低优先级
            return t;
        }
    }

    private final ExecutorService writeExecutor = Executors.newSingleThreadExecutor(new SPThreadFactory());

    /**
     * 启动写入工作线程
     */
    private void startWriteWorker() {
        writeExecutor.execute(new WriteWorker());
    }

    /**
     * 写入工作线程
     */
    private class WriteWorker implements Runnable {
        @Override
        public void run() {
            try {
                while (!isShuttingDown) {
                    // 阻塞获取任务，无任务时线程挂起，不消耗CPU
                    WriteTask task = writeQueue.take();

                    // 处理单个任务
                    processWriteTask(task);

                    // 批量处理：如果队列还有任务，继续处理（限制批次大小，避免长时间占用）
                    int batchProcessed = 0;
                    WriteTask nextTask;
                    while (batchProcessed < 50 && (nextTask = writeQueue.poll()) != null) {
                        processWriteTask(nextTask);
                        batchProcessed++;
                    }
                }
            } catch (InterruptedException e) {
                // 线程被中断，准备退出
                Thread.currentThread().interrupt();
            } finally {
                // 优雅关闭：处理剩余任务
                processRemainingTasksOnShutdown();
            }
        }

        /**
         * 处理写入任务
         */
        private void processWriteTask(@NonNull WriteTask task) {
            try {
                SharedPreferences sp = getSharedPreferences(task.spName);
                SharedPreferences.Editor editor = sp.edit();

                switch (task.operation) {
                    case PUT:
                        applyValueToEditor(editor, task.key, task.value);
                        break;
                    case REMOVE:
                        editor.remove(task.key);
                        break;
                    case CLEAR:
                        editor.clear();
                        // 清除对应内存缓存
                        clearMemoryCacheForSP(task.spName);
                        break;
                }

                // 使用 apply() 异步写入，不阻塞当前线程
                editor.apply();

            } catch (Exception e) {
                // 写入失败处理：重试一次（可选）
                System.err.println("SpUtils: 写入失败 - " + e.getMessage());
                // 可以根据需要添加重试逻辑
            }
        }

        /**
         * 关闭时处理剩余任务
         */
        private void processRemainingTasksOnShutdown() {
            int remaining = 0;
            WriteTask task;

            // 处理剩余任务，但限制最大处理数量
            while ((task = writeQueue.poll()) != null && remaining < 100) {
                try {
                    processWriteTask(task);
                    remaining++;
                } catch (Exception e) {
                    System.err.println("SpUtils: 关闭时处理任务失败 - " + e.getMessage());
                }
            }

            if (!writeQueue.isEmpty()) {
                System.err.println("SpUtils: 关闭时丢弃了 " + writeQueue.size() + " 个待写入任务");
                writeQueue.clear();
            }
        }
    }

    /**
     * 获取 SharedPreferences 实例（带缓存）
     */
    @NonNull
    private synchronized SharedPreferences getSharedPreferences(@NonNull String spName) {
        SharedPreferences sp = spCache.get(spName);
        if (sp == null) {
            sp = appContext.getSharedPreferences(spName, Context.MODE_PRIVATE);
            spCache.put(spName, sp);
            // 预加载到内存缓存
            loadAllToCache(spName, sp);
        }
        return sp;
    }

    /**
     * 加载所有数据到内存缓存
     */
    private void loadAllToCache(@NonNull String spName, @NonNull SharedPreferences sp) {
        Map<String, ?> all = sp.getAll();
        for (Map.Entry<String, ?> entry : all.entrySet()) {
            String cacheKey = buildCacheKey(spName, entry.getKey());
            memoryCache.put(cacheKey, entry.getValue());
        }
    }

    /**
     * 清除指定 SP 的内存缓存
     */
    private void clearMemoryCacheForSP(@NonNull String spName) {
        String prefix = spName + "::";
        memoryCache.keySet().removeIf(key -> key.startsWith(prefix));
    }

    // ==================== 公共 API - 写入操作 ====================

    /**
     * 保存 String 值（异步）
     */
    public void putString(@NonNull String key, @Nullable String value) {
        putString(Constant.SP.PREF_CONFIG, key, value);
    }

    public void putString(@NonNull String spName, @NonNull String key, @Nullable String value) {
        checkShutdown();
        String cacheKey = buildCacheKey(spName, key);
        memoryCache.put(cacheKey, value);
        enqueueWriteTask(spName, key, value, WriteTask.Operation.PUT);
    }

    /**
     * 保存 int 值（异步）
     */
    public void putInt(@NonNull String key, int value) {
        putInt(Constant.SP.PREF_CONFIG, key, value);
    }

    public void putInt(@NonNull String spName, @NonNull String key, int value) {
        checkShutdown();
        String cacheKey = buildCacheKey(spName, key);
        memoryCache.put(cacheKey, value);
        enqueueWriteTask(spName, key, value, WriteTask.Operation.PUT);
    }

    /**
     * 保存 long 值（异步）
     */
    public void putLong(@NonNull String key, long value) {
        putLong(Constant.SP.PREF_CONFIG, key, value);
    }

    public void putLong(@NonNull String spName, @NonNull String key, long value) {
        checkShutdown();
        String cacheKey = buildCacheKey(spName, key);
        memoryCache.put(cacheKey, value);
        enqueueWriteTask(spName, key, value, WriteTask.Operation.PUT);
    }

    /**
     * 保存 float 值（异步）
     */
    public void putFloat(@NonNull String key, float value) {
        putFloat(Constant.SP.PREF_CONFIG, key, value);
    }

    public void putFloat(@NonNull String spName, @NonNull String key, float value) {
        checkShutdown();
        String cacheKey = buildCacheKey(spName, key);
        memoryCache.put(cacheKey, value);
        enqueueWriteTask(spName, key, value, WriteTask.Operation.PUT);
    }

    /**
     * 保存 boolean 值（异步）
     */
    public void putBoolean(@NonNull String key, boolean value) {
        putBoolean(Constant.SP.PREF_CONFIG, key, value);
    }

    public void putBoolean(@NonNull String spName, @NonNull String key, boolean value) {
        checkShutdown();
        String cacheKey = buildCacheKey(spName, key);
        memoryCache.put(cacheKey, value);
        enqueueWriteTask(spName, key, value, WriteTask.Operation.PUT);
    }

    /**
     * 保存 String Set（异步）
     */
    public void putStringSet(@NonNull String key, @Nullable Set<String> value) {
        putStringSet(Constant.SP.PREF_CONFIG, key, value);
    }

    public void putStringSet(@NonNull String spName, @NonNull String key, @Nullable Set<String> value) {
        checkShutdown();
        String cacheKey = buildCacheKey(spName, key);
        memoryCache.put(cacheKey, value);
        enqueueWriteTask(spName, key, value, WriteTask.Operation.PUT);
    }

    // ==================== 公共 API - 读取操作 ====================

    /**
     * 获取 String 值
     */
    @Nullable
    public String getString(@NonNull String key, @Nullable String defaultValue) {
        return getString(Constant.SP.PREF_CONFIG, key, defaultValue);
    }

    @Nullable
    public String getString(@NonNull String spName, @NonNull String key, @Nullable String defaultValue) {
        checkShutdown();
        String cacheKey = buildCacheKey(spName, key);

        // 1. 从内存缓存读取
        Object cached = memoryCache.get(cacheKey);
        if (cached instanceof String) {
            return (String) cached;
        }

        // 2. 从磁盘读取
        String value = getSharedPreferences(spName).getString(key, defaultValue);
        if (value != null) {
            memoryCache.put(cacheKey, value);
        }
        return value;
    }

    /**
     * 获取 int 值
     */
    public int getInt(@NonNull String key, int defaultValue) {
        return getInt(Constant.SP.PREF_CONFIG, key, defaultValue);
    }

    public int getInt(@NonNull String spName, @NonNull String key, int defaultValue) {
        checkShutdown();
        String cacheKey = buildCacheKey(spName, key);

        // 1. 从内存缓存读取
        Object cached = memoryCache.get(cacheKey);
        if (cached instanceof Integer) {
            return (Integer) cached;
        }

        // 2. 从磁盘读取
        int value = getSharedPreferences(spName).getInt(key, defaultValue);
        memoryCache.put(cacheKey, value);
        return value;
    }

    /**
     * 获取 long 值
     */
    public long getLong(@NonNull String key, long defaultValue) {
        return getLong(Constant.SP.PREF_CONFIG, key, defaultValue);
    }

    public long getLong(@NonNull String spName, @NonNull String key, long defaultValue) {
        checkShutdown();
        String cacheKey = buildCacheKey(spName, key);

        // 1. 从内存缓存读取
        Object cached = memoryCache.get(cacheKey);
        if (cached instanceof Long) {
            return (Long) cached;
        }

        // 2. 从磁盘读取
        long value = getSharedPreferences(spName).getLong(key, defaultValue);
        memoryCache.put(cacheKey, value);
        return value;
    }

    /**
     * 获取 float 值
     */
    public float getFloat(@NonNull String key, float defaultValue) {
        return getFloat(Constant.SP.PREF_CONFIG, key, defaultValue);
    }

    public float getFloat(@NonNull String spName, @NonNull String key, float defaultValue) {
        checkShutdown();
        String cacheKey = buildCacheKey(spName, key);

        // 1. 从内存缓存读取
        Object cached = memoryCache.get(cacheKey);
        if (cached instanceof Float) {
            return (Float) cached;
        }

        // 2. 从磁盘读取
        float value = getSharedPreferences(spName).getFloat(key, defaultValue);
        memoryCache.put(cacheKey, value);
        return value;
    }

    /**
     * 获取 boolean 值
     */
    public boolean getBoolean(@NonNull String key, boolean defaultValue) {
        return getBoolean(Constant.SP.PREF_CONFIG, key, defaultValue);
    }

    public boolean getBoolean(@NonNull String spName, @NonNull String key, boolean defaultValue) {
        checkShutdown();
        String cacheKey = buildCacheKey(spName, key);

        // 1. 从内存缓存读取
        Object cached = memoryCache.get(cacheKey);
        if (cached instanceof Boolean) {
            return (Boolean) cached;
        }

        // 2. 从磁盘读取
        boolean value = getSharedPreferences(spName).getBoolean(key, defaultValue);
        memoryCache.put(cacheKey, value);
        return value;
    }

    /**
     * 获取 String Set
     */
    @Nullable
    public Set<String> getStringSet(@NonNull String key, @Nullable Set<String> defaultValue) {
        return getStringSet(Constant.SP.PREF_CONFIG, key, defaultValue);
    }

    @Nullable
    public Set<String> getStringSet(@NonNull String spName, @NonNull String key, @Nullable Set<String> defaultValue) {
        checkShutdown();
        String cacheKey = buildCacheKey(spName, key);

        // 1. 从内存缓存读取
        Object cached = memoryCache.get(cacheKey);
        if (cached instanceof Set) {
            // 返回副本，防止外部修改影响缓存
            @SuppressWarnings("unchecked")
            Set<String> cachedSet = (Set<String>) cached;
            return new HashSet<>(cachedSet);
        }

        // 2. 从磁盘读取
        Set<String> value = getSharedPreferences(spName).getStringSet(key, defaultValue);
        if (value != null) {
            // 存储副本到缓存
            memoryCache.put(cacheKey, new HashSet<>(value));
        }
        return value;
    }

    // ==================== 公共 API - 删除操作 ====================

    /**
     * 删除键
     */
    public void remove(@NonNull String key) {
        remove(Constant.SP.PREF_CONFIG, key);
    }

    public void remove(@NonNull String spName, @NonNull String key) {
        checkShutdown();
        String cacheKey = buildCacheKey(spName, key);
        memoryCache.remove(cacheKey);
        enqueueWriteTask(spName, key, null, WriteTask.Operation.REMOVE);
    }

    /**
     * 清空指定 SharedPreferences
     */
    public void clear(@NonNull String spName) {
        checkShutdown();
        enqueueWriteTask(spName, null, null, WriteTask.Operation.CLEAR);
    }

    public void clear() {
        clear(Constant.SP.PREF_CONFIG);
    }

    /**
     * 检查是否包含某个键
     */
    public boolean contains(@NonNull String key) {
        return contains(Constant.SP.PREF_CONFIG, key);
    }

    public boolean contains(@NonNull String spName, @NonNull String key) {
        checkShutdown();
        String cacheKey = buildCacheKey(spName, key);

        if (memoryCache.containsKey(cacheKey)) {
            // 即使值为 null，也认为包含该键（表示已删除）
            return true;
        }

        return getSharedPreferences(spName).contains(key);
    }

    /**
     * 获取所有数据（谨慎使用，可能包含大量数据）
     */
    @NonNull
    public Map<String, ?> getAll() {
        return getAll(Constant.SP.PREF_CONFIG);
    }

    @NonNull
    public Map<String, ?> getAll(@NonNull String spName) {
        checkShutdown();
        return getSharedPreferences(spName).getAll();
    }

    // ==================== 同步操作（谨慎使用）====================

    /**
     * 立即同步保存（阻塞调用线程）
     */
    public boolean commitString(@NonNull String key, @Nullable String value) {
        return commitString(Constant.SP.PREF_CONFIG, key, value);
    }

    public boolean commitString(@NonNull String spName, @NonNull String key, @Nullable String value) {
        checkShutdown();
        String cacheKey = buildCacheKey(spName, key);
        memoryCache.put(cacheKey, value);

        SharedPreferences.Editor editor = getSharedPreferences(spName).edit();
        if (value != null) {
            editor.putString(key, value);
        } else {
            editor.remove(key);
        }
        return editor.commit();
    }

    /**
     * 批量同步操作
     */
    public boolean commitBatch(@NonNull BatchOperation operation) {
        return commitBatch(Constant.SP.PREF_CONFIG, operation);
    }

    public boolean commitBatch(@NonNull String spName, @NonNull BatchOperation operation) {
        checkShutdown();
        SharedPreferences.Editor editor = getSharedPreferences(spName).edit();
        operation.execute(editor, memoryCache, spName);
        return editor.commit();
    }

    /**
     * 批量操作接口
     */
    public interface BatchOperation {
        void execute(
            @NonNull SharedPreferences.Editor editor,
            @NonNull Map<String, Object> memoryCache,
            @NonNull String spName
        );
    }

    // ==================== 状态和统计 ====================

    /**
     * 获取待写入任务数量
     */
    public int getPendingWriteCount() {
        return writeQueue.size();
    }

    /**
     * 获取内存缓存大小
     */
    public int getMemoryCacheSize() {
        return memoryCache.size();
    }

    // ==================== 资源管理 ====================

    /**
     * 优雅关闭（在 Application 销毁时调用）
     */
    public void shutdown() {
        if (isShuttingDown) {
            return;
        }

        isShuttingDown = true;

        // 1. 停止接受新任务
        writeQueue.clear();

        // 2. 关闭线程池
        writeExecutor.shutdown();

        try {
            // 等待现有任务完成
            if (!writeExecutor.awaitTermination(3, TimeUnit.SECONDS)) {
                // 强制关闭
                writeExecutor.shutdownNow();

                // 再次等待
                if (!writeExecutor.awaitTermination(2, TimeUnit.SECONDS)) {
                    System.err.println("SpUtils: 线程池未能正常关闭");
                }
            }
        } catch (InterruptedException e) {
            // 重新设置中断状态
            Thread.currentThread().interrupt();
            writeExecutor.shutdownNow();
        }

        // 3. 清空缓存
        memoryCache.clear();
        spCache.clear();

        instance = null;
    }

    /**
     * 检查是否正在关闭
     */
    private void checkShutdown() {
        if (isShuttingDown) {
            throw new IllegalStateException("SpUtils 已关闭，无法执行操作");
        }
    }

    // ==================== 内部工具方法 ====================

    /**
     * 将值应用到 Editor
     */
    private void applyValueToEditor(
        @NonNull SharedPreferences.Editor editor,
        @NonNull String key,
        @Nullable Object value
    ) {
        if (value == null) {
            editor.remove(key);
        } else if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Set) {
            @SuppressWarnings("unchecked")
            Set<String> stringSet = (Set<String>) value;
            editor.putStringSet(key, stringSet);
        } else {
            throw new IllegalArgumentException("不支持的 value 类型: " + value.getClass().getName());
        }
    }

    /**
     * 将写入任务加入队列
     */
    private void enqueueWriteTask(
        @NonNull String spName,
        @Nullable String key,
        @Nullable Object value,
        @NonNull WriteTask.Operation operation
    ) {
        if (writeQueue.size() >= MAX_PENDING_WRITES) {
            // 队列已满，丢弃最旧的任务（根据需求调整策略）
            writeQueue.poll();
        }

        WriteTask task = new WriteTask(spName, key, value, operation);

        if (!writeQueue.offer(task)) {
            // 队列满，直接同步写入（降级策略）
            System.err.println("SpUtils: 写入队列满，降级为同步写入");
            writeImmediately(spName, key, value, operation);
        }
    }

    /**
     * 立即写入（降级策略）
     */
    private void writeImmediately(
        @NonNull String spName,
        @Nullable String key,
        @Nullable Object value,
        @NonNull WriteTask.Operation operation
    ) {
        try {
            SharedPreferences.Editor editor = getSharedPreferences(spName).edit();

            switch (operation) {
                case PUT:
                    if (!TextUtils.isEmpty(key)) {
                        applyValueToEditor(editor, key, value);
                    }
                    break;
                case REMOVE:
                    if (!TextUtils.isEmpty(key)) {
                        editor.remove(key);
                    }
                    break;
                case CLEAR:
                    editor.clear();
                    break;
            }

            editor.apply();
        } catch (Exception e) {
            System.err.println("SpUtils: 同步写入失败 - " + e.getMessage());
        }
    }

    /**
     * 更新内存缓存
     */
    private void updateCache(@NonNull String spName, @NonNull String key, @Nullable Object value) {
        String cacheKey = buildCacheKey(spName, key);
        if (value != null) {
            memoryCache.put(cacheKey, value);
        } else {
            memoryCache.remove(cacheKey);
        }
    }

    /**
     * 从内存缓存移除
     */
    private void removeFromCache(@NonNull String spName, @NonNull String key) {
        String cacheKey = buildCacheKey(spName, key);
        memoryCache.remove(cacheKey);
    }
}