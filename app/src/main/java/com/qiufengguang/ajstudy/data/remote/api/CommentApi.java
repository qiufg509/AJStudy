package com.qiufengguang.ajstudy.data.remote.api;

import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.router.Router;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * CommentApi
 *
 * @author qiufengguang
 * @since 2026/3/2 15:48
 */
public interface CommentApi {
    @POST(Router.URI.PAGE_COMMENT)
    Call<RawRespData> getCommentData(@Body Request request);
}