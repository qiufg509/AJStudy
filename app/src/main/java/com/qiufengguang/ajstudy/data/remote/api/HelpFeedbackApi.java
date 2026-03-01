package com.qiufengguang.ajstudy.data.remote.api;

import com.qiufengguang.ajstudy.data.remote.dto.LayoutResponse;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.router.Router;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * HelpFeedbackApi
 *
 * @author qiufengguang
 * @since 2026/2/28 18:33
 */
public interface HelpFeedbackApi {
    @POST(Router.URI.PAGE_HELP_FEEDBACK)
    Call<LayoutResponse> getHelpFeedbackData(@Body Request request);
}