package com.qiufengguang.ajstudy.data.model;

/**
 * 登录操作响应结果
 *
 * @author qiufengguang
 * @since 2025/11/30 2:15
 */
public record LoginResult(Status status, String message, User user) {
    public enum Status {
        SUCCESS,
        ERROR,
        LOADING,
        INVALID
    }

}