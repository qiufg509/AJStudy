package com.qiufengguang.ajstudy.card.brief;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.data.model.BriefCardBean;
import com.qiufengguang.ajstudy.databinding.CardBriefBinding;

import java.lang.ref.WeakReference;

/**
 * 详情页一句话卡片
 *
 * @author qiufengguang
 * @since 2026/3/1 15:54
 */
public class BriefCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 15;

    private WeakReference<TextView> briefRef;

    private BriefCardBean bean;

    private BriefCard() {
    }

    public void setData(BriefCardBean bean) {
        this.bean = bean;
        show();
    }

    public void show() {
        if (bean == null) {
            return;
        }
        if (briefRef != null) {
            TextView view = briefRef.get();
            if (view == null) {
                return;
            }
            view.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_quotation_start,
                0,
                R.drawable.ic_quotation_end,
                0
            );
            view.setCompoundDrawablePadding(
                view.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_xs));
            view.setText(bean.getBody());

        }
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardBriefBinding binding = CardBriefBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new BriefCardHolder(binding);
        }
    }

    public static class Builder {
        private TextView tvContent;

        /**
         * 设置卡片控件
         *
         * @param tvContent TextView
         * @return Builder
         */
        public BriefCard.Builder setTvContent(TextView tvContent) {
            this.tvContent = tvContent;
            return this;
        }


        public BriefCard create() {
            if (this.tvContent == null) {
                throw new UnsupportedOperationException(
                    "tvContent is null, call setTvContent first.");
            }
            BriefCard wrapper = new BriefCard();
            if (this.tvContent != null) {
                wrapper.briefRef = new WeakReference<>(this.tvContent);
            }
            return wrapper;
        }
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        if (briefRef != null) {
            briefRef.clear();
            briefRef = null;
        }
    }
}
