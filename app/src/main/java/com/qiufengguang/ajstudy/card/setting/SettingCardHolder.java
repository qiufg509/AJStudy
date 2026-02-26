package com.qiufengguang.ajstudy.card.setting;

import android.content.Context;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.model.SettingCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardSettingBinding;
import com.qiufengguang.ajstudy.router.AppNavigator;

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
        int horizontalSpacing = itemView.getResources().getDimensionPixelSize(
            R.dimen.activity_horizontal_margin_s);
        card = new SettingCard.Builder()
            .setRecyclerView(binding.recyclerLgc)
            .setTitleView(binding.tvTitle)
            .setHorizontalSpacing(horizontalSpacing)
            .setListener(SettingCardHolder::onItemClickListener)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || !data.isCollection()
            || data.getLayoutId() != SettingCard.LAYOUT_ID) {
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
        AppNavigator.getInstance().startSecondActivity(context, bean.getUri(), bean.getTitle());
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