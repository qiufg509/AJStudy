package com.qiufengguang.ajstudy.data.remote.service;

import com.qiufengguang.ajstudy.data.remote.api.HomeApi;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit 客户端
 *
 * @author qiufengguang
 * @since 2026/2/26 14:14
 */
public class RetrofitClient {
    private static final String BASE_URL = "http://127.0.0.1:8080/";
    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }

    public static HomeApi getHomeApi() {
        return getInstance().create(HomeApi.class);
    }
}