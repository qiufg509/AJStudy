package com.qiufengguang.ajstudy.card.topicmulti;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.TopicMultiCardBean;
import com.qiufengguang.ajstudy.databinding.CardTopicMultiBinding;

/**
 * 多主题聚合卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/2/7 0:42
 */
public class TopicMultiCardCardHolder extends BaseViewHolder<CardTopicMultiBinding> {

    private TopicMultiCard card;

    public TopicMultiCardCardHolder(@NonNull CardTopicMultiBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new TopicMultiCard.Builder()
            .setBinding(binding)
            .setListener(this::onCommonClickListener)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != TopicMultiCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        TopicMultiCardBean bean = (TopicMultiCardBean) data.getData();
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