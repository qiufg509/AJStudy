package com.qiufengguang.ajstudy.data.callback;

/**
 * 数据响应回调
 *
 * @author qiufengguang
 * @since 2026/2/26 14:59
 */
public interface OnDataLoadedCallback<T> {
    void onSuccess(T data);

    void onFailure(Throwable t);
}