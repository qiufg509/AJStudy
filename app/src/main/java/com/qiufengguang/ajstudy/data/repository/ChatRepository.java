package com.qiufengguang.ajstudy.data.repository;

import com.qiufengguang.ajstudy.data.callback.BodyRespCallback;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.model.ChatMessage;
import com.qiufengguang.ajstudy.data.remote.api.DeepSeekApi;
import com.qiufengguang.ajstudy.data.remote.dto.ChatCompletionRequest;
import com.qiufengguang.ajstudy.data.remote.service.DeepSeekClient;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Ai对话消息仓库
 *
 * @author qiufengguang
 * @since 2026/3/28 18:20
 */
public class ChatRepository {
    private static volatile ChatRepository instance;

    private final DeepSeekApi api;

    private ChatRepository() {
        api = DeepSeekClient.getDeepSeekApi();
    }

    public static ChatRepository getInstance() {
        if (instance == null) {
            synchronized (ChatRepository.class) {
                if (instance == null) {
                    instance = new ChatRepository();
                }
            }
        }
        return instance;
    }

    public Call<ResponseBody> sendMessage(String msg, final OnDataLoadedCallback<String> callback) {
        List<ChatMessage> messages = new ArrayList<>();
        messages.add(new ChatMessage("user", msg));
        ChatCompletionRequest request = new ChatCompletionRequest(messages);
        Call<ResponseBody> call = api.chatCompletion(request);
        call.enqueue(new BodyRespCallback(callback));
        return call;
    }
}
