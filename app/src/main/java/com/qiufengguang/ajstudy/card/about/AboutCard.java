package com.qiufengguang.ajstudy.card.about;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.data.model.AboutCardBean;
import com.qiufengguang.ajstudy.databinding.CardAboutBinding;

import java.lang.ref.WeakReference;

/**
 * 详情页关于卡片
 *
 * @author qiufengguang
 * @since 2026/3/1 16:12
 */
public class AboutCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 16;

    private String cardTitle;

    private AboutCardBean bean;

    private WeakReference<CardAboutBinding> bindingRef;

    private AboutCard() {
    }

    public void setData(AboutCardBean bean, String cardTitle) {
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
        CardAboutBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }
        binding.tvTitle.setText(cardTitle);
        binding.tvTariff.setText(bean.getTariffDesc());
        binding.tvVersion.setText(bean.getVersion());
        binding.tvContent.setText(bean.getAppIntro());
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
            CardAboutBinding normalBinding = CardAboutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new AboutCardHolder(normalBinding);
        }
    }

    public static class Builder {
        private CardAboutBinding binding;

        /**
         * 设置卡片布局viewbinding
         *
         * @param binding CardAboutBinding
         * @ Builder
         */
        public AboutCard.Builder setBinding(CardAboutBinding binding) {
            this.binding = binding;
            return this;
        }

        public AboutCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            AboutCard wrapper = new AboutCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            return wrapper;
        }
    }
}

