package com.qiufengguang.ajstudy.card.graphicm;

import android.content.Context;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.model.GraphicCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardGraphicMBinding;
import com.qiufengguang.ajstudy.router.AppNavigator;

/**
 * 中尺寸图文卡的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/2/25 20:38
 */
public class GraphicCardMHolder extends BaseViewHolder<CardGraphicMBinding> {

    private GraphicCardM card;

    public GraphicCardMHolder(@NonNull CardGraphicMBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new GraphicCardM.Builder()
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
            || data.getLayoutId() != GraphicCardM.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        GraphicCardBean bean = (GraphicCardBean) data.getData();
        card.setData(bean);
    }

    private void onItemClickListener(Context context, GraphicCardBean bean) {
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