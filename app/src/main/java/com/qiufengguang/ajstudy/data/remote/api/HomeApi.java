package com.qiufengguang.ajstudy.data.remote.api;

import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.router.Router;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * HomeApi
 *
 * @author qiufengguang
 * @since 2026/2/28 14:49
 */
public interface HomeApi {
    @POST(Router.URI.PAGE_HOME)
    Call<RawRespData> getHomeData(@Body Request request);
}