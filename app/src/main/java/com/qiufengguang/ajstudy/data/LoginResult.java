package com.qiufengguang.ajstudy.data;

// LoginResult.java
public class LoginResult {
    public enum Status {
        SUCCESS,
        ERROR,
        LOADING,
        INVALID
    }

    private Status status;
    private String message;
    private User user;

    public LoginResult(Status status, String message, User user) {
        this.status = status;
        this.message = message;
        this.user = user;
    }

    // Getters and Setters
    public Status getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public User getUser() {
        return user;
    }
}