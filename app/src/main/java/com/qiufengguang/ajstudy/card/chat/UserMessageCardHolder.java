package com.qiufengguang.ajstudy.card.chat;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.ChatMessage;
import com.qiufengguang.ajstudy.databinding.CardAiMessageBinding;
import com.qiufengguang.ajstudy.databinding.CardUserMessageBinding;

/**
 * AI对话页-用户消息卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/3/30 17:42
 */
public class UserMessageCardHolder extends BaseViewHolder<CardUserMessageBinding> {

    private UserMessageCard card;

    public UserMessageCardHolder(@NonNull CardUserMessageBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new UserMessageCard.Builder()
            .setBinding(binding)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != UserMessageCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        ChatMessage bean = (ChatMessage) data.getData();
        card.setData(bean);
    }

    @Override
    public void cleanup() {
        if (card != null) {
            card.release();
            card = null;
        }
        super.cleanup();
    }
}