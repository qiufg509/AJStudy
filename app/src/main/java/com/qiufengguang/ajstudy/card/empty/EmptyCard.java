package com.qiufengguang.ajstudy.card.empty;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.databinding.CardEmpty1Binding;
import com.qiufengguang.ajstudy.databinding.CardEmpty2Binding;

/**
 * 空卡，无任何内容，无宽高
 *
 * @author qiufengguang
 * @since 2026/2/24 18:12
 */
public class EmptyCard extends Card {
    /**
     * 卡片（高度自适应）id
     */
    public static final int LAYOUT_ID_1 = -1;

    /**
     * 卡片（高度撑满）id
     */
    public static final int LAYOUT_ID_2 = 0;

    private EmptyCard() {
    }

    public static class WrapCreator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardEmpty1Binding binding = CardEmpty1Binding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new EmptyCardWrapHolder(binding);
        }
    }

    public static class FullCreator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardEmpty2Binding binding = CardEmpty2Binding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new EmptyCardFullHolder(binding);
        }
    }

    public static class Builder {
        public EmptyCard create() {
            return new EmptyCard();
        }
    }
}

