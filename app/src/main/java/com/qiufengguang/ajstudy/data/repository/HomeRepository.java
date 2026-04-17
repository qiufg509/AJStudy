package com.qiufengguang.ajstudy.data.repository;

import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.callback.LayoutRespCallback;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.remote.api.HomeApi;
import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;
import com.qiufengguang.ajstudy.utils.JsonUtils;

import retrofit2.Call;

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

    public Call<RawRespData> fetchHomeData(final OnDataLoadedCallback<PageData> callback) {
        Request request = new Request();
        Call<RawRespData> call = homeApi.getHomeData(request);
        // [性能重构]：使用 JsonUtils 获取全局单例 Gson
        call.enqueue(new LayoutRespCallback(JsonUtils.getGson(), callback));
        return call;
    }
}