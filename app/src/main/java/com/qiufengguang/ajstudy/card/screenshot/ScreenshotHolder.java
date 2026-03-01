package com.qiufengguang.ajstudy.card.screenshot;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.grid.GridCard;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.ScreenshotCardBean;
import com.qiufengguang.ajstudy.databinding.CardScreenshotBinding;

/**
 * 截图卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/3/1 16:48
 */
public class ScreenshotHolder extends BaseViewHolder<CardScreenshotBinding> {

    private ScreenshotCard card;

    public ScreenshotHolder(@NonNull CardScreenshotBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new ScreenshotCard.Builder()
            .setRecyclerView(binding.recyclerView)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != GridCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        ScreenshotCardBean bean = (ScreenshotCardBean) data.getData();
        card.setData(bean);
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