package com.qiufengguang.ajstudy.data.remote.api;

import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.router.Router;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * ArticleListApi
 *
 * @author qiufengguang
 * @since 2026/2/28 14:49
 */
public interface ArticleListApi {
    @POST(Router.URI.PAGE_ARTICLE_LIST)
    Call<RawRespData> getArticleListData(@Body Request request);
}