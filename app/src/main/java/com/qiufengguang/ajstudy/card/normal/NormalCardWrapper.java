package com.qiufengguang.ajstudy.card.normal;

import android.content.Context;
import android.text.TextUtils;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.NormalCardBean;
import com.qiufengguang.ajstudy.databinding.CardNormalBinding;

import java.lang.ref.WeakReference;

/**
 * 普通卡片
 *
 * @author qiufengguang
 * @since 2026/1/29 19:18
 */
public class NormalCardWrapper {

    private NormalCardBean bean;

    private WeakReference<CardNormalBinding> bindingRef;

    private OnItemClickListener listener;

    private RequestOptions requestOptions;

    private NormalCardWrapper() {
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

    public static class Builder {
        private CardNormalBinding binding;

        private OnItemClickListener listener;

        /**
         * 设置系列卡片布局viewbinding
         *
         * @param binding CardNormalBinding
         * @ Builder
         */
        public NormalCardWrapper.Builder setBinding(CardNormalBinding binding) {
            this.binding = binding;
            return this;
        }

        /**
         * 设置点击监听
         *
         * @param listener {@link OnItemClickListener}
         * @return Builder
         */
        public NormalCardWrapper.Builder setListener(OnItemClickListener listener) {
            this.listener = listener;
            return this;
        }

        public NormalCardWrapper create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            NormalCardWrapper wrapper = new NormalCardWrapper();
            wrapper.bindingRef = new WeakReference<>(binding);
            wrapper.listener = this.listener;
            return wrapper;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Context context, NormalCardBean bean);
    }
}

