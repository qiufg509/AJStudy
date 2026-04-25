package com.qiufengguang.ajstudy.data.repository;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.converter.LayoutDataConverter;
import com.qiufengguang.ajstudy.data.remote.api.AppListApi;
import com.qiufengguang.ajstudy.data.remote.api.ArticleListApi;
import com.qiufengguang.ajstudy.data.remote.api.FavoritesApi;
import com.qiufengguang.ajstudy.data.remote.api.HelpFeedbackApi;
import com.qiufengguang.ajstudy.data.remote.api.StudyRecordApi;
import com.qiufengguang.ajstudy.data.remote.api.UserApi;
import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.utils.JsonUtils;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 二级页仓库层
 * [性能专家重构]：复用全局单例 Gson，移除冗余实例创建
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

    private SecondaryRepository() {
        appListApi = RetrofitClient.getAppListApi();
        articleListApi = RetrofitClient.getArticleListApi();
        favoritesApi = RetrofitClient.getFavoritesApi();
        studyRecordApi = RetrofitClient.getStudyRecordApi();
        userApi = RetrofitClient.getUserApi();
        helpFeedbackApi = RetrofitClient.getHelpFeedbackApi();
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

    public Observable<PageData> fetchData(@NonNull String uri, String directory) {
        Observable<RawRespData> observable;
        switch (uri) {
            case Router.URI.PAGE_APP_LIST:
                observable = fetchAppListData(directory);
                break;
            case Router.URI.PAGE_ARTICLE_LIST:
                observable = fetchArticleListData(directory);
                break;
            case Router.URI.PAGE_FAVORITES:
                observable = fetchFavoritesData();
                break;
            case Router.URI.PAGE_STUDY_RECORD:
                observable = fetchStudyRecordData();
                break;
            case Router.URI.PAGE_USER:
                observable = fetchUserData();
                break;
            case Router.URI.PAGE_HELP_FEEDBACK:
                observable = fetchHelpFeedbackData();
                break;
            default:
                return Observable.error(new IllegalArgumentException("Unsupported URI: " + uri));
        }
        return observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(rawRespData -> {
                if (rawRespData.isSuccess()) {
                    return LayoutDataConverter.convert(JsonUtils.getGson(), rawRespData);
                } else {
                    throw new Exception("Server error: " + rawRespData.getRtnCode());
                }
            });
    }

    private Observable<RawRespData> fetchAppListData(String directory) {
        Request request = new Request(directory);
        return appListApi.getAppListData(request);
    }

    private Observable<RawRespData> fetchArticleListData(String directory) {
        Request request = new Request(directory);
        return articleListApi.getArticleListData(request);
    }

    private Observable<RawRespData> fetchFavoritesData() {
        Request request = new Request();
        return favoritesApi.getFavoritesData(request);
    }

    private Observable<RawRespData> fetchStudyRecordData() {
        Request request = new Request();
        return studyRecordApi.getStudyRecordData(request);
    }

    private Observable<RawRespData> fetchUserData() {
        Request request = new Request();
        return userApi.getUserData(request);
    }

    private Observable<RawRespData> fetchHelpFeedbackData() {
        Request request = new Request();
        return helpFeedbackApi.getHelpFeedbackData(request);
    }
}