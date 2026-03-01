package com.qiufengguang.ajstudy.data.remote.api;

import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.router.Router;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * AppDetailApi
 *
 * @author qiufengguang
 * @since 2026/2/28 16:24
 */
public interface AppDetailApi {
    @POST(Router.URI.PAGE_APP_DETAIL)
    Call<RawRespData> getAppDetailData(@Body Request request);
}