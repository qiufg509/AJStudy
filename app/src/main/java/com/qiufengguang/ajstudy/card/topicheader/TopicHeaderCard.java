package com.qiufengguang.ajstudy.card.topicheader;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.data.model.TopicHeaderCardBean;
import com.qiufengguang.ajstudy.databinding.CardTopicHeaderBinding;
import com.qiufengguang.ajstudy.global.GlobalApp;
import com.qiufengguang.ajstudy.utils.DisplayMetricsHelper;

import java.lang.ref.WeakReference;

/**
 * 专题头卡
 *
 * @author qiufengguang
 * @since 2026/3/7 17:56
 */
public class TopicHeaderCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 20;

    private TopicHeaderCardBean bean;

    private final int iconSize;

    private WeakReference<CardTopicHeaderBinding> bindingRef;

    private TopicHeaderCard() {
        iconSize = DisplayMetricsHelper.dp2px(GlobalApp.getContext(), 136);
    }

    public void setData(TopicHeaderCardBean bean) {
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
        CardTopicHeaderBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }
        binding.tvTitle.setText(bean.getTitle());
        binding.tvSubtitle.setText(bean.getSubtitle());
        String description = bean.getBrief();
        if (TextUtils.isEmpty(description)) {
            binding.tvBrief.setVisibility(View.GONE);
        } else {
            binding.tvBrief.setText(description);
            binding.tvBrief.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(bean.getIconUrl())) {
            Glide.with(binding.ivImage.getContext())
                .load(bean.getIconUrl())
                .override(iconSize, iconSize)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(binding.ivImage);
        } else {
            binding.ivImage.setImageDrawable(null);
        }
        binding.ivImage.setBackgroundColor(bean.getBackground());
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
            CardTopicHeaderBinding binding = CardTopicHeaderBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            TopicHeaderCardHolder holder = new TopicHeaderCardHolder(binding);
            setFullSpanInStaggeredPage(holder);
            return holder;
        }

        @Override
        public boolean isFitToMargin() {
            return true;
        }
    }

    public static class Builder {
        private CardTopicHeaderBinding binding;

        /**
         * 设置卡片布局viewbinding
         *
         * @param binding CardTopicHeaderBinding
         * @ Builder
         */
        public TopicHeaderCard.Builder setBinding(CardTopicHeaderBinding binding) {
            this.binding = binding;
            return this;
        }

        public TopicHeaderCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            TopicHeaderCard wrapper = new TopicHeaderCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            return wrapper;
        }
    }
}

