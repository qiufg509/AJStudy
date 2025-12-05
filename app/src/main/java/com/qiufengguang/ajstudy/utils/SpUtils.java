package com.qiufengguang.ajstudy.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.qiufengguang.ajstudy.data.User;
import com.qiufengguang.ajstudy.global.GlobalApp;

/**
 * SharedPreferences工具类
 *
 * @author qiufengguang
 * @since 2025/11/30 2:15
 */
public class SpUtils {
    private static final String PREF_NAME = "wechat_login";
    private static final String KEY_USER_INFO = "user_info";

    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    public SpUtils() {
        Context context = GlobalApp.getContext();
        if (context == null) {
            throw new RuntimeException("The global Context has been recycled.");
        }
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveUserInfo(User user) {
        String userJson = gson.toJson(user);
        sharedPreferences.edit()
            .putString(KEY_USER_INFO, userJson)
            .apply();
    }

    public User getSavedUser() {
        String userJson = sharedPreferences.getString(KEY_USER_INFO, null);
        if (userJson != null) {
            return gson.fromJson(userJson, User.class);
        }
        return null;
    }

    public void clearUserInfo() {
        sharedPreferences.edit()
            .remove(KEY_USER_INFO)
            .apply();
    }
}
