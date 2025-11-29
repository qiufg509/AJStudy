package com.qiufengguang.ajstudy.data;

// User.java
public class User {
    private String phone;
    private String password;
    private boolean rememberPwd;

    public User() {
    }

    public User(String phone, String password, boolean rememberPwd) {
        this.phone = phone;
        this.password = password;
        this.rememberPwd = rememberPwd;
    }

    // Getters and Setters
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
}
