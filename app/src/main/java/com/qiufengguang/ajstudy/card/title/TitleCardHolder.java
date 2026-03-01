package com.qiufengguang.ajstudy.card.title;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardTitleBinding;

/**
 * 标题卡的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/2/25 21:04
 */
public class TitleCardHolder extends BaseViewHolder<CardTitleBinding> {

    private TitleCard card;

    public TitleCardHolder(@NonNull CardTitleBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new TitleCard.Builder()
            .setTvTitle(binding.tvTitle)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
    }

    @Override
    public void bind(LayoutData<?> data, LifecycleOwner lifecycleOwner) {
        if (data == null || data.isCollection() || data.getLayoutId() != TitleCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        card.setData(data.getName());
    }

    @Override
    public void cleanup() {
        if (card != null) {
            card.release();
            card = null;
        }
        super.cleanup();
    }
}