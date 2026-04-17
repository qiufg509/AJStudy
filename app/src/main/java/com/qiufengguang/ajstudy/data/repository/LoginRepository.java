package com.qiufengguang.ajstudy.data.repository;

import android.text.TextUtils;

import com.qiufengguang.ajstudy.data.callback.LoginCallback;
import com.qiufengguang.ajstudy.data.model.User;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.utils.AppExecutors;
import com.qiufengguang.ajstudy.utils.SpUtils;

import java.util.Map;

/**
 * 登录操作
 * [性能专家重构]：移除私有线程池，改用全局 AppExecutors，修复 SpUtils 兼容性问题。
 *
 * @author qiufengguang
 * @since 2025/11/30 2:15
 */
public class LoginRepository {

    public void login(String phone, String password, LoginCallback callback) {
        // [性能重构]：利用全局动态线程池执行模拟耗时任务，避免私造 HandlerThread
        AppExecutors.getInstance().execute(() -> {
            try {
                Thread.sleep(1500); // 模拟网络延迟

                // 模拟登录逻辑
                if ("123456".equals(password)) {
                    User user = new User(phone, password);
                    // 模拟返回成功
                    AppExecutors.getInstance().mainThread().execute(() -> callback.onSuccess(user));
                } else {
                    AppExecutors.getInstance().mainThread().execute(() -> callback.onError("密码错误"));
                }
            } catch (InterruptedException e) {
                AppExecutors.getInstance().mainThread().execute(() -> callback.onError("网络连接失败"));
            }
        }, null);
    }

    public void saveUserInfo(User user) {
        // [性能专家修复]：修复 SpUtils API 变更导致的编译错误，直接使用原生的链式 apply()
        String phone = user.getPhone();
        String password = user.getPassword();
        boolean rememberPwd = user.isRememberPwd();
        long timestamp = user.getTimestamp();

        SpUtils.getInstance().getSp(Constant.Sp.PREF_USER).edit()
            .putString("phone", TextUtils.isEmpty(phone) ? "" : phone)
            .putString("password", TextUtils.isEmpty(password) ? "" : password)
            .putBoolean("rememberPwd", rememberPwd)
            .putLong("timestamp", timestamp)
            .apply();
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
        // [性能重构]：已移除私有线程，此处不再需要手动关闭
    }
}
