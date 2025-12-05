package com.qiufengguang.ajstudy.data;

import com.qiufengguang.ajstudy.global.Constant;

/**
 * 用户bean
 *
 * @author qiufengguang
 * @since 2025/11/30 2:15
 */
public class User {
    private String phone;
    private String password;
    private boolean rememberPwd;

    private long timestamp;

    public User() {
    }

    public User(String phone, String password) {
        this.phone = phone;
        this.password = password;
        this.timestamp = System.currentTimeMillis();
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isRememberPwd() {
        return rememberPwd;
    }

    public void setRememberPwd(boolean rememberPwd) {
        this.rememberPwd = rememberPwd;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isInvalid() {
        boolean isInvalid = System.currentTimeMillis() > this.timestamp + Constant.TOKEN_EXPIRY_TIME;
        if (isInvalid) {
            // 重置-1，避免将系统时间改回去绕过登录
            this.timestamp = -1;
        }
        return isInvalid;
    }
}
