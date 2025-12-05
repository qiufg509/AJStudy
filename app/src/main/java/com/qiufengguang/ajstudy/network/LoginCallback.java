package com.qiufengguang.ajstudy.network;

import com.qiufengguang.ajstudy.data.User;

/**
 * 登录操作回调
 *
 * @author qiufengguang
 * @since 2025/11/30 2:15
 */
public interface LoginCallback {
    void onSuccess(User user);

    void onError(String error);
}
