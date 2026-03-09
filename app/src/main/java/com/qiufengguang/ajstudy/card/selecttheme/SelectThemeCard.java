package com.qiufengguang.ajstudy.card.selecttheme;

import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.data.model.SelectThemeCardBean;
import com.qiufengguang.ajstudy.databinding.CardSelectThemeBinding;
import com.qiufengguang.ajstudy.router.AppNavigator;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.utils.ThemeUtils;

import java.lang.ref.WeakReference;

/**
 * 选择主题卡片
 *
 * @author qiufengguang
 * @since 2026/3/9 23:41
 */
public class SelectThemeCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 22;

    private SelectThemeCardBean bean;

    private WeakReference<CardSelectThemeBinding> bindingRef;

    private ColorStateList tintList;

    private SelectThemeCard() {
    }

    public void setData(SelectThemeCardBean bean) {
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
        CardSelectThemeBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }

        binding.tvTitle.setText(bean.getTitle());
        binding.tvColor.setBackgroundResource(bean.getColor());
        if (tintList == null) {
            tintList = ColorStateList.valueOf(ContextCompat.getColor(
                binding.getRoot().getContext(), bean.getColor()));
        }
        ImageViewCompat.setImageTintList(binding.tvIndicator, tintList);
        binding.tvIndicator.setVisibility(bean.isSelected() ? View.VISIBLE : View.GONE);
        binding.getRoot().setOnClickListener(v -> {
            bean.setSelected(true);
            binding.tvIndicator.setVisibility(View.VISIBLE);
            ThemeUtils.setSelectedThemeIndex(bean.getThemeIndex());
            AppNavigator.getInstance().restart(v.getContext(), Router.EXTRA_RESTART);
            ((AppCompatActivity) v.getContext()).finish();
        });
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
            CardSelectThemeBinding binding = CardSelectThemeBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new SelectThemeCardHolder(binding);
        }
    }

    public static class Builder {
        private CardSelectThemeBinding binding;

        /**
         * 设置卡片布局viewbinding
         *
         * @param binding CardSelectThemeBinding
         * @ Builder
         */
        public SelectThemeCard.Builder setBinding(CardSelectThemeBinding binding) {
            this.binding = binding;
            return this;
        }

        public SelectThemeCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            SelectThemeCard wrapper = new SelectThemeCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            return wrapper;
        }
    }
}

