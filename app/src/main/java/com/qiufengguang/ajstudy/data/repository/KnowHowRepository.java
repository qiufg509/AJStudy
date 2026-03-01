package com.qiufengguang.ajstudy.data.repository;

import com.google.gson.Gson;
import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.callback.LayoutRespCallback;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.remote.api.KnowHowApi;
import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;

import retrofit2.Call;

/**
 * 知识页页仓库层
 *
 * @author qiufengguang
 * @since 2026/2/28 15:12
 */
public class KnowHowRepository {
    private static volatile KnowHowRepository instance;

    private final KnowHowApi api;

    private final Gson gson;

    private KnowHowRepository() {
        api = RetrofitClient.getKnowHowApi();
        gson = new Gson();
    }

    public static KnowHowRepository getInstance() {
        if (instance == null) {
            synchronized (KnowHowRepository.class) {
                if (instance == null) {
                    instance = new KnowHowRepository();
                }
            }
        }
        return instance;
    }


    public Call<RawRespData> fetchKnowHowData(final OnDataLoadedCallback<PageData> callback) {
        Request request = new Request();
        Call<RawRespData> call = api.getKnowHowData(request);
        call.enqueue(new LayoutRespCallback(gson, callback));
        return call;
    }
}