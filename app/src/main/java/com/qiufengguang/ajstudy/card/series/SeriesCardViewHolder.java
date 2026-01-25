package com.qiufengguang.ajstudy.card.series;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.SeriesCardBean;
import com.qiufengguang.ajstudy.databinding.CardSeriesBinding;

import java.util.List;

/**
 * 系列卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/25 17:20
 */
public class SeriesCardViewHolder extends BaseViewHolder<CardSeriesBinding, List<SeriesCardBean>> {

    private SeriesCardWrapper cardWrapper;

    public SeriesCardViewHolder(@NonNull CardSeriesBinding binding) {
        super(binding);
    }

    @Override
    public void initCardWrapper() {
        if (cardWrapper != null) {
            return;
        }
        cardWrapper = new SeriesCardWrapper.Builder()
            .setBinding(binding)
            .setListener(SeriesCardViewHolder::onItemClickListener)
            .create();
        cardWrapper.show();
    }

    @Override
    public void bind(List<SeriesCardBean> beans) {
        if (beans == null) {
            return;
        }
        if (cardWrapper == null) {
            initCardWrapper();
        }
        cardWrapper.setData(beans);
    }

    private static void onItemClickListener(Context context, SeriesCardBean bean) {
        if (!(context instanceof AppCompatActivity)) {
            return;
        }
        AppCompatActivity activity = (AppCompatActivity) context;
    }

    @Override
    public void cleanup() {
        if (cardWrapper != null) {
            cardWrapper.release();
            cardWrapper = null;
        }
        super.cleanup();
    }
}