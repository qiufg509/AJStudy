package com.qiufengguang.ajstudy.card.largegraphicgrid;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.GridDecoration;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardLargeGraphicBinding;

import java.util.List;

/**
 * 大图文格网卡的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/24 23:31
 */
public class LargeGraphicGridCardHolder extends BaseViewHolder<CardLargeGraphicBinding> {

    private LargeGraphicGridCard card;

    public LargeGraphicGridCardHolder(@NonNull CardLargeGraphicBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        int spacing = itemView.getResources().getDimensionPixelSize(
            R.dimen.activity_horizontal_margin_s);
        card = new LargeGraphicGridCard.Builder()
            .setRecyclerView(binding.recyclerLgc)
            .setTitleView(binding.tvTitle)
            .setSpacingBuilder(new GridDecoration.Builder().spacing(spacing))
            .setListener(this::onItemClickListener)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || !data.isCollection()
            || data.getLayoutId() != LargeGraphicGridCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        @SuppressWarnings("unchecked")
        List<LargeGraphicCardBean> beans = (List<LargeGraphicCardBean>) data.getData();
        card.setData(beans, data.getCardTitle());
    }

    private void onItemClickListener(Context context, LargeGraphicCardBean bean) {
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