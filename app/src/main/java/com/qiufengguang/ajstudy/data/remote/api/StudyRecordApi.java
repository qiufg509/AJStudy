package com.qiufengguang.ajstudy.data.remote.api;

import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.router.Router;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * StudyRecordApi
 *
 * @author qiufengguang
 * @since 2026/2/28 14:48
 */
public interface StudyRecordApi {
    @POST(Router.URI.PAGE_STUDY_RECORD)
    Call<RawRespData> getStudyRecordData(@Body Request request);
}