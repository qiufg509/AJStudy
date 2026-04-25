package com.qiufengguang.ajstudy.data.repository;

import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.converter.LayoutDataConverter;
import com.qiufengguang.ajstudy.data.remote.api.AppDetailApi;
import com.qiufengguang.ajstudy.data.remote.api.CommentApi;
import com.qiufengguang.ajstudy.data.remote.api.RecommendApi;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;
import com.qiufengguang.ajstudy.utils.JsonUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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

    private AppDetailRepository() {
        appDetailApi = RetrofitClient.getAppDetailApi();
        commentApi = RetrofitClient.getCommentApi();
        recommendApi = RetrofitClient.getRecommendApi();
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

    public Observable<PageData> fetchAppDetailData(String directory) {
        Request request = new Request(directory);
        return appDetailApi.getAppDetailData(request)
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

    public Observable<PageData> fetchCommentData(String directory) {
        Request request = new Request(directory);
        return commentApi.getCommentData(request)
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

    public Observable<PageData> fetchRecommendData(String directory) {
        Request request = new Request(directory);
        return recommendApi.getRecommendData(request)
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