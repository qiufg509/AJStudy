package com.qiufengguang.ajstudy.network;

import com.qiufengguang.ajstudy.data.User;

public interface LoginCallback {
    void onSuccess(User user);

    void onError(String error);
}
