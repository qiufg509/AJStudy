package com.qiufengguang.ajstudy.card.empty;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardEmpty1Binding;

/**
 * 空卡（高度自适应）的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/2/24 18:53
 */
public class EmptyCardWrapHolder extends BaseViewHolder<CardEmpty1Binding> {

    private EmptyCard card;

    public EmptyCardWrapHolder(@NonNull CardEmpty1Binding binding) {
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