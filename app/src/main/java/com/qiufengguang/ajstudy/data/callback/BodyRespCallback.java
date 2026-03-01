package com.qiufengguang.ajstudy.data.callback;

import androidx.annotation.NonNull;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 字符内容请求响应结果回调
 *
 * @author qiufengguang
 * @since 2026/2/28 16:17
 */
public class BodyRespCallback implements Callback<ResponseBody> {
    OnDataLoadedCallback<String> callback;

    public BodyRespCallback(OnDataLoadedCallback<String> callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(@NonNull Call<ResponseBody> call, Response<ResponseBody> response) {
        if (response.isSuccessful()) {
            try (ResponseBody body = response.body()) {
                if (body != null) {
                    callback.onSuccess(body.string());
                }
            } catch (IOException e) {
                callback.onFailure(new Exception("Response unsuccessful"));
            }
        } else {
            callback.onFailure(new Exception("Response unsuccessful"));
        }
    }

    @Override
    public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
        callback.onFailure(t);
    }
}