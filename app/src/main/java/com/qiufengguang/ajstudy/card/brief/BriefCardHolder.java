package com.qiufengguang.ajstudy.card.brief;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.BriefCardBean;
import com.qiufengguang.ajstudy.databinding.CardBriefBinding;

/**
 * 详情页一句话卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/3/1 15:54
 */
public class BriefCardHolder extends BaseViewHolder<CardBriefBinding> {

    private BriefCard card;

    public BriefCardHolder(@NonNull CardBriefBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new BriefCard.Builder()
            .setTvContent(binding.tvContent)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != BriefCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        BriefCardBean bean = (BriefCardBean) data.getData();
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