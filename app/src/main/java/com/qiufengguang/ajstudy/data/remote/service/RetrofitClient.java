package com.qiufengguang.ajstudy.data.remote.service;

import com.qiufengguang.ajstudy.data.remote.api.AppDetailApi;
import com.qiufengguang.ajstudy.data.remote.api.AppListApi;
import com.qiufengguang.ajstudy.data.remote.api.ArticleDetailApi;
import com.qiufengguang.ajstudy.data.remote.api.ArticleListApi;
import com.qiufengguang.ajstudy.data.remote.api.CommentApi;
import com.qiufengguang.ajstudy.data.remote.api.FavoritesApi;
import com.qiufengguang.ajstudy.data.remote.api.HelpFeedbackApi;
import com.qiufengguang.ajstudy.data.remote.api.HomeApi;
import com.qiufengguang.ajstudy.data.remote.api.KnowHowApi;
import com.qiufengguang.ajstudy.data.remote.api.MeApi;
import com.qiufengguang.ajstudy.data.remote.api.RecommendApi;
import com.qiufengguang.ajstudy.data.remote.api.StudyRecordApi;
import com.qiufengguang.ajstudy.data.remote.api.UserApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 客户端
 *
 * @author qiufengguang
 * @since 2026/2/26 14:14
 */
public class RetrofitClient {
    private static final String BASE_URL = "http://127.0.0.1:8080/";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }

    public static HomeApi getHomeApi() {
        return getInstance().create(HomeApi.class);
    }

    public static KnowHowApi getKnowHowApi() {
        return getInstance().create(KnowHowApi.class);
    }

    public static MeApi getMeApi() {
        return getInstance().create(MeApi.class);
    }

    public static AppListApi getAppListApi() {
        return getInstance().create(AppListApi.class);
    }

    public static ArticleListApi getArticleListApi() {
        return getInstance().create(ArticleListApi.class);
    }

    public static AppDetailApi getAppDetailApi() {
        return getInstance().create(AppDetailApi.class);
    }

    public static CommentApi getCommentApi() {
        return getInstance().create(CommentApi.class);
    }

    public static RecommendApi getRecommendApi() {
        return getInstance().create(RecommendApi.class);
    }

    public static ArticleDetailApi getArticleDetailApi() {
        return getInstance().create(ArticleDetailApi.class);
    }

    public static StudyRecordApi getStudyRecordApi() {
        return getInstance().create(StudyRecordApi.class);
    }

    public static FavoritesApi getFavoritesApi() {
        return getInstance().create(FavoritesApi.class);
    }

    public static UserApi getUserApi() {
        return getInstance().create(UserApi.class);
    }

    public static HelpFeedbackApi getHelpFeedbackApi() {
        return getInstance().create(HelpFeedbackApi.class);
    }
}