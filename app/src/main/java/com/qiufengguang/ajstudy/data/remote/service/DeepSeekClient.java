package com.qiufengguang.ajstudy.data.remote.service;

import com.qiufengguang.ajstudy.BuildConfig;
import com.qiufengguang.ajstudy.data.remote.api.DeepSeekApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * DeepSeek 客户端
 *
 * @author qiufengguang
 * @since 2026/3/20 23:14
 */
public class DeepSeekClient {
    /**
     * deepseek api 固定地址
     */
    private static final String BASE_URL = "https://api.deepseek.com/";

    private static Retrofit retrofit;

    public static Retrofit getInstance() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        }
        return retrofit;
    }

    private static OkHttpClient getOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
            .addInterceptor(chain -> {
                okhttp3.Request original = chain.request();
                okhttp3.Request request = original.newBuilder()
                    .header("Authorization", "Bearer " + BuildConfig.DEEPSEEK_API_KEY)
                    .method(original.method(), original.body())
                    .build();
                return chain.proceed(request);
            })
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS);

        return builder.build();
    }

    public static DeepSeekApi getDeepSeekApi() {
        return getInstance().create(DeepSeekApi.class);
    }
}