package com.qiufengguang.ajstudy.data.remote.dto;

import com.qiufengguang.ajstudy.data.model.ChatMessage;

import java.util.List;

/**
 * DeepSeek 接口返回数据
 *
 * @author qiufengguang
 * @since 2026/3/30 18:11
 */
public class DsRespData {
    private List<Choices> choices;

    public List<Choices> getChoices() {
        return choices;
    }

    public void setChoices(List<Choices> choices) {
        this.choices = choices;
    }

    public static class Choices {
        private ChatMessage message;

        public ChatMessage getMessage() {
            return message;
        }

        public void setMessage(ChatMessage message) {
            this.message = message;
        }
    }
}
