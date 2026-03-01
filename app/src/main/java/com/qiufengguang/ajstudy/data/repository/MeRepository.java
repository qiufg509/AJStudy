package com.qiufengguang.ajstudy.data.repository;

import com.google.gson.Gson;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.callback.LayoutRespCallback;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.remote.api.MeApi;
import com.qiufengguang.ajstudy.data.remote.dto.LayoutResponse;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;

import java.util.List;

import retrofit2.Call;

/**
 * 我的页仓库层
 *
 * @author qiufengguang
 * @since 2026/2/28 15:12
 */
public class MeRepository {
    private static volatile MeRepository instance;

    private final MeApi api;

    private final Gson gson;

    private MeRepository() {
        api = RetrofitClient.getMeApi();
        gson = new Gson();
    }

    public static MeRepository getInstance() {
        if (instance == null) {
            synchronized (MeRepository.class) {
                if (instance == null) {
                    instance = new MeRepository();
                }
            }
        }
        return instance;
    }

    public Call<LayoutResponse> fetchMeData(final OnDataLoadedCallback<List<LayoutData<?>>> callback) {
        Request request = new Request();
        Call<LayoutResponse> call = api.getMeData(request);
        call.enqueue(new LayoutRespCallback(gson, callback));
        return call;
    }
}