package com.qiufengguang.ajstudy.data.callback;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.converter.LayoutDataConverter;
import com.qiufengguang.ajstudy.data.remote.dto.LayoutResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 布局数据请求响应结果回调
 *
 * @author qiufengguang
 * @since 2026/2/28 15:00
 */
public class LayoutRespCallback implements Callback<LayoutResponse> {
    private static final String TAG = "LayoutRespCallback";

    OnDataLoadedCallback<List<LayoutData<?>>> callback;

    private final Gson gson;

    public LayoutRespCallback(Gson gson, OnDataLoadedCallback<List<LayoutData<?>>> callback) {
        this.gson = gson;
        this.callback = callback;
    }

    @Override
    public void onResponse(
        @NonNull Call<LayoutResponse> call,
        @NonNull Response<LayoutResponse> response
    ) {
        if (response.isSuccessful() && response.body() != null) {
            LayoutResponse layoutResponse = response.body();
            if (layoutResponse.isSuccess()) {
                List<LayoutData<?>> layoutDataList = LayoutDataConverter.convert(gson, layoutResponse.getLayoutData());
                callback.onSuccess(layoutDataList);
            } else {
                Log.e(TAG, "Server error code: " + layoutResponse.getRtnCode());
                callback.onFailure(new Exception("Server error"));
            }
        } else {
            callback.onFailure(new Exception("Response unsuccessful"));
        }
    }

    @Override
    public void onFailure(@NonNull Call<LayoutResponse> call, @NonNull Throwable t) {
        callback.onFailure(t);
    }
}