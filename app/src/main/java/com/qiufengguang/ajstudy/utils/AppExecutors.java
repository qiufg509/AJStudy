package com.qiufengguang.ajstudy.utils;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 全局线程池管理器
 * [性能专家重构]：引入动态伸缩线程池与流式调度 API
 *
 * @author qiufengguang
 */
public class AppExecutors {

    private final ExecutorService diskIO;
    private final ExecutorService networkIO;
    private final Executor mainThread;

    private AppExecutors() {
        // 1. 磁盘 IO：单线程保证写入顺序，防止并发导致的文件损坏
        // 使用 ExecutorService 以便支持更丰富的方法和 OkHttp 等库的接入
        this.diskIO = Executors.newSingleThreadExecutor();

        // 2. 网络与异步任务：参考 OkHttp 的 Dispatcher 实现
        // 核心线程为 0，最大线程 64，空闲 60s 自动回收。
        this.networkIO = new ThreadPoolExecutor(
            0, 64, 60L, TimeUnit.SECONDS,
            new SynchronousQueue<>(), runnable -> {
            Thread thread = new Thread(runnable, "AppExecutor-Async");
            thread.setPriority(Thread.NORM_PRIORITY - 1);
            return thread;
        });

        this.mainThread = new MainThreadExecutor();
    }

    private static final class Holder {
        private static final AppExecutors INSTANCE = new AppExecutors();
    }

    public static AppExecutors getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 获取磁盘 IO 执行器
     */
    public ExecutorService diskIO() {
        return diskIO;
    }

    /**
     * 获取网络/异步任务执行器
     */
    public ExecutorService networkIO() {
        return networkIO;
    }

    /**
     * 获取主线程执行器
     */
    public Executor mainThread() {
        return mainThread;
    }

    /**
     * [现代异步模式]：后台执行任务，完成后自动切回主线程执行 UI 更新
     */
    public void execute(@NonNull Runnable background, Runnable ui) {
        networkIO.execute(() -> {
            background.run();
            if (ui != null) {
                mainThread.execute(ui);
            }
        });
    }

    private static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
