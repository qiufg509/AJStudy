package com.qiufengguang.ajstudy.card.luckywheel;

import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.data.LuckyWheelCardBean;
import com.qiufengguang.ajstudy.databinding.CardLuckyWheelBinding;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.view.LuckyWheel;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * 幸运转盘卡片
 *
 * @author qiufengguang
 * @since 2026/2/1 21:49
 */
public class LuckyWheelCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 3;

    private String cardTitle;

    private List<LuckyWheelCardBean> beans;

    private WeakReference<CardLuckyWheelBinding> bindingRef;

    private LuckyWheel.AnimationEndListener listener;

    private LuckyWheelCard() {
    }

    public void setData(List<LuckyWheelCardBean> beans, String cardTitle) {
        this.cardTitle = cardTitle;
        this.beans = beans;
        this.show();
    }

    public void show() {
        if (this.beans == null) {
            return;
        }
        if (bindingRef == null) {
            return;
        }
        CardLuckyWheelBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }
        binding.tvTitle.setText(cardTitle);
        binding.luckyWheel.setBeans(this.beans);
        binding.luckyWheel.setAnimationEndListener(listener);
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
        if (this.beans != null && !this.beans.isEmpty()) {
            for (LuckyWheelCardBean bean : this.beans) {
                if (bean == null) {
                    continue;
                }
                Bitmap bitmap = bean.getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bean.setBitmap(null);
                }
            }
            beans = null;
        }
        listener = null;
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardLuckyWheelBinding binding = CardLuckyWheelBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new LuckyWheelHolder(binding);
        }

        @Override
        public Map<Integer, Integer> getSpanSize() {
            return getSpanSizeMap(Constant.Pln.DEF_4, Constant.Pln.DEF_8, Constant.Pln.DEF_12);
        }
    }

    public static class Builder {
        private CardLuckyWheelBinding binding;

        private LuckyWheel.AnimationEndListener listener;

        /**
         * 卡片布局viewbinding
         *
         * @param binding CardLuckyWheelBinding
         * @ Builder
         */
        public LuckyWheelCard.Builder setBinding(CardLuckyWheelBinding binding) {
            this.binding = binding;
            return this;
        }

        /**
         * 转动结束监听
         *
         * @param listener {@link com.qiufengguang.ajstudy.view.LuckyWheel.AnimationEndListener}
         * @return Builder
         */
        public LuckyWheelCard.Builder setListener(LuckyWheel.AnimationEndListener listener) {
            this.listener = listener;
            return this;
        }

        public LuckyWheelCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            LuckyWheelCard wrapper = new LuckyWheelCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            wrapper.listener = this.listener;
            return wrapper;
        }
    }
}

