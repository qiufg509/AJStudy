package com.qiufengguang.ajstudy.data.repository;

import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.callback.LayoutRespCallback;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.remote.api.KnowHowApi;
import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;
import com.qiufengguang.ajstudy.utils.JsonUtils;

import retrofit2.Call;

/**
 * 知识页页仓库层
 * [性能专家重构]：复用全局单例 Gson，移除冗余实例创建
 *
 * @author qiufengguang
 * @since 2026/2/28 15:12
 */
public class KnowHowRepository {
    private static volatile KnowHowRepository instance;

    private final KnowHowApi api;

    private KnowHowRepository() {
        api = RetrofitClient.getKnowHowApi();
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
        // [性能重构]：使用 JsonUtils 获取全局单例 Gson
        call.enqueue(new LayoutRespCallback(JsonUtils.getGson(), callback));
        return call;
    }
}