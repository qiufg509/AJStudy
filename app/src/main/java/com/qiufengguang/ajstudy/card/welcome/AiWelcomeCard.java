package com.qiufengguang.ajstudy.card.welcome;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.airbnb.lottie.LottieDrawable;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.base.BaseCardBean;
import com.qiufengguang.ajstudy.data.model.ChatMessage;
import com.qiufengguang.ajstudy.databinding.CardAiWelcomeBinding;

import java.lang.ref.WeakReference;

/**
 * Ai欢迎卡片
 *
 * @author qiufengguang
 * @since 2026/3/29 1:34
 */
public class AiWelcomeCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 24;

    private WeakReference<CardAiWelcomeBinding> bindingRef;

    private OnItemClickListener<BaseCardBean> listener;

    private AiWelcomeCard() {
    }

    /**
     * 设置卡片item点击事件
     *
     * @param listener {@link OnItemClickListener}
     */
    public void setListener(OnItemClickListener<BaseCardBean> listener) {
        this.listener = listener;
    }

    public void show() {
        if (bindingRef == null) {
            return;
        }
        CardAiWelcomeBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }

        binding.animationView.cancelAnimation();
        binding.animationView.setAnimation(R.raw.ai_logo);
        binding.animationView.setRepeatCount(LottieDrawable.INFINITE);
        binding.animationView.playAnimation();

        View.OnClickListener onClickListener = v -> {
            if (listener != null && v instanceof TextView) {
                ChatMessage message = new ChatMessage(ChatMessage.ROLE_USER, ((TextView) v).getText().toString());
                listener.onItemClick(v.getContext(), message);
            }
        };
        binding.tvMessage1.setOnClickListener(onClickListener);
        binding.tvMessage2.setOnClickListener(onClickListener);
        binding.tvMessage3.setOnClickListener(onClickListener);
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        if (bindingRef != null) {
            CardAiWelcomeBinding binding = bindingRef.get();
            if (binding != null) {
                binding.animationView.cancelAnimation();
                binding.tvMessage1.setOnClickListener(null);
                binding.tvMessage2.setOnClickListener(null);
                binding.tvMessage3.setOnClickListener(null);
                bindingRef.clear();
            }
            bindingRef = null;
        }
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardAiWelcomeBinding binding = CardAiWelcomeBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new AiWelcomeCardHolder(binding);
        }

        @Override
        public boolean isFullSpanInStaggeredPage() {
            return true;
        }
    }

    public static class Builder {
        private CardAiWelcomeBinding binding;

        private OnItemClickListener<BaseCardBean> listener;

        /**
         * 设置卡片布局viewbinding
         *
         * @param binding CardAiWelcomeBinding
         * @ Builder
         */
        public AiWelcomeCard.Builder setBinding(CardAiWelcomeBinding binding) {
            this.binding = binding;
            return this;
        }

        /**
         * 设置卡片item点击事件
         *
         * @param listener {@link OnItemClickListener}
         * @return Builder
         */
        public AiWelcomeCard.Builder setListener(
            OnItemClickListener<BaseCardBean> listener) {
            this.listener = listener;
            return this;
        }

        public AiWelcomeCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            AiWelcomeCard wrapper = new AiWelcomeCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            wrapper.listener = this.listener;
            return wrapper;
        }
    }
}

