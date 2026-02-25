package com.qiufengguang.ajstudy.card.empty;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardEmpty2Binding;

/**
 * 空卡（高度撑满）的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/2/24 18:53
 */
public class EmptyCardFullHolder extends BaseViewHolder<CardEmpty2Binding> {

    private EmptyCard card;

    public EmptyCardFullHolder(@NonNull CardEmpty2Binding binding) {
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