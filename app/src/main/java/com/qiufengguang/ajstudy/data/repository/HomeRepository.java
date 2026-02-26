package com.qiufengguang.ajstudy.data.repository;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.converter.LayoutDataConverter;
import com.qiufengguang.ajstudy.data.remote.api.HomeApi;
import com.qiufengguang.ajstudy.data.remote.dto.HomeResponse;
import com.qiufengguang.ajstudy.data.remote.service.RetrofitClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 首页仓库层
 *
 * @author qiufengguang
 * @since 2026/2/26 14:49
 */
public class HomeRepository {
    private static final String TAG = "HomeRepository";
    private static volatile HomeRepository instance;
    private final HomeApi homeApi;
    private final Gson gson;

    private HomeRepository() {
        homeApi = RetrofitClient.getHomeApi();
        gson = new Gson();
    }

    public static HomeRepository getInstance() {
        if (instance == null) {
            synchronized (HomeRepository.class) {
                if (instance == null) {
                    instance = new HomeRepository();
                }
            }
        }
        return instance;
    }

    public Call<HomeResponse> fetchHomeData(final OnDataLoadedCallback<List<LayoutData<?>>> callback) {
        Call<HomeResponse> call = homeApi.getHomeData();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(
                @NonNull Call<HomeResponse> call,
                @NonNull Response<HomeResponse> response
            ) {
                if (response.isSuccessful() && response.body() != null) {
                    HomeResponse homeResponse = response.body();
                    if (homeResponse.isSuccess()) {
                        List<LayoutData<?>> layoutDataList = LayoutDataConverter.convert(gson,homeResponse.getLayoutData());
                        callback.onSuccess(layoutDataList);
                    } else {
                        Log.e(TAG, "Server error code: " + homeResponse.getRtnCode());
                        callback.onFailure(new Exception("Server error"));
                    }
                } else {
                    callback.onFailure(new Exception("Response unsuccessful"));
                }
            }

            @Override
            public void onFailure(@NonNull Call<HomeResponse> call, @NonNull Throwable t) {
                callback.onFailure(t);
            }
        });
        return call;
    }
}