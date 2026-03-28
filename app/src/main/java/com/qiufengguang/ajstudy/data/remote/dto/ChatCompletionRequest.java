package com.qiufengguang.ajstudy.data.remote.dto;

import com.qiufengguang.ajstudy.data.model.ChatMessage;

import java.util.List;

// ChatCompletionRequest.java
public class ChatCompletionRequest {
    private String model = "deepseek-chat";
    private List<ChatMessage> messages;
    private boolean stream = false;

    public ChatCompletionRequest(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public boolean isStream() {
        return stream;
    }

    public void setStream(boolean stream) {
        this.stream = stream;
    }
}