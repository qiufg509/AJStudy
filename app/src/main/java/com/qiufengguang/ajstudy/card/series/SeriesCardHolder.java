package com.qiufengguang.ajstudy.card.series;

import android.content.Context;
import android.text.TextUtils;

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

    private SeriesCard cardWrapper;

    public SeriesCardHolder(@NonNull CardSeriesBinding binding) {
        super(binding);
    }

    @Override
    public void initCardWrapper() {
        if (cardWrapper != null) {
            return;
        }
        cardWrapper = new SeriesCard.Builder()
            .setBinding(binding)
            .setListener(this::onItemClickListener)
            .create();
        cardWrapper.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || !data.isCollection()
            || !TextUtils.equals(data.getLayoutName(), SeriesCardBean.LAYOUT_NAME)) {
            return;
        }
        if (cardWrapper == null) {
            initCardWrapper();
        }
        @SuppressWarnings("unchecked")
        List<SeriesCardBean> beans = (List<SeriesCardBean>) data.getData();
        cardWrapper.setData(beans, data.getCardTitle());
    }

    private void onItemClickListener(Context context, SeriesCardBean bean) {
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