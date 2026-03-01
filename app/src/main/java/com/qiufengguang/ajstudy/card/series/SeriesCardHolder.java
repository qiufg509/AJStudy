package com.qiufengguang.ajstudy.card.series;

import android.content.Context;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.SeriesCardBean;
import com.qiufengguang.ajstudy.databinding.CardSeriesBinding;
import com.qiufengguang.ajstudy.router.AppNavigator;

import java.util.List;

/**
 * 系列卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/25 17:20
 */
public class SeriesCardHolder extends BaseViewHolder<CardSeriesBinding> {

    private SeriesCard card;

    private String title;

    private String moreUri;

    public SeriesCardHolder(@NonNull CardSeriesBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new SeriesCard.Builder()
            .setBinding(binding)
            .setListener(this::onItemClickListener)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || !data.isCollection()
            || data.getLayoutId() != SeriesCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        title = data.getName();
        moreUri = data.getDetailId();
        @SuppressWarnings("unchecked")
        List<SeriesCardBean> beans = (List<SeriesCardBean>) data.getData();
        card.setData(beans, data.getName());
    }

    private void onItemClickListener(Context context, SeriesCardBean bean) {
        if (bean == null) {
            AppNavigator.getInstance().jumpTo(context, moreUri, title);
        } else {
            onCommonClickListener(context, bean);
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