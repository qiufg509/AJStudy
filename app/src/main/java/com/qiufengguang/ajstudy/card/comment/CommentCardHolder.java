package com.qiufengguang.ajstudy.card.comment;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.CommentCardBean;
import com.qiufengguang.ajstudy.databinding.CardCommentBinding;

/**
 * 评论卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/3/1 17:31
 */
public class CommentCardHolder extends BaseViewHolder<CardCommentBinding> {

    private CommentCard card;

    public CommentCardHolder(@NonNull CardCommentBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new CommentCard.Builder()
            .setBinding(binding)
            .setListener(this::onCommonClickListener)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != CommentCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        CommentCardBean bean = (CommentCardBean) data.getData();
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