package com.qiufengguang.ajstudy.card.setting;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.SettingCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardSettingBinding;

import java.util.List;

/**
 * 设置卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/31 12:56
 */
public class SettingCardHolder extends BaseViewHolder<CardSettingBinding> {

    private SettingCard cardWrapper;

    public SettingCardHolder(@NonNull CardSettingBinding binding) {
        super(binding);
    }

    @Override
    public void initCardWrapper() {
        if (cardWrapper != null) {
            return;
        }
        int spacing = itemView.getResources().getDimensionPixelSize(
            R.dimen.activity_horizontal_margin_s);
        cardWrapper = new SettingCard.Builder()
            .setRecyclerView(binding.recyclerLgc)
            .setTitleView(binding.tvTitle)
            .setHorizontalSpacing(spacing)
            .setListener(SettingCardHolder::onItemClickListener)
            .create();
        cardWrapper.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || !data.isCollection()
            || !TextUtils.equals(data.getLayoutName(), SettingCardBean.LAYOUT_NAME)) {
            return;
        }
        if (cardWrapper == null) {
            initCardWrapper();
        }
        @SuppressWarnings("unchecked")
        List<SettingCardBean> beans = (List<SettingCardBean>) data.getData();
        cardWrapper.setData(beans, data.getCardTitle());
    }

    private static void onItemClickListener(Context context, SettingCardBean bean) {
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