package com.qiufengguang.ajstudy.card.recommend;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.model.RecommendCardBean;
import com.qiufengguang.ajstudy.databinding.CardRecommendBinding;
import com.qiufengguang.ajstudy.global.Constant;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * 推荐卡片
 *
 * @author qiufengguang
 * @since 2026/3/1 17:24
 */
public class RecommendCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 18;

    private RecommendCardBean bean;

    private WeakReference<CardRecommendBinding> bindingRef;

    private OnItemClickListener<RecommendCardBean> listener;

    private RequestOptions requestOptions;

    private RecommendCard() {
    }

    public void setData(RecommendCardBean bean) {
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
        CardRecommendBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }

        binding.tvAppName.setText(bean.getName());
        binding.tvDescription.setText(bean.getMemo());
        binding.tvRating.setText(bean.getScore());
        binding.tvRating.setText(bean.getScore());
        binding.rbRating.setRating(bean.getStars());

        if (this.requestOptions == null) {
            int radius = binding.getRoot().getResources().getDimensionPixelSize(R.dimen.radius_m);
            this.requestOptions = new RequestOptions()
                .centerCrop()
                .error(R.drawable.placeholder_icon_m)
                .transform(new CenterCrop(), new RoundedCorners(radius));
        }
        if (!TextUtils.isEmpty(bean.getIcon())) {
            Glide.with(binding.ivAppIcon.getContext())
                .load(bean.getIcon())
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(binding.ivAppIcon);
        } else {
            binding.ivAppIcon.setImageResource(R.drawable.placeholder_icon_m);
        }
        // 设置点击事件
        binding.getRoot().setOnClickListener(v -> {
            if (listener != null && bean != null) {
                listener.onItemClick(v.getContext(), bean);
            }
        });
        binding.btnInstall.setOnClickListener(v -> {
            if (listener != null && bean != null) {
                listener.onItemClick(v.getContext(), bean);
            }
        });
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        if (bindingRef != null) {
            CardRecommendBinding binding = bindingRef.get();
            if (binding != null) {
                binding.btnInstall.setOnClickListener(null);
                binding.getRoot().setOnClickListener(null);
                bindingRef.clear();
            }
            bindingRef = null;
        }
        bean = null;
        requestOptions = null;
        listener = null;
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardRecommendBinding binding = CardRecommendBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new RecommendCardHolder(binding);
        }

        @Override
        public Map<Integer, Integer> getSpanSize() {
            return getSpanSizeMap(Constant.Pln.DEF_4, Constant.Pln.DEF_8, Constant.Pln.DEF_12);
        }
    }

    public static class Builder {
        private CardRecommendBinding binding;

        private OnItemClickListener<RecommendCardBean> listener;

        /**
         * 设置卡片布局viewbinding
         *
         * @param binding CardRecommendBinding
         * @ Builder
         */
        public RecommendCard.Builder setBinding(CardRecommendBinding binding) {
            this.binding = binding;
            return this;
        }

        /**
         * 设置点击监听
         *
         * @param listener {@link OnItemClickListener}
         * @return Builder
         */
        public RecommendCard.Builder setListener(OnItemClickListener<RecommendCardBean> listener) {
            this.listener = listener;
            return this;
        }

        public RecommendCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            RecommendCard wrapper = new RecommendCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            wrapper.listener = this.listener;
            return wrapper;
        }
    }
}

