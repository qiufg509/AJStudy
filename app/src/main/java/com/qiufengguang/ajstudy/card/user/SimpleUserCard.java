package com.qiufengguang.ajstudy.card.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.model.User;
import com.qiufengguang.ajstudy.databinding.CardSimpleUserBinding;

import java.lang.ref.WeakReference;

/**
 * 简单用户卡片
 *
 * @author qiufengguang
 * @since 2026/1/31 1:24
 */
public class SimpleUserCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 8;

    private User user;

    private WeakReference<CardSimpleUserBinding> bindingRef;

    private OnItemClickListener<User> listener;

    private SimpleUserCard() {
    }

    public void setData(User user) {
        this.user = user;
        this.show();
    }

    public void show() {
        if (this.user == null) {
            return;
        }
        if (bindingRef == null) {
            return;
        }
        CardSimpleUserBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }
        if (user.isInvalid()) {
            binding.tvUserName.setText("未登录");
            binding.tvUserAccount.setText("");
            binding.tvUserAccount.setVisibility(View.GONE);
        } else {
            binding.tvUserName.setText("假的用户名");
            binding.tvUserAccount.setText(user.anonymizePhone());
            binding.tvUserAccount.setVisibility(View.VISIBLE);
        }

        binding.getRoot().setOnClickListener(v -> {
            if (listener != null && user != null) {
                listener.onItemClick(v.getContext(), user);
            }
        });
    }


    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        if (bindingRef != null) {
            CardSimpleUserBinding binding = bindingRef.get();
            if (binding != null) {
                binding.getRoot().setOnClickListener(null);
                bindingRef.clear();
            }
            bindingRef = null;
        }
        user = null;
        listener = null;
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardSimpleUserBinding binding = CardSimpleUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new SimpleUserCardHolder(binding);
        }
    }

    public static class Builder {
        private CardSimpleUserBinding binding;

        private OnItemClickListener<User> listener;

        /**
         * 设置卡片布局viewbinding
         *
         * @param binding CardSimpleUserBinding
         * @ Builder
         */
        public SimpleUserCard.Builder setBinding(CardSimpleUserBinding binding) {
            this.binding = binding;
            return this;
        }

        /**
         * 设置点击监听
         *
         * @param listener {@link OnItemClickListener}
         * @return Builder
         */
        public SimpleUserCard.Builder setListener(OnItemClickListener<User> listener) {
            this.listener = listener;
            return this;
        }

        public SimpleUserCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            SimpleUserCard wrapper = new SimpleUserCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            wrapper.listener = this.listener;
            return wrapper;
        }
    }
}

