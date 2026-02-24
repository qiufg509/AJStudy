package com.qiufengguang.ajstudy.card.empty;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardEmptyBinding;

/**
 * 空卡的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/2/24 18:53
 */
public class EmptyCardHolder extends BaseViewHolder<CardEmptyBinding> {

    private EmptyCard card;

    public EmptyCardHolder(@NonNull CardEmptyBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new EmptyCard.Builder().create();
    }

    @Override
    public void bind(LayoutData<?> data) {
    }


    @Override
    public void cleanup() {
        if (card != null) {
            card = null;
        }
        super.cleanup();
    }
}