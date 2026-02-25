package com.qiufengguang.ajstudy.card.graphicl;

import android.content.Context;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardGraphicLBinding;
import com.qiufengguang.ajstudy.router.AppNavigator;

/**
 * 大图文卡的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/2/6 23:24
 */
public class GraphicCardLHolder extends BaseViewHolder<CardGraphicLBinding> {

    private GraphicCardL card;

    public GraphicCardLHolder(@NonNull CardGraphicLBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new GraphicCardL.Builder()
            .setRoot(binding.getRoot())
            .setTvTitle(binding.tvTitle)
            .setTvSubTitle(binding.tvSubtitle)
            .setIvPic(binding.ivPic)
            .setListener(this::onItemClickListener)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != GraphicCardL.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        LargeGraphicCardBean bean = (LargeGraphicCardBean) data.getData();
        card.setData(bean);
    }

    private void onItemClickListener(Context context, LargeGraphicCardBean bean) {
        AppNavigator.getInstance().startArticleActivity(
            context, bean.getUri(), bean.getTitle());
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