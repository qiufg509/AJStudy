package com.qiufengguang.ajstudy.data.remote.api;

import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.router.Router;

import io.reactivex.rxjava3.core.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * ArticleDetailApi
 *
 * @author qiufengguang
 * @since 2026/2/28 16:25
 */
public interface ArticleDetailApi {
    @POST(Router.URI.PAGE_ARTICLE_DETAIL)
    Observable<ResponseBody> getArticleDetailData(@Body Request request);
}