package com.qiufengguang.ajstudy.data.remote.api;

import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.router.Router;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * RecommendApi
 *
 * @author qiufengguang
 * @since 2026/3/2 15:46
 */
public interface RecommendApi {
    @POST(Router.URI.PAGE_RECOMMEND)
    Call<RawRespData> getRecommendData(@Body Request request);
}