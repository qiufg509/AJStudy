package com.qiufengguang.ajstudy.data.remote.api;

import com.qiufengguang.ajstudy.data.remote.dto.ChatCompletionRequest;
import com.qiufengguang.ajstudy.router.Router;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;

/**
 * DeepSeekApi
 *
 * @author qiufengguang
 * @since 2026/3/23 14:49
 */
public interface DeepSeekApi {
    /**
     * 非流式
     *
     * @param request 请求ChatCompletionRequest
     * @return 响应
     */
    @POST(Router.URI.DEEPSEEK_CHAT)
    @Headers("Content-Type: application/json")
    Call<ResponseBody> chatCompletion(@Body ChatCompletionRequest request);

    /**
     * 流式
     *
     * @param request 请求ChatCompletionRequest
     * @return 响应流
     */
    @Streaming
    @POST(Router.URI.DEEPSEEK_CHAT)
    Call<ResponseBody> streamChatCompletion(@Body ChatCompletionRequest request);
}