package com.qiufengguang.ajstudy.data.remote.api;

import com.qiufengguang.ajstudy.data.remote.dto.HomeResponse;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * 首页接口
 *
 * @author qiufengguang
 * @since 2026/2/26 14:12
 */
public interface HomeApi {
    @GET("home")
    Call<HomeResponse> getHomeData();
}