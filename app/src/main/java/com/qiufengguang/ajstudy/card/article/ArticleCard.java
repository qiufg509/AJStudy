package com.qiufengguang.ajstudy.card.article;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.data.model.ArticleCardBean;
import com.qiufengguang.ajstudy.databinding.CardArticleBinding;

import java.lang.ref.WeakReference;

/**
 * 文章卡片
 *
 * @author qiufengguang
 * @since 2026/3/11 17:02
 */
public class ArticleCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 23;

    private ArticleCardBean bean;

    private WeakReference<CardArticleBinding> bindingRef;

    private ArticleCard() {
    }

    public void setData(ArticleCardBean bean) {
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
        CardArticleBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }
        binding.tvTitle.setText(bean.getTitle());
        binding.tvContent.setText(bean.getContent());
        binding.tvSource.setText(bean.getAuthor());
        if (!TextUtils.isEmpty(bean.getAuthor())) {
            Glide.with(binding.getRoot().getContext())
                .load(bean.getImageUrl())
                .placeholder(R.drawable.placeholder_image_9_16)
                .into(binding.ivCover);
        } else {
            binding.ivCover.setImageResource(R.drawable.placeholder_image_9_16);
        }
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
            CardArticleBinding binding = CardArticleBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new ArticleCardHolder(binding);
        }
    }

    public static class Builder {
        private CardArticleBinding binding;

        /**
         * 设置卡片布局viewbinding
         *
         * @param binding CardArticleBinding
         * @ Builder
         */
        public ArticleCard.Builder setBinding(CardArticleBinding binding) {
            this.binding = binding;
            return this;
        }

        public ArticleCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            ArticleCard wrapper = new ArticleCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            return wrapper;
        }
    }
}

