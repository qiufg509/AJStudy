package com.qiufengguang.ajstudy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.FrameLayout;

import com.qiufengguang.ajstudy.R;

public class RoundedFrameLayout extends FrameLayout {
    private float cornerRadius;

    public RoundedFrameLayout(Context context) {
        super(context);
        init();
    }

    public RoundedFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
        loadAttributes(attrs);
    }

    public RoundedFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        loadAttributes(attrs);
    }

    private void init() {
        // 启用硬件加速
        setLayerType(LAYER_TYPE_HARDWARE, null);
        // 设置自定义Outline Provider
        setOutlineProvider(new RoundRectOutlineProvider());
        // 启用裁剪
        setClipToOutline(true);
    }

    private void loadAttributes(AttributeSet attrs) {
        if (attrs == null) {
            cornerRadius = getResources().getDimensionPixelSize(R.dimen.dashboard_item_img_radius);
            return;
        }
        try (TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.RoundedFrameLayout)) {
            cornerRadius = a.getDimensionPixelSize(R.styleable.RoundedFrameLayout_corner_radius, 16);
        }
    }

    public void setCornerRadius(float radius) {
        this.cornerRadius = radius;
        // 刷新Outline
        invalidateOutline();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldWidth, int oldHeight) {
        super.onSizeChanged(w, h, oldWidth, oldHeight);
        // 尺寸改变时刷新Outline
        invalidateOutline();
    }

    private class RoundRectOutlineProvider extends ViewOutlineProvider {
        @Override
        public void getOutline(View view, Outline outline) {
            if (cornerRadius > 0) {
                // 使用圆角矩形Outline
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), cornerRadius);
            } else {
                // 没有圆角时设置为矩形
                outline.setRect(0, 0, view.getWidth(), view.getHeight());
            }
        }
    }
}
