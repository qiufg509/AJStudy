package com.qiufengguang.ajstudy.data.remote.api;

import com.qiufengguang.ajstudy.data.remote.dto.LayoutResponse;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.router.Router;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * KnowHowApi
 *
 * @author qiufengguang
 * @since 2026/2/28 14:49
 */
public interface KnowHowApi {
    @POST(Router.URI.PAGE_KNOW_HOW)
    Call<LayoutResponse> getKnowHowData(@Body Request request);
}