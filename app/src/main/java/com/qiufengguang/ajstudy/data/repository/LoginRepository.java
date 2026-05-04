package com.qiufengguang.ajstudy.data.repository;

import android.text.TextUtils;

import com.qiufengguang.ajstudy.data.model.User;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.utils.SpUtils;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * 登录操作
 * [性能专家重构]：移除私有线程池，改用 RxJava 链式调度。
 *
 * @author qiufengguang
 * @since 2025/11/30 2:15
 */
public class LoginRepository {

    public Single<User> login(String phone, String password) {
        // [性能重构]：利用 RxJava 替代手动线程管理，模拟耗时任务
        return Single.fromCallable(() -> {
                // 模拟登录逻辑
                if ("123456".equals(password)) {
                    return new User(phone, password);
                } else {
                    throw new Exception("密码错误");
                }
            })
            .delay(1500, TimeUnit.MILLISECONDS) // 模拟网络延迟
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
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
        if (timestamp instanceof Long tt) {
            user.setTimestamp(tt);
        }
        Object phone = all.get("phone");
        if (phone instanceof String p) {
            user.setPhone(p);
        }
        Object password = all.get("password");
        if (password instanceof String p) {
            user.setPassword(p);
        }
        Object rememberPwd = all.get("rememberPwd");
        if (rememberPwd instanceof Boolean r) {
            user.setRememberPwd(r);
        }
        return user;
    }

    public void release() {
        // [性能重构]：已移除私有线程，此处不再需要手动关闭
    }
}
