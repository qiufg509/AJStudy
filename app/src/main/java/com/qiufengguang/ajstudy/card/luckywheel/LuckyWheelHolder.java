package com.qiufengguang.ajstudy.card.luckywheel;

import android.widget.Toast;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.model.LuckyWheelCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardLuckyWheelBinding;

import java.util.List;

/**
 * 幸运转盘卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/2/1 21:49
 */
public class LuckyWheelHolder extends BaseViewHolder<CardLuckyWheelBinding> {

    private LuckyWheelCard card;

    public LuckyWheelHolder(@NonNull CardLuckyWheelBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new LuckyWheelCard.Builder()
            .setBinding(binding)
            .setListener((context, bean) ->
                Toast.makeText(context, bean.getContent(), Toast.LENGTH_SHORT).show())
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || !data.isCollection()
            || data.getLayoutId() != LuckyWheelCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        @SuppressWarnings("unchecked")
        List<LuckyWheelCardBean> beans = (List<LuckyWheelCardBean>) data.getData();
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