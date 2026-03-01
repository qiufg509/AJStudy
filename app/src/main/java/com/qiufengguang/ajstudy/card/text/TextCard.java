package com.qiufengguang.ajstudy.card.text;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.data.model.TextCardBean;
import com.qiufengguang.ajstudy.databinding.CardTextBinding;

import java.lang.ref.WeakReference;

/**
 * 纯文字卡片
 *
 * @author qiufengguang
 * @since 2026/3/1 16:35
 */
public class TextCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 17;

    private String cardTitle;

    private TextCardBean bean;

    private WeakReference<CardTextBinding> bindingRef;

    private TextCard() {
    }

    public void setData(TextCardBean bean, String cardTitle) {
        this.cardTitle = cardTitle;
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
        CardTextBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }
        binding.tvTitle.setText(cardTitle);
        binding.tvContent.setText(bean.getText());
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
            CardTextBinding binding = CardTextBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new TextCardHolder(binding);
        }
    }

    public static class Builder {
        private CardTextBinding binding;

        /**
         * 设置卡片布局viewbinding
         *
         * @param binding CardTextBinding
         * @ Builder
         */
        public TextCard.Builder setBinding(CardTextBinding binding) {
            this.binding = binding;
            return this;
        }

        public TextCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            TextCard wrapper = new TextCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            return wrapper;
        }
    }
}

