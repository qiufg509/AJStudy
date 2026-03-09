package com.qiufengguang.ajstudy.card.selecttheme;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.SelectThemeCardBean;
import com.qiufengguang.ajstudy.databinding.CardSelectThemeBinding;

/**
 * 选择主题卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/3/9 23:41
 */
public class SelectThemeCardHolder extends BaseViewHolder<CardSelectThemeBinding> {

    private SelectThemeCard card;

    public SelectThemeCardHolder(@NonNull CardSelectThemeBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new SelectThemeCard.Builder()
            .setBinding(binding)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != SelectThemeCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        SelectThemeCardBean bean = (SelectThemeCardBean) data.getData();
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