package com.qiufengguang.ajstudy.data.model;

public class ChatMessage {
    private String role; // "user" 或 "assistant"
    private String content;

    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }
}
