package com.qiufengguang.ajstudy.card.grid;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.GridDecoration;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.GridCardBean;
import com.qiufengguang.ajstudy.databinding.CardGridBinding;

import java.util.List;

/**
 * 格网卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/19 14:03
 */
public class GridViewHolder extends BaseViewHolder<CardGridBinding> {

    private GridCard card;

    public GridViewHolder(@NonNull CardGridBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        int spacing = itemView.getResources().getDimensionPixelSize(
            R.dimen.activity_horizontal_margin_s);
        card = new GridCard.Builder()
            .setRecyclerView(binding.recyclerGrid)
            .setTitleView(binding.tvTitle)
            .setSpacingBuilder(
                new GridDecoration.Builder().horizontalSpacing(spacing)
            )
            .setListener(this::onCommonClickListener)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || !data.isCollection()
            || data.getLayoutId() != GridCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        @SuppressWarnings("unchecked")
        List<GridCardBean> beans = (List<GridCardBean>) data.getData();
        card.setData(beans, data.getName());
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