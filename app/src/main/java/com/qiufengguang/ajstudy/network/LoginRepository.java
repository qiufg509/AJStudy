package com.qiufengguang.ajstudy.network;

import com.qiufengguang.ajstudy.data.User;
import com.qiufengguang.ajstudy.utils.SpUtils;

// LoginRepository.java
public class LoginRepository {
    private final SpUtils spManager;

    public LoginRepository() {
        this.spManager = new SpUtils();
    }

    public void login(String phone, String password, LoginCallback callback) {
        // 模拟网络请求
        new Thread(() -> {
            try {
                Thread.sleep(1500); // 模拟网络延迟

                // 模拟登录逻辑
                if ("123456".equals(password)) {
                    User user = new User(phone, password, true);
                    callback.onSuccess(user);
                } else {
                    callback.onError("密码错误");
                }
            } catch (InterruptedException e) {
                callback.onError("网络连接失败");
            }
        }).start();
    }

    public void saveUserInfo(User user) {
        spManager.saveUserInfo(user);
    }

    public User getSavedUser() {
        return spManager.getSavedUser();
    }
}