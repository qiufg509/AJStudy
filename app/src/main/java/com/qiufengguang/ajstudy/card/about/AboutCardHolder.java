package com.qiufengguang.ajstudy.card.about;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.AboutCardBean;
import com.qiufengguang.ajstudy.databinding.CardAboutBinding;

/**
 * 详情页关于卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/3/1 16:12
 */
public class AboutCardHolder extends BaseViewHolder<CardAboutBinding> {

    private AboutCard card;

    public AboutCardHolder(@NonNull CardAboutBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new AboutCard.Builder()
            .setBinding(binding)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != AboutCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        AboutCardBean bean = (AboutCardBean) data.getData();
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