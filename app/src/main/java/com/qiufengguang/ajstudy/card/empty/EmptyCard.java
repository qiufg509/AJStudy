package com.qiufengguang.ajstudy.card.empty;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.databinding.CardEmptyBinding;

/**
 * 空卡，无任何内容，无宽高
 *
 * @author qiufengguang
 * @since 2026/2/24 18:12
 */
public class EmptyCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 0;

    private EmptyCard() {
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardEmptyBinding binding = CardEmptyBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new EmptyCardHolder(binding);
        }
    }

    public static class Builder {
        public EmptyCard create() {
            return new EmptyCard();
        }
    }
}

