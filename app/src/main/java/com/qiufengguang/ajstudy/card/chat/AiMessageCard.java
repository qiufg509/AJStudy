package com.qiufengguang.ajstudy.card.chat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.data.model.ChatMessage;
import com.qiufengguang.ajstudy.databinding.CardAiMessageBinding;

import java.lang.ref.WeakReference;

/**
 * AI对话页-AI助手消息卡片
 *
 * @author qiufengguang
 * @since 2026/3/30 17:41
 */
public class AiMessageCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 26;

    private ChatMessage bean;

    private WeakReference<CardAiMessageBinding> bindingRef;

    private AiMessageCard() {
    }

    public void setData(ChatMessage bean) {
        this.bean = bean;
        this.show();
    }

    public void show() {
        if (this.bean == null) {
            return;
        }
        if (bindingRef == null) {
            return;
        }
        CardAiMessageBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }
        binding.tvContent.setText(bean.getContent());
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        if (bindingRef != null) {
            bindingRef.clear();
            bindingRef = null;
        }
        bean = null;
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardAiMessageBinding binding = CardAiMessageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new AiMessageCardHolder(binding);
        }
    }

    public static class Builder {
        private CardAiMessageBinding binding;

        /**
         * 设置卡片布局viewbinding
         *
         * @param binding CardAiMessageBinding
         * @ Builder
         */
        public AiMessageCard.Builder setBinding(CardAiMessageBinding binding) {
            this.binding = binding;
            return this;
        }

        public AiMessageCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            AiMessageCard wrapper = new AiMessageCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            return wrapper;
        }
    }
}

