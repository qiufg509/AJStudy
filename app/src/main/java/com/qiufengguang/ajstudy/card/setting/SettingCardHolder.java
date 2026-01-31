package com.qiufengguang.ajstudy.card.setting;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.activity.second.SecondActivity;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.SettingCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardSettingBinding;
import com.qiufengguang.ajstudy.fragment.second.SecondFragment;

import java.util.List;

/**
 * 设置卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/31 12:56
 */
public class SettingCardHolder extends BaseViewHolder<CardSettingBinding> {

    private SettingCard card;

    public SettingCardHolder(@NonNull CardSettingBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        int spacing = itemView.getResources().getDimensionPixelSize(
            R.dimen.activity_horizontal_margin_s);
        card = new SettingCard.Builder()
            .setRecyclerView(binding.recyclerLgc)
            .setTitleView(binding.tvTitle)
            .setHorizontalSpacing(spacing)
            .setListener(SettingCardHolder::onItemClickListener)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || !data.isCollection()
            || !TextUtils.equals(data.getLayoutName(), SettingCardBean.LAYOUT_NAME)) {
            return;
        }
        if (card == null) {
            initCard();
        }
        @SuppressWarnings("unchecked")
        List<SettingCardBean> beans = (List<SettingCardBean>) data.getData();
        card.setData(beans, data.getCardTitle());
    }

    private static void onItemClickListener(Context context, SettingCardBean bean) {
        if (!(context instanceof AppCompatActivity)) {
            return;
        }
        AppCompatActivity activity = (AppCompatActivity) context;
        Intent intent = new Intent(activity, SecondActivity.class);
        Bundle args = new Bundle();
        args.putString(SecondFragment.ARG_TITLE, bean.getTitle());
        intent.putExtra(SecondActivity.ARGS_SECOND_PAGE_KEY, args);
        activity.startActivity(intent);
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