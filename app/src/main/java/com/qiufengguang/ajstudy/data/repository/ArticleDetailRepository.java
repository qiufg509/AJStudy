package com.qiufengguang.ajstudy.data.repository;

import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.callback.BodyRespCallback;
import com.qiufengguang.ajstudy.data.remote.api.ArticleDetailApi;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;

import okhttp3.ResponseBody;
import retrofit2.Call;

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

    public Call<ResponseBody> fetchArticleDetailApi(
        String directory,
        final OnDataLoadedCallback<String> callback
    ) {
        Request request = new Request(directory);
        Call<ResponseBody> call = appListApi.getArticleDetailData(request);
        call.enqueue(new BodyRespCallback(callback));
        return call;
    }
}