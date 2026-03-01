package com.qiufengguang.ajstudy.data.callback;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.converter.LayoutDataConverter;
import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 布局数据请求响应结果回调
 *
 * @author qiufengguang
 * @since 2026/2/28 15:00
 */
public class LayoutRespCallback implements Callback<RawRespData> {
    private static final String TAG = "LayoutRespCallback";

    OnDataLoadedCallback<PageData> callback;

    private final Gson gson;

    public LayoutRespCallback(Gson gson, OnDataLoadedCallback<PageData> callback) {
        this.gson = gson;
        this.callback = callback;
    }

    @Override
    public void onResponse(
        @NonNull Call<RawRespData> call,
        @NonNull Response<RawRespData> response
    ) {
        if (response.isSuccessful() && response.body() != null) {
            RawRespData rawRespData = response.body();
            if (rawRespData.isSuccess()) {
                PageData pageData = LayoutDataConverter.convert(gson, rawRespData);
                callback.onSuccess(pageData);
            } else {
                Log.e(TAG, "Server error code: " + rawRespData.getRtnCode());
                callback.onFailure(new Exception("Server error"));
            }
        } else {
            callback.onFailure(new Exception("Response unsuccessful"));
        }
    }

    @Override
    public void onFailure(@NonNull Call<RawRespData> call, @NonNull Throwable t) {
        callback.onFailure(t);
    }
}