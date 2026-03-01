package com.qiufengguang.ajstudy.card.recommend;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.RecommendCardBean;
import com.qiufengguang.ajstudy.databinding.CardRecommendBinding;

/**
 * 推荐卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/3/1 17:09
 */
public class RecommendCardHolder extends BaseViewHolder<CardRecommendBinding> {

    private RecommendCard card;

    public RecommendCardHolder(@NonNull CardRecommendBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new RecommendCard.Builder()
            .setBinding(binding)
            .setListener(this::onCommonClickListener)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != RecommendCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        RecommendCardBean bean = (RecommendCardBean) data.getData();
        card.setData(bean);
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