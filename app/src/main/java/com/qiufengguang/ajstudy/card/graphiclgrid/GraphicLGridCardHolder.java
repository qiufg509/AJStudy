package com.qiufengguang.ajstudy.card.graphiclgrid;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.GridDecoration;
import com.qiufengguang.ajstudy.data.model.GraphicCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardGraphicLGridBinding;

import java.util.List;

/**
 * 大图文格网卡的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/24 23:31
 */
public class GraphicLGridCardHolder extends BaseViewHolder<CardGraphicLGridBinding> {

    private GraphicLGridCard card;

    public GraphicLGridCardHolder(@NonNull CardGraphicLGridBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        int spacing = itemView.getResources().getDimensionPixelSize(
            R.dimen.activity_horizontal_margin_s);
        card = new GraphicLGridCard.Builder()
            .setRecyclerView(binding.recyclerLgc)
            .setTitleView(binding.tvTitle)
            .setSpacingBuilder(new GridDecoration.Builder().horizontalSpacing(spacing))
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || !data.isCollection()
            || data.getLayoutId() != GraphicLGridCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        @SuppressWarnings("unchecked")
        List<GraphicCardBean> beans = (List<GraphicCardBean>) data.getData();
        card.setData(beans, data.getCardTitle());
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