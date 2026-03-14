package com.qiufengguang.ajstudy.card.state;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.airbnb.lottie.LottieDrawable;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.data.model.StateCardBean;
import com.qiufengguang.ajstudy.databinding.CardStateBinding;

import java.lang.ref.WeakReference;

/**
 * 页面状态卡片
 *
 * @author qiufengguang
 * @since 2026/3/13 18:43
 */
public class StateCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = -1;

    private StateCardBean bean;

    private WeakReference<CardStateBinding> bindingRef;

    private OnItemClickListener<StateCardBean> listener;

    private StateCard() {
    }

    public void setData(StateCardBean bean) {
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
        CardStateBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }


        int rawRes = getAnimationRes(bean.getState());
        binding.animationView.setAnimation(rawRes);
        binding.animationView.setRepeatCount(LottieDrawable.INFINITE);
        binding.animationView.playAnimation();

        String message = getDefaultMessage(bean.getState());
        if (TextUtils.isEmpty(message)) {
            binding.tvMessage.setVisibility(View.GONE);
        } else {
            binding.tvMessage.setText(message);
            binding.tvMessage.setVisibility(View.VISIBLE);
        }

        if (bean.getState() == State.NO_NETWORK) {
            binding.getRoot().setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(v.getContext(), bean);
                }
            });
        } else {
            binding.getRoot().setOnClickListener(null);
        }
    }

    private int getAnimationRes(State type) {
        switch (type) {
            case LOADING:
                return R.raw.wave_loading;
            case NO_NETWORK:
                return R.raw.no_internet_connection;
            case ERROR:
                return R.raw.girl_relaxing_while_error_404;
            default:
                return R.raw.empty_box;
        }
    }

    private String getDefaultMessage(State type) {
        switch (type) {
            case NO_NETWORK:
                return "网络未连接，请轻触重试";
            case EMPTY:
                return "暂无数据";
            case ERROR:
                return "出错了，请轻触重试";
            case LOADING:
            default:
                return "";
        }
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        if (bindingRef != null) {
            CardStateBinding binding = bindingRef.get();
            if (binding != null) {
                binding.animationView.cancelAnimation();
                binding.getRoot().setOnClickListener(null);
                bindingRef.clear();
            }
            bindingRef = null;
        }
        bean = null;
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardStateBinding binding = CardStateBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new StateCardHolder(binding);
        }

        @Override
        public boolean isFullSpanInStaggeredPage() {
            return true;
        }
    }

    public static class Builder {
        private CardStateBinding binding;

        private OnItemClickListener<StateCardBean> listener;

        /**
         * 设置卡片布局viewbinding
         *
         * @param binding CardStateBinding
         * @ Builder
         */
        public StateCard.Builder setBinding(CardStateBinding binding) {
            this.binding = binding;
            return this;
        }

        /**
         * 设置卡片item点击事件
         *
         * @param listener {@link OnItemClickListener}
         * @return Builder
         */
        public StateCard.Builder setListener(
            OnItemClickListener<StateCardBean> listener) {
            this.listener = listener;
            return this;
        }

        public StateCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            StateCard wrapper = new StateCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            wrapper.listener = this.listener;
            return wrapper;
        }
    }
}

