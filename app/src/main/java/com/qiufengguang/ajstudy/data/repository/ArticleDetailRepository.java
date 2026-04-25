package com.qiufengguang.ajstudy.data.repository;

import com.qiufengguang.ajstudy.data.remote.api.ArticleDetailApi;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import okhttp3.ResponseBody;

/**
 * 文章详情页仓库层
 *
 * @author qiufengguang
 * @since 2026/2/28 16:20
 */
public class ArticleDetailRepository {
    private static volatile ArticleDetailRepository instance;

    private final ArticleDetailApi appListApi;


    private ArticleDetailRepository() {
        appListApi = RetrofitClient.getArticleDetailApi();
    }

    public static ArticleDetailRepository getInstance() {
        if (instance == null) {
            synchronized (ArticleDetailRepository.class) {
                if (instance == null) {
                    instance = new ArticleDetailRepository();
                }
            }
        }
        return instance;
    }

    public Observable<String> fetchArticleDetailApi(String directory) {
        Request request = new Request(directory);
        return appListApi.getArticleDetailData(request)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .map(ResponseBody::string);
    }
}