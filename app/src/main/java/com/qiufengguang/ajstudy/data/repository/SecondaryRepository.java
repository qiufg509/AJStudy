package com.qiufengguang.ajstudy.data.repository;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.callback.LayoutRespCallback;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.remote.api.AppListApi;
import com.qiufengguang.ajstudy.data.remote.api.ArticleListApi;
import com.qiufengguang.ajstudy.data.remote.api.FavoritesApi;
import com.qiufengguang.ajstudy.data.remote.api.HelpFeedbackApi;
import com.qiufengguang.ajstudy.data.remote.api.StudyRecordApi;
import com.qiufengguang.ajstudy.data.remote.api.UserApi;
import com.qiufengguang.ajstudy.data.remote.dto.LayoutResponse;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;
import com.qiufengguang.ajstudy.router.Router;

import java.util.List;

import retrofit2.Call;

/**
 * 二级页仓库层
 *
 * @author qiufengguang
 * @since 2026/2/28 15:15
 */
public class SecondaryRepository {
    private static volatile SecondaryRepository instance;

    private final AppListApi appListApi;

    private final ArticleListApi articleListApi;

    private final FavoritesApi favoritesApi;

    private final StudyRecordApi studyRecordApi;

    private final UserApi userApi;
    private final HelpFeedbackApi helpFeedbackApi;

    private final Gson gson;

    private SecondaryRepository() {
        appListApi = RetrofitClient.getAppListApi();
        articleListApi = RetrofitClient.getArticleListApi();
        favoritesApi = RetrofitClient.getFavoritesApi();
        studyRecordApi = RetrofitClient.getStudyRecordApi();
        userApi = RetrofitClient.getUserApi();
        helpFeedbackApi = RetrofitClient.getHelpFeedbackApi();
        gson = new Gson();
    }

    public static SecondaryRepository getInstance() {
        if (instance == null) {
            synchronized (SecondaryRepository.class) {
                if (instance == null) {
                    instance = new SecondaryRepository();
                }
            }
        }
        return instance;
    }

    public Call<LayoutResponse> fetchData(
        @NonNull String uri,
        String directory,
        final OnDataLoadedCallback<List<LayoutData<?>>> callback
    ) {
        switch (uri) {
            case Router.URI.PAGE_APP_LIST:
                return fetchAppListData(directory, callback);
            case Router.URI.PAGE_ARTICLE_LIST:
                return fetchArticleListData(directory, callback);
            case Router.URI.PAGE_FAVORITES:
                return fetchFavoritesData(callback);
            case Router.URI.PAGE_STUDY_RECORD:
                return fetchStudyRecordData(callback);
            case Router.URI.PAGE_USER:
                return fetchUserData(callback);
            case Router.URI.PAGE_HELP_FEEDBACK:
                return fetchHelpFeedbackData(callback);
            default:
                return null;
        }
    }

    public Call<LayoutResponse> fetchAppListData(String directory, final OnDataLoadedCallback<List<LayoutData<?>>> callback) {
        Request request = new Request(directory);
        Call<LayoutResponse> call = appListApi.getAppListData(request);
        call.enqueue(new LayoutRespCallback(gson, callback));
        return call;
    }

    public Call<LayoutResponse> fetchArticleListData(String directory, final OnDataLoadedCallback<List<LayoutData<?>>> callback) {
        Request request = new Request(directory);
        Call<LayoutResponse> call = articleListApi.getArticleListData(request);
        call.enqueue(new LayoutRespCallback(gson, callback));
        return call;
    }

    public Call<LayoutResponse> fetchFavoritesData(final OnDataLoadedCallback<List<LayoutData<?>>> callback) {
        Request request = new Request();
        Call<LayoutResponse> call = favoritesApi.getFavoritesData(request);
        call.enqueue(new LayoutRespCallback(gson, callback));
        return call;
    }

    public Call<LayoutResponse> fetchStudyRecordData(final OnDataLoadedCallback<List<LayoutData<?>>> callback) {
        Request request = new Request();
        Call<LayoutResponse> call = studyRecordApi.getStudyRecordData(request);
        call.enqueue(new LayoutRespCallback(gson, callback));
        return call;
    }

    public Call<LayoutResponse> fetchUserData(final OnDataLoadedCallback<List<LayoutData<?>>> callback) {
        Request request = new Request();
        Call<LayoutResponse> call = userApi.getUserData(request);
        call.enqueue(new LayoutRespCallback(gson, callback));
        return call;
    }

    public Call<LayoutResponse> fetchHelpFeedbackData(final OnDataLoadedCallback<List<LayoutData<?>>> callback) {
        Request request = new Request();
        Call<LayoutResponse> call = helpFeedbackApi.getHelpFeedbackData(request);
        call.enqueue(new LayoutRespCallback(gson, callback));
        return call;
    }
}