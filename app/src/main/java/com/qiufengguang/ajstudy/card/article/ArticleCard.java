package com.qiufengguang.ajstudy.card.article;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.base.BaseCardBean;
import com.qiufengguang.ajstudy.data.model.ArticleCardBean;
import com.qiufengguang.ajstudy.databinding.CardArticleBinding;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.utils.TimeFormatter;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * 文章卡片
 *
 * @author qiufengguang
 * @since 2026/2/7 0:43
 */
public class ArticleCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 10;

    private ArticleCardBean bean;

    private WeakReference<CardArticleBinding> bindingRef;

    private OnItemClickListener<BaseCardBean> listener;

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

        loadImage(binding.ivPublisherLogo, R.drawable.ic_avatar, bean.getAvatar());
        binding.tvPublisherName.setText(bean.getAuthor());
        binding.tvPublishTime.setText(TimeFormatter.formatTimeAgo(bean.getPublishTime()));

        List<ArticleCardBean.Article> articles = bean.getArticles();
        if (articles == null || articles.isEmpty()) {
            return;
        }
        ArticleCardBean.Article article0 = articles.get(0);
        loadImage(binding.ivItemThumb0, R.drawable.placeholder_image_16_9, article0.getImageUrl());
        binding.tvItemTitle0.setText(article0.getTitle());

        if (articles.size() > 1) {
            ArticleCardBean.Article article1 = articles.get(1);
            loadImage(binding.ivItemThumb1, R.drawable.placeholder_image_1_1, article1.getImageUrl());
            binding.tvItemTitle1.setText(article1.getTitle());
        }

        if (articles.size() > 2) {
            ArticleCardBean.Article article2 = articles.get(2);
            loadImage(binding.ivItemThumb2, R.drawable.placeholder_image_1_1, article2.getImageUrl());
            binding.tvItemTitle2.setText(article2.getTitle());
        }

        binding.layoutUser.setOnClickListener(v -> {
            if (listener != null && bean != null) {
                listener.onItemClick(v.getContext(), bean);
            }
        });
        binding.layoutThumb0.setOnClickListener(v -> {
            if (listener != null && !articles.isEmpty()) {
                listener.onItemClick(v.getContext(), articles.get(0));
            }
        });
        binding.layoutThumb1.setOnClickListener(v -> {
            if (listener != null && articles.size() >= 2) {
                listener.onItemClick(v.getContext(), articles.get(1));
            }
        });
        binding.layoutThumb2.setOnClickListener(v -> {
            if (listener != null && articles.size() >= 3) {
                listener.onItemClick(v.getContext(), articles.get(2));
            }
        });
    }

    private void loadImage(ImageView view, @DrawableRes() int placeholderId, String imageUrl) {
        if (!TextUtils.isEmpty(bean.getAuthor())) {
            Glide.with(view.getContext())
                .load(imageUrl)
                .placeholder(placeholderId)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(view);
        } else {
            view.setImageResource(placeholderId);
        }
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        if (bindingRef != null) {
            CardArticleBinding binding = bindingRef.get();
            if (binding != null) {
                binding.getRoot().setOnClickListener(null);
                bindingRef.clear();
            }
            bindingRef = null;
        }
        bean = null;
        listener = null;
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardArticleBinding binding = CardArticleBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new ArticleCardHolder(binding);
        }

        @Override
        public Map<Integer, Integer> getSpanSize() {
            return getSpanSizeMap(Constant.Pln.DEF_4, Constant.Pln.DEF_8, Constant.Pln.DEF_12);
        }
    }

    public static class Builder {
        private CardArticleBinding binding;

        private OnItemClickListener<BaseCardBean> listener;

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

        /**
         * 设置点击监听
         *
         * @param listener {@link OnItemClickListener}
         * @return Builder
         */
        public ArticleCard.Builder setListener(OnItemClickListener<BaseCardBean> listener) {
            this.listener = listener;
            return this;
        }

        public ArticleCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            ArticleCard wrapper = new ArticleCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            wrapper.listener = this.listener;
            return wrapper;
        }
    }
}

