package com.qiufengguang.ajstudy.card.topicheader;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.TopicHeaderCardBean;
import com.qiufengguang.ajstudy.databinding.CardTopicHeaderBinding;

/**
 * 专题头卡的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/3/7 17:57
 */
public class TopicHeaderCardHolder extends BaseViewHolder<CardTopicHeaderBinding> {

    private TopicHeaderCard card;

    public TopicHeaderCardHolder(@NonNull CardTopicHeaderBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new TopicHeaderCard.Builder()
            .setBinding(binding)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != TopicHeaderCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        TopicHeaderCardBean bean = (TopicHeaderCardBean) data.getData();
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