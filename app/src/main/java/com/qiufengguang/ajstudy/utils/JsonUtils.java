package com.qiufengguang.ajstudy.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * 全局 JSON 工具类
 * [性能专家重构]：复用单例 Gson 实例，减少内存开销和反射损耗
 */
public class JsonUtils {

    private static final Gson GSON = new GsonBuilder()
        .setDateFormat("yyyy-MM-dd HH:mm:ss") // 统一日期格式化，提升转换效率
        .create();

    public static Gson getGson() {
        return GSON;
    }

    private JsonUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
}