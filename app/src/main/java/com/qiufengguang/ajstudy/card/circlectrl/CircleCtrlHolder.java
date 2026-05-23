package com.qiufengguang.ajstudy.card.circlectrl;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardCircleCtrlBinding;

/**
 * 圆形控制器卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/2/1 21:49
 */
public class CircleCtrlHolder extends BaseViewHolder<CardCircleCtrlBinding> {

    private CircleCtrlCard card;

    public CircleCtrlHolder(@NonNull CardCircleCtrlBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new CircleCtrlCard.Builder()
            .setBinding(binding)
            .setListener(this::handleResult)
            .create();
        card.show();
    }

    @Override
    public boolean bind(LayoutData<?> data, LifecycleOwner lifecycleOwner) {
        if (data == null || data.isCollection() || data.getLayoutId() != CircleCtrlCard.LAYOUT_ID) {
            return false;
        }
        if (card == null) {
            initCard();
        }
        card.setData(data.getName());
        return true;
    }

    private void handleResult(Context context, float angleDegrees) {
        String direction;
        if (angleDegrees >= 45 && angleDegrees < 135) {
            direction = "下";
        } else if (angleDegrees >= 135 && angleDegrees < 225) {
            direction = "左";
        } else if (angleDegrees >= 225 && angleDegrees < 315) {
            direction = "上";
        } else {
            direction = "右";
        }
        Toast.makeText(context.getApplicationContext(), direction, Toast.LENGTH_SHORT).show();
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