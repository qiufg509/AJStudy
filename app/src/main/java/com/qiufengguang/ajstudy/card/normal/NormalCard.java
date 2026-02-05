package com.qiufengguang.ajstudy.card.normal;

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
import com.qiufengguang.ajstudy.data.NormalCardBean;
import com.qiufengguang.ajstudy.databinding.CardNormalBinding;
import com.qiufengguang.ajstudy.global.Constant;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * 普通卡片
 *
 * @author qiufengguang
 * @since 2026/1/29 19:18
 */
public class NormalCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 6;

    private NormalCardBean bean;

    private WeakReference<CardNormalBinding> bindingRef;

    private OnItemClickListener<NormalCardBean> listener;

    private RequestOptions requestOptions;

    private NormalCard() {
    }

    public void setData(NormalCardBean bean) {
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
        CardNormalBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }

        if (this.requestOptions == null) {
            int radius = binding.getRoot().getResources().getDimensionPixelSize(R.dimen.radius_l);
            this.requestOptions = new RequestOptions()
                .centerCrop()
                .error(R.drawable.placeholder_icon_l)
                .transform(new CenterCrop(), new RoundedCorners(radius));
        }
        if (!TextUtils.isEmpty(bean.getIcon())) {
            Glide.with(binding.ivIcon.getContext())
                .load(bean.getIcon())
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(binding.ivIcon);
        } else {
            binding.ivIcon.setImageResource(R.drawable.placeholder_icon_l);
        }

        String title = bean.getTitle();
        String titleContent = title.replaceAll("\\.(md|txt|json|xml)$", "");
        binding.tvTitle.setText(titleContent);

        binding.tvSubtitle.setText(bean.getSubtitle());

        binding.tvBrief.setText(bean.getBrief());

        // 优化点击事件，使用单个监听器
        binding.getRoot().setOnClickListener(v -> {
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
            CardNormalBinding binding = bindingRef.get();
            if (binding != null) {
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
            CardNormalBinding normalBinding = CardNormalBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new NormalCardHolder(normalBinding);
        }

        @Override
        public Map<Integer, Integer> getSpanSize() {
            return getSpanSizeMap(Constant.Pln.DEF_4, Constant.Pln.DEF_8, Constant.Pln.DEF_12);
        }
    }

    public static class Builder {
        private CardNormalBinding binding;

        private OnItemClickListener<NormalCardBean> listener;

        /**
         * 设置系列卡片布局viewbinding
         *
         * @param binding CardNormalBinding
         * @ Builder
         */
        public NormalCard.Builder setBinding(CardNormalBinding binding) {
            this.binding = binding;
            return this;
        }

        /**
         * 设置点击监听
         *
         * @param listener {@link OnItemClickListener}
         * @return Builder
         */
        public NormalCard.Builder setListener(OnItemClickListener<NormalCardBean> listener) {
            this.listener = listener;
            return this;
        }

        public NormalCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            NormalCard wrapper = new NormalCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            wrapper.listener = this.listener;
            return wrapper;
        }
    }
}

