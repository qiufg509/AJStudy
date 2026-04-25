package com.qiufengguang.ajstudy.data.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * 首页缓存数据
 *
 * @author qiufengguang
 * @since 2026/4/26 1:50
 */
@Entity(tableName = "home_cache")
public class HomeCache {
    @PrimaryKey
    @NonNull
    public String key = "home_data";
    public String content;

    public HomeCache() {}

    @Ignore
    public HomeCache(String content) {
        this.content = content;
    }
}
