package com.qiufengguang.ajstudy.card.series;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.activity.markdown.MarkdownActivity;
import com.qiufengguang.ajstudy.activity.second.SecondActivity;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.SeriesCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardSeriesBinding;
import com.qiufengguang.ajstudy.fragment.second.SecondFragment;
import com.qiufengguang.ajstudy.global.Constant;

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
        title = data.getCardTitle();
        @SuppressWarnings("unchecked")
        List<SeriesCardBean> beans = (List<SeriesCardBean>) data.getData();
        card.setData(beans, data.getCardTitle());
    }

    private void onItemClickListener(Context context, SeriesCardBean bean) {
        Intent intent;
        if (bean == null) {
            intent = new Intent(context, SecondActivity.class);
            Bundle args = new Bundle();
            args.putString(SecondFragment.ARG_TITLE, title);
            args.putString(SecondFragment.ARG_URI, title);
            intent.putExtra(SecondActivity.ARGS_SECOND_PAGE_KEY, args);
        } else {
            intent = new Intent(context, MarkdownActivity.class);
            intent.putExtra("title", "《春》");
            intent.putExtra("filePath", Constant.Data.DETAIL_SPRING);
        }
        context.startActivity(intent);
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