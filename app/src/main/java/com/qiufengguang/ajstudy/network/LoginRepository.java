package com.qiufengguang.ajstudy.network;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

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
        SpUtils.getInstance().commitBatch(Constant.Sp.PREF_USER, (editor, cache, spName) -> {
            // 保存sp同时更新内存缓存
            String phone = user.getPhone();
            if (!TextUtils.isEmpty(phone)) {
                editor.putString("phone", phone);
                cache.put(spName + "phone", phone);
            }
            String password = user.getPassword();
            if (!TextUtils.isEmpty(password)) {
                editor.putString("password", password);
                cache.put(spName + "password", password);
            }
            boolean rememberPwd = user.isRememberPwd();
            editor.putBoolean("rememberPwd", rememberPwd);
            cache.put(spName + "rememberPwd", rememberPwd);
            long timestamp = user.getTimestamp();
            editor.putLong("timestamp", timestamp);
            cache.put(spName + "timestamp", timestamp);
        });
    }

    public User getSavedUser() {
        Map<String, ?> all = SpUtils.getInstance().getAll(Constant.Sp.PREF_USER);
        if (all.isEmpty()) {
            return null;
        }
        User user = new User();
        Object timestamp = all.get("timestamp");
        if (timestamp instanceof Long) {
            user.setTimestamp((Long) timestamp);
        }
        Object phone = all.get("phone");
        if (phone instanceof String) {
            user.setPhone((String) phone);
        }
        Object password = all.get("password");
        if (password instanceof String) {
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