package com.qiufengguang.ajstudy.card.largegraphic;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardLargeGraphicBinding;

import java.util.List;

/**
 * 大图文卡的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/24 23:31
 */
public class LargeGraphicCardHolder extends BaseViewHolder<CardLargeGraphicBinding> {

    private LargeGraphicCard cardWrapper;

    public LargeGraphicCardHolder(@NonNull CardLargeGraphicBinding binding) {
        super(binding);
    }

    @Override
    public void initCardWrapper() {
        if (cardWrapper != null) {
            return;
        }
        int spacing = itemView.getResources().getDimensionPixelSize(
            R.dimen.activity_horizontal_margin_s);
        cardWrapper = new LargeGraphicCard.Builder()
            .setRecyclerView(binding.recyclerLgc)
            .setTitleView(binding.tvTitle)
            .setSpacing(spacing)
            .setListener(LargeGraphicCardHolder::onItemClickListener)
            .create();
        cardWrapper.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || !data.isCollection()
            || !TextUtils.equals(data.getLayoutName(), LargeGraphicCardBean.LAYOUT_NAME)) {
            return;
        }
        if (cardWrapper == null) {
            initCardWrapper();
        }
        @SuppressWarnings("unchecked")
        List<LargeGraphicCardBean> beans = (List<LargeGraphicCardBean>) data.getData();
        cardWrapper.setData(beans, data.getCardTitle());
    }

    private static void onItemClickListener(Context context, LargeGraphicCardBean bean) {
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