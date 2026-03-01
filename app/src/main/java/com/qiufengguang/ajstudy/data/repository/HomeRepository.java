package com.qiufengguang.ajstudy.data.repository;

import com.google.gson.Gson;
import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.callback.LayoutRespCallback;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.remote.api.HomeApi;
import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;

import retrofit2.Call;

/**
 * 首页仓库层
 *
 * @author qiufengguang
 * @since 2026/2/26 14:49
 */
public class HomeRepository {
    private static volatile HomeRepository instance;

    private final HomeApi homeApi;

    private final Gson gson;

    private HomeRepository() {
        homeApi = RetrofitClient.getHomeApi();
        gson = new Gson();
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
        call.enqueue(new LayoutRespCallback(gson, callback));
        return call;
    }
}