package com.qiufengguang.ajstudy.network;

import android.os.Handler;
import android.os.HandlerThread;

import com.qiufengguang.ajstudy.data.User;
import com.qiufengguang.ajstudy.utils.SpUtils;

/**
 * 登录操作
 *
 * @author qiufengguang
 * @since 2025/11/30 2:15
 */
public class LoginRepository {
    private final SpUtils spManager;

    private HandlerThread thread;

    public LoginRepository() {
        this.spManager = new SpUtils();
    }

    public void login(String phone, String password, LoginCallback callback) {
        if (thread == null) {
            thread = new HandlerThread("Handler-Login");
        }
        if (!thread.isAlive()) {
            thread.start();
        }
        Handler handler = new Handler(thread.getLooper());
        // 模拟网络请求
        handler.post(() -> {
            try {
                Thread.sleep(1500); // 模拟网络延迟

                // 模拟登录逻辑
                if ("123456".equals(password)) {
                    User user = new User(phone, password);
                    callback.onSuccess(user);
                } else {
                    callback.onError("密码错误");
                }
            } catch (InterruptedException e) {
                callback.onError("网络连接失败");
            }
        });
    }

    public void saveUserInfo(User user) {
        spManager.saveUserInfo(user);
    }

    public User getSavedUser() {
        return spManager.getSavedUser();
    }

    public void release() {
        if (thread != null) {
            thread.quitSafely();
            thread = null;
        }
    }
}