package com.qiufengguang.ajstudy.card.state;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.base.BaseCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.data.model.StateCardBean;
import com.qiufengguang.ajstudy.databinding.CardStateBinding;

/**
 * 页面状态卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/3/13 18:44
 */
public class StateCardHolder extends BaseViewHolder<CardStateBinding> {
    public static final String ARG_KEY = "state";

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
            .create();
        card.show();
    }

    @Override
    public boolean bind(LayoutData<?> data, OnItemClickListener<BaseCardBean> listener) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != StateCard.LAYOUT_ID) {
            return false;
        }
        if (card == null) {
            initCard();
        }
        card.setListener(listener);
        StateCardBean bean = (StateCardBean) data.getData();
        card.setData(bean);
        return true;
    }

    @Override
    public void update(Bundle bundle) {
        if (bundle == null) {
            return;
        }
        State state = bundle.getParcelable(ARG_KEY);
        if (card != null && state != null) {
            card.update(state);
        }
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