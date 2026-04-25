package com.qiufengguang.ajstudy.data.remote.api;

import com.qiufengguang.ajstudy.data.remote.dto.RawRespData;
import com.qiufengguang.ajstudy.data.remote.dto.Request;
import com.qiufengguang.ajstudy.router.Router;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * AppListApi
 *
 * @author qiufengguang
 * @since 2026/2/28 14:49
 */
public interface AppListApi {
    @POST(Router.URI.PAGE_APP_LIST)
    Observable<RawRespData> getAppListData(@Body Request request);
}