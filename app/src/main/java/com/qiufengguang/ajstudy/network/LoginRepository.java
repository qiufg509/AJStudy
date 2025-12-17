package com.qiufengguang.ajstudy.network;

import android.os.Handler;
import android.os.HandlerThread;

import com.qiufengguang.ajstudy.data.User;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.utils.SpUtils;

import java.util.Map;

/**
 * 登录操作
 *
 * @author qiufengguang
 * @since 2025/11/30 2:15
 */
public class LoginRepository {

    private HandlerThread thread;

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
        SpUtils.getInstance().commitBatch(Constant.SP.PREF_USER, (editor, cache, spName) -> {
            String phone = user.getPhone();
            editor.putString("phone", phone);
            editor.putString("password", user.getPassword());
            editor.putBoolean("rememberPwd", user.isRememberPwd());
            editor.putLong("timestamp", user.getTimestamp());

            // 同时更新内存缓存
            cache.put(spName + "phone", phone);
            cache.put(spName + "password", user.getPassword());
            cache.put(spName + "rememberPwd", user.isRememberPwd());
            cache.put(spName + "timestamp", user.getTimestamp());
        });
    }

    public User getSavedUser() {
        Map<String, ?> all = SpUtils.getInstance().getAll(Constant.SP.PREF_USER);
        if (all.isEmpty()) {
            return null;
        }
        User user = new User();
        Object timestamp = all.get("timestamp");
        if (timestamp instanceof Long) {
            user.setTimestamp((Long) timestamp);
        }
        Object phone = all.get("phone");
        if (timestamp instanceof String) {
            user.setPhone((String) phone);
        }
        Object password = all.get("password");
        if (timestamp instanceof String) {
            user.setPassword((String) password);
        }
        Object rememberPwd = all.get("rememberPwd");
        if (rememberPwd instanceof Boolean) {
            user.setRememberPwd((boolean) rememberPwd);
        }
        return user;
    }

    public void release() {
        if (thread != null) {
            thread.quitSafely();
            thread = null;
        }
    }
}