package com.qiufengguang.ajstudy.data.repository;

import com.google.gson.Gson;
import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.callback.LayoutRespCallback;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.remote.api.AppDetailApi;
import com.qiufengguang.ajstudy.data.remote.api.CommentApi;
import com.qiufengguang.ajstudy.data.remote.api.RecommendApi;
import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;

import retrofit2.Call;

/**
 * 应用详情页仓库层
 *
 * @author qiufengguang
 * @since 2026/2/28 16:19
 */
public class AppDetailRepository {
    private static volatile AppDetailRepository instance;

    private final AppDetailApi appDetailApi;

    private final CommentApi commentApi;

    private final RecommendApi recommendApi;

    private final Gson gson;

    private AppDetailRepository() {
        appDetailApi = RetrofitClient.getAppDetailApi();
        commentApi = RetrofitClient.getCommentApi();
        recommendApi = RetrofitClient.getRecommendApi();
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

    public Call<RawRespData> fetchAppDetailData(
        String directory, final OnDataLoadedCallback<PageData> callback) {
        Request request = new Request(directory);
        Call<RawRespData> call = appDetailApi.getAppDetailData(request);
        call.enqueue(new LayoutRespCallback(gson, callback));
        return call;
    }

    public Call<RawRespData> fetchCommentData(
        String directory, final OnDataLoadedCallback<PageData> callback) {
        Request request = new Request(directory);
        Call<RawRespData> call = commentApi.getCommentData(request);
        call.enqueue(new LayoutRespCallback(gson, callback));
        return call;
    }

    public Call<RawRespData> fetchRecommendData(
        String directory, final OnDataLoadedCallback<PageData> callback) {
        Request request = new Request(directory);
        Call<RawRespData> call = recommendApi.getRecommendData(request);
        call.enqueue(new LayoutRespCallback(gson, callback));
        return call;
    }
}