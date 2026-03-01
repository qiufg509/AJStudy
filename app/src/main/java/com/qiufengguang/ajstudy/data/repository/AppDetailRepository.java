package com.qiufengguang.ajstudy.data.repository;

import com.google.gson.Gson;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.callback.LayoutRespCallback;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.remote.api.AppDetailApi;
import com.qiufengguang.ajstudy.data.remote.dto.LayoutResponse;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;

import java.util.List;

import retrofit2.Call;

/**
 * 应用详情页仓库层
 *
 * @author qiufengguang
 * @since 2026/2/28 16:19
 */
public class AppDetailRepository {
    private static volatile AppDetailRepository instance;

    private final AppDetailApi api;

    private final Gson gson;

    private AppDetailRepository() {
        api = RetrofitClient.getAppDetailApi();
        gson = new Gson();
    }

    public static AppDetailRepository getInstance() {
        if (instance == null) {
            synchronized (AppDetailRepository.class) {
                if (instance == null) {
                    instance = new AppDetailRepository();
                }
            }
        }
        return instance;
    }

    public Call<LayoutResponse> fetchAppDetailData(final OnDataLoadedCallback<List<LayoutData<?>>> callback) {
        Request request = new Request();
        Call<LayoutResponse> call = api.getAppDetailData(request);
        call.enqueue(new LayoutRespCallback(gson, callback));
        return call;
    }
}