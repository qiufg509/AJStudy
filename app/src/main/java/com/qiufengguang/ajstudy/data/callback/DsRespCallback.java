package com.qiufengguang.ajstudy.data.callback;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.data.model.ChatMessage;
import com.qiufengguang.ajstudy.data.remote.dto.DsRespData;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * DeepSeek数据请求响应结果回调
 *
 * @author qiufengguang
 * @since 2026/3/30 18:16
 */
public class DsRespCallback implements Callback<DsRespData> {
    private static final String TAG = "LayoutRespCallback";

    OnDataLoadedCallback<ChatMessage> callback;

    public DsRespCallback(OnDataLoadedCallback<ChatMessage> callback) {
        this.callback = callback;
    }

    @Override
    public void onResponse(
        @NonNull Call<DsRespData> call,
        @NonNull Response<DsRespData> response
    ) {
        if (response.isSuccessful() && response.body() != null) {
            DsRespData respData = response.body();
            List<DsRespData.Choices> choices = respData.getChoices();
            if (choices != null && !choices.isEmpty()) {
                callback.onSuccess(choices.get(0).getMessage());
            } else {
                Log.e(TAG, "Server error code: " + response.body());
                ChatMessage message = new ChatMessage(ChatMessage.ROLE_ASSISTANT,
                    "服务器异常：" + response.body());
                callback.onSuccess(message);
            }
        } else {
            Log.e(TAG, "Response unsuccessful.");
            ChatMessage message = new ChatMessage(ChatMessage.ROLE_ASSISTANT, "请求失败。");
            callback.onSuccess(message);
        }
    }

    @Override
    public void onFailure(@NonNull Call<DsRespData> call, @NonNull Throwable t) {
        Log.w(TAG, "onFailure: ", t);
        ChatMessage message = new ChatMessage(ChatMessage.ROLE_ASSISTANT, t.getMessage());
        callback.onSuccess(message);
    }
}