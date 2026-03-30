package com.qiufengguang.ajstudy.card.chat;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.data.model.ChatMessage;
import com.qiufengguang.ajstudy.databinding.CardUserMessageBinding;

import java.lang.ref.WeakReference;

/**
 * AI对话页-用户消息卡片
 *
 * @author qiufengguang
 * @since 2026/3/30 17:41
 */
public class UserMessageCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 25;

    private ChatMessage bean;

    private WeakReference<CardUserMessageBinding> bindingRef;

    private UserMessageCard() {
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
        CardUserMessageBinding binding = bindingRef.get();
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
            CardUserMessageBinding binding = CardUserMessageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new UserMessageCardHolder(binding);
        }
    }

    public static class Builder {
        private CardUserMessageBinding binding;

        /**
         * 设置卡片布局viewbinding
         *
         * @param binding CardUserMessageBinding
         * @ Builder
         */
        public UserMessageCard.Builder setBinding(CardUserMessageBinding binding) {
            this.binding = binding;
            return this;
        }

        public UserMessageCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            UserMessageCard wrapper = new UserMessageCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            return wrapper;
        }
    }
}

