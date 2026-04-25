package com.qiufengguang.ajstudy.data.repository;

import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.converter.LayoutDataConverter;
import com.qiufengguang.ajstudy.data.remote.api.HomeApi;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;
import com.qiufengguang.ajstudy.utils.JsonUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
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

    public Observable<PageData> fetchHomeData() {
        Request request = new Request();
        return homeApi.getHomeData(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(rawRespData -> {
                if (rawRespData.isSuccess()) {
                    return LayoutDataConverter.convert(JsonUtils.getGson(), rawRespData);
                } else {
                    throw new Exception("Server error: " + rawRespData.getRtnCode());
                }
            });
    }
}