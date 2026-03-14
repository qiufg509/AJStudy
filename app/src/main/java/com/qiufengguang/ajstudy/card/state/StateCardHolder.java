package com.qiufengguang.ajstudy.card.state;

import android.util.Log;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.StateCardBean;
import com.qiufengguang.ajstudy.databinding.CardStateBinding;

/**
 * 页面状态卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/3/13 18:44
 */
public class StateCardHolder extends BaseViewHolder<CardStateBinding> {

    private StateCard card;

    public StateCardHolder(@NonNull CardStateBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new StateCard.Builder()
            .setBinding(binding)
            .setListener((context, data) ->
                Log.i("qfg", "onItemClick: retry."))
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != StateCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        StateCardBean bean = (StateCardBean) data.getData();
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