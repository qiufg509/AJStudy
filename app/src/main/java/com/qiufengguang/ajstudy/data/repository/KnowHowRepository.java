package com.qiufengguang.ajstudy.data.repository;

import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.converter.LayoutDataConverter;
import com.qiufengguang.ajstudy.data.remote.api.KnowHowApi;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;
import com.qiufengguang.ajstudy.utils.JsonUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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


    public Observable<PageData> fetchKnowHowData() {
        Request request = new Request();
        return api.getKnowHowData(request)
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