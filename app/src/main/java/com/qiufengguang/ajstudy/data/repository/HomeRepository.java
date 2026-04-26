package com.qiufengguang.ajstudy.data.repository;

import com.qiufengguang.ajstudy.data.database.AppDatabase;
import com.qiufengguang.ajstudy.data.model.HomeCache;
import com.qiufengguang.ajstudy.data.remote.api.HomeApi;
import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;
import com.qiufengguang.ajstudy.utils.JsonUtils;

import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 首页仓库层
 * [性能专家重构]：复用全局单例 Gson，移除冗余实例创建
 *
 * @author qiufengguang
 * @since 2026/2/26 14:49
 */
public class HomeRepository {
    private static volatile HomeRepository instance;

    private final HomeApi homeApi;

    private HomeRepository() {
        homeApi = RetrofitClient.getHomeApi();
    }

    public static HomeRepository getInstance() {
        if (instance == null) {
            synchronized (HomeRepository.class) {
                if (instance == null) {
                    instance = new HomeRepository();
                }
            }
        }
        return instance;
    }

    public Observable<RawRespData> fetchHomeRawData() {
        Request request = new Request();
        return homeApi.getHomeData(request)
            .subscribeOn(Schedulers.io());
    }

    public Maybe<RawRespData> getLocalRawData() {
        return AppDatabase.getInstance().homeCacheDao().getHomeCache()
            .map(cache -> JsonUtils.getGson().fromJson(cache.content, RawRespData.class))
            .subscribeOn(Schedulers.io());
    }

    /**
     * 保存数据到缓存
     *
     * @param rawData 页面数据
     */
    public void saveRawDataToCache(RawRespData rawData) {
        String json = JsonUtils.getGson().toJson(rawData);
        AppDatabase.getInstance().homeCacheDao().saveHomeCache(new HomeCache(json))
            .subscribeOn(Schedulers.io())
            .subscribe();
    }
}
