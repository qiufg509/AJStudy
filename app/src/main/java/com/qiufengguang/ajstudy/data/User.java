package com.qiufengguang.ajstudy.data;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;
import com.qiufengguang.ajstudy.global.Constant;

/**
 * 用户bean
 *
 * @author qiufengguang
 * @since 2025/11/30 2:15
 */
public class User extends BaseCardBean {

    public static final String LAYOUT_NAME = "userCard";

    public static final int LAYOUT_ID = 5;

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

    @Override
    public String getLayoutName() {
        return LAYOUT_NAME;
    }

    @Override
    public int getLayoutId() {
        return LAYOUT_ID;
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

    /**
     * 保留前3位和后4位，中间用*代替
     * 例如：13800138000 → 138****8000
     */
    public String anonymizePhone() {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}
