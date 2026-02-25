package com.qiufengguang.ajstudy.card.title;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.databinding.CardTitleBinding;

import java.lang.ref.WeakReference;

/**
 * 标题卡
 *
 * @author qiufengguang
 * @since 2026/2/25 21:04
 */
public class TitleCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 12;

    private WeakReference<TextView> titleRef;

    private String title;

    private TitleCard() {
    }

    public void setData(String title) {
        this.title = title;
        show();
    }

    public void show() {
        if (TextUtils.isEmpty(title)) {
            return;
        }
        if (titleRef != null) {
            TextView view = titleRef.get();
            if (view != null) {
                view.setText(title);
            }
        }
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardTitleBinding binding = CardTitleBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new TitleCardHolder(binding);
        }
    }

    public static class Builder {
        private TextView tvTitle;

        /**
         * 设置卡片标题控件
         *
         * @param tvTitle TextView
         * @return Builder
         */
        public TitleCard.Builder setTvTitle(TextView tvTitle) {
            this.tvTitle = tvTitle;
            return this;
        }

        public TitleCard create() {
            if (this.tvTitle == null) {
                throw new UnsupportedOperationException(
                    "tvTitle is null, call setTvTitle first.");
            }
            TitleCard wrapper = new TitleCard();
            wrapper.titleRef = new WeakReference<>(this.tvTitle);
            return wrapper;
        }
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        if (titleRef != null) {
            titleRef.clear();
            titleRef = null;
        }
    }
}
