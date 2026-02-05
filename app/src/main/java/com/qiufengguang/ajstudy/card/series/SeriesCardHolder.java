package com.qiufengguang.ajstudy.card.series;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.SeriesCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardSeriesBinding;

import java.util.List;

/**
 * 系列卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/25 17:20
 */
public class SeriesCardHolder extends BaseViewHolder<CardSeriesBinding> {

    private SeriesCard card;

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
        @SuppressWarnings("unchecked")
        List<SeriesCardBean> beans = (List<SeriesCardBean>) data.getData();
        card.setData(beans, data.getCardTitle());
    }

    private void onItemClickListener(Context context, SeriesCardBean bean) {
        if (!(context instanceof AppCompatActivity)) {
            return;
        }
        AppCompatActivity activity = (AppCompatActivity) context;
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