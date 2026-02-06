package com.qiufengguang.ajstudy.card.largegraphic;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.activity.markdown.MarkdownActivity;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardLargeGraphicBinding;
import com.qiufengguang.ajstudy.global.Constant;

/**
 * 大图文卡的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/2/6 23:24
 */
public class LargeGraphicCardHolder extends BaseViewHolder<CardLargeGraphicBinding> {

    private LargeGraphicCard card;

    public LargeGraphicCardHolder(@NonNull CardLargeGraphicBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new LargeGraphicCard.Builder()
            .setRoot(binding.getRoot())
            .setTvTitle(binding.tvTitle)
            .setTvSubTitle(binding.tvSubtitle)
            .setIvPic(binding.ivPic)
            .setListener(LargeGraphicCardHolder::onItemClickListener)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != LargeGraphicCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        LargeGraphicCardBean bean = (LargeGraphicCardBean) data.getData();
        card.setData(bean);
    }

    private static void onItemClickListener(Context context, LargeGraphicCardBean bean) {
        Intent intent = new Intent(context, MarkdownActivity.class);
        intent.putExtra("title", "《春》");
        intent.putExtra("filePath", Constant.Data.DETAIL_SPRING);
        context.startActivity(intent);
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