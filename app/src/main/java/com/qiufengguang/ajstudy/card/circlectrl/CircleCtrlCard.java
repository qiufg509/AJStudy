package com.qiufengguang.ajstudy.card.circlectrl;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.databinding.CardCircleCtrlBinding;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.view.CircleCtrl;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * 圆形控制器卡片
 *
 * @author qiufengguang
 * @since 2026/2/1 21:49
 */
public class CircleCtrlCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 27;

    private String cardTitle;

    private WeakReference<CardCircleCtrlBinding> bindingRef;

    private CircleCtrl.OnDirectionPadListener listener;

    private CircleCtrlCard() {
    }

    public void setData(String cardTitle) {
        this.cardTitle = cardTitle;
        this.show();
    }

    public void show() {
        if (bindingRef == null) {
            return;
        }
        CardCircleCtrlBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }
        binding.tvTitle.setText(cardTitle);
        binding.circleCtrl.setListener(listener);
        // 确保 UI 刷新
        binding.circleCtrl.invalidate();
    }


    /**
     * 释放资源
     * 页面onDestroyView或ViewHolder回收时调用
     */
    public void release() {
        if (bindingRef != null) {
            CardCircleCtrlBinding binding = bindingRef.get();
            if (binding != null) {
                // 显式释放 View 内部持有的缓存位图
                binding.circleCtrl.release();
            }
            bindingRef.clear();
            bindingRef = null;
        }
        listener = null;
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardCircleCtrlBinding binding = CardCircleCtrlBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new CircleCtrlHolder(binding);
        }

        @Override
        public Map<Integer, Integer> getSpanSizeInGridPage() {
            return getSpanSizeMap(Constant.Pln.DEF_4, Constant.Pln.DEF_8, Constant.Pln.DEF_12);
        }
    }

    public static class Builder {
        private CardCircleCtrlBinding binding;

        private CircleCtrl.OnDirectionPadListener listener;

        /**
         * 卡片布局viewbinding
         *
         * @param binding CardCircleCtrlBinding
         * @ Builder
         */
        public Builder setBinding(CardCircleCtrlBinding binding) {
            this.binding = binding;
            return this;
        }

        /**
         * 监听
         *
         * @param listener {@link CircleCtrl.OnDirectionPadListener}
         * @return Builder
         */
        public CircleCtrlCard.Builder setListener(CircleCtrl.OnDirectionPadListener listener) {
            this.listener = listener;
            return this;
        }

        public CircleCtrlCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            CircleCtrlCard wrapper = new CircleCtrlCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            wrapper.listener = this.listener;
            return wrapper;
        }
    }
}
