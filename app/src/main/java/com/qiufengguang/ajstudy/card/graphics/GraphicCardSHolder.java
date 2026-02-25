package com.qiufengguang.ajstudy.card.graphics;

import android.content.Context;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardGraphicSBinding;
import com.qiufengguang.ajstudy.router.AppNavigator;

/**
 * 小尺寸图文卡的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/2/26 0:12
 */
public class GraphicCardSHolder extends BaseViewHolder<CardGraphicSBinding> {

    private GraphicCardS card;

    public GraphicCardSHolder(@NonNull CardGraphicSBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new GraphicCardS.Builder()
            .setRoot(binding.getRoot())
            .setTvTitle(binding.tvTitle)
            .setTvSubTitle(binding.tvSubtitle)
            .setIvPic(binding.ivImage)
            .setListener(this::onItemClickListener)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != GraphicCardS.LAYOUT_ID) {
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