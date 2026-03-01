package com.qiufengguang.ajstudy.data.remote.api;

import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.router.Router;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * UserApi
 *
 * @author qiufengguang
 * @since 2026/2/28 14:48
 */
public interface UserApi {
    @POST(Router.URI.PAGE_USER)
    Call<RawRespData> getUserData(@Body Request request);
}