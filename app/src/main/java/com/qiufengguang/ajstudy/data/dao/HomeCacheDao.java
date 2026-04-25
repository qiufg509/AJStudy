package com.qiufengguang.ajstudy.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.qiufengguang.ajstudy.data.model.HomeCache;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Single;

/**
 * 首页缓存DAO接口
 *
 * @author qiufengguang
 * @since 2026/4/26 1:51
 */
@Dao
public interface HomeCacheDao {
    @Query("SELECT * FROM home_cache WHERE `key` = 'home_data' LIMIT 1")
    Maybe<HomeCache> getHomeCache();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> saveHomeCache(HomeCache cache);
}
