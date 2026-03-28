package com.qiufengguang.ajstudy.card.welcome;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.base.BaseCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardAiWelcomeBinding;

/**
 * Ai欢迎卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/3/29 1:33
 */
public class AiWelcomeCardHolder extends BaseViewHolder<CardAiWelcomeBinding> {

    private AiWelcomeCard card;

    public AiWelcomeCardHolder(@NonNull CardAiWelcomeBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new AiWelcomeCard.Builder()
            .setBinding(binding)
            .create();
        card.show();
    }

    @Override
    public boolean bind(LayoutData<?> data, OnItemClickListener<BaseCardBean> listener) {
        if (data == null || data.isCollection()
            || data.getLayoutId() != AiWelcomeCard.LAYOUT_ID) {
            return false;
        }
        if (card == null) {
            initCard();
        }
        card.setListener(listener);
        return true;
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