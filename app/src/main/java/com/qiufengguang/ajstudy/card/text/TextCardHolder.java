package com.qiufengguang.ajstudy.card.text;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.TextCardBean;
import com.qiufengguang.ajstudy.databinding.CardTextBinding;

/**
 * 纯文字卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/3/1 16:36
 */
public class TextCardHolder extends BaseViewHolder<CardTextBinding> {

    private TextCard card;

    public TextCardHolder(@NonNull CardTextBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new TextCard.Builder()
            .setBinding(binding)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != TextCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        TextCardBean bean = (TextCardBean) data.getData();
        card.setData(bean, data.getName());
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