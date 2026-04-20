package com.qiufengguang.ajstudy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.global.GlobalApp;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 高性能 SharedPreferences 工具类
 * [性能专家重构]：支持多文件自动预热，移除冗余内存缓存，增强线程安全性。
 *
 * @author qiufengguang
 */
public final class SpUtils {
    private final Map<String, SharedPreferences> mSpCache = new ConcurrentHashMap<>(4);

    private SpUtils() {
        // 自动预热最常用的配置文件
        prewarm(Constant.Sp.PREF_CONFIG);
        prewarm(Constant.Sp.PREF_USER);
    }

    private static final class Holder {
        private static final SpUtils INSTANCE = new SpUtils();
    }

    public static SpUtils getInstance() {
        return Holder.INSTANCE;
    }

    /**
     * 异步预热 SP 文件
     */
    private void prewarm(String spName) {
        AppExecutors.getInstance().diskIO().execute(() -> getSp(spName));
    }

    @NonNull
    public SharedPreferences getSp(@NonNull String spName) {
        SharedPreferences sp = mSpCache.get(spName);
        if (sp == null) {
            Context context = GlobalApp.getContext();
            if (context == null) {
                throw new IllegalStateException("SpUtils 无法工作：GlobalApp 未正确初始化");
            }
            sp = context.getSharedPreferences(spName, Context.MODE_PRIVATE);
            mSpCache.put(spName, sp);
        }
        return sp;
    }

    // ==================== 默认配置文件的便捷操作 (PREF_CONFIG) ====================

    public void putString(String key, String value) {
        getSp(Constant.Sp.PREF_CONFIG).edit().putString(key, value).apply();
    }

    public void putInt(String key, int value) {
        getSp(Constant.Sp.PREF_CONFIG).edit().putInt(key, value).apply();
    }

    public void putLong(String key, long value) {
        getSp(Constant.Sp.PREF_CONFIG).edit().putLong(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        getSp(Constant.Sp.PREF_CONFIG).edit().putBoolean(key, value).apply();
    }

    @Nullable
    public String getString(String key, String defaultValue) {
        return getSp(Constant.Sp.PREF_CONFIG).getString(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return getSp(Constant.Sp.PREF_CONFIG).getInt(key, defaultValue);
    }

    public long getLong(String key, long defaultValue) {
        return getSp(Constant.Sp.PREF_CONFIG).getLong(key, defaultValue);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return getSp(Constant.Sp.PREF_CONFIG).getBoolean(key, defaultValue);
    }

    // ==================== 通用管理 API ====================

    public Map<String, ?> getAll(String spName) {
        return getSp(spName).getAll();
    }

    public void remove(String spName, String key) {
        getSp(spName).edit().remove(key).apply();
    }

    public void clear(String spName) {
        getSp(spName).edit().clear().apply();
    }

    public void shutdown() {
        mSpCache.clear();
    }
}
