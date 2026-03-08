package com.qiufengguang.ajstudy.fragment.base;

import android.animation.ArgbEvaluator;
import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.view.DynamicToolbar;

import java.lang.ref.WeakReference;

/**
 * RecyclerView 滚动监听，实现标题栏背景色和标题文字的渐变
 *
 * @author qiufengguang
 * @since 2026/3/8 17:53
 */
public class TitleBarScrollListener extends RecyclerView.OnScrollListener {

    private final WeakReference<DynamicToolbar> toolbarWeakRef;
    /**
     * 头卡高度（像素）
     */
    private final int headerHeightPx;
    /**
     * 最终背景色
     */
    private final int endColor;

    /**
     * 上次的进度值
     */
    private float lastFraction = -1f;

    /**
     * -1 表示未初始化
     */
    private int totalScrollY = -1;

    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    public TitleBarScrollListener(@NonNull DynamicToolbar toolbar, int headerHeightPx) {
        this.toolbarWeakRef = new WeakReference<>(toolbar);
        this.headerHeightPx = headerHeightPx;
        this.endColor = ContextCompat.getColor(toolbar.getContext(), R.color.ajstudy_window_background);
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        // 首次调用时初始化 totalScrollY
        if (totalScrollY == -1) {
            totalScrollY = recyclerView.computeVerticalScrollOffset();
        } else {
            // 采用累加滚动增量的方式保证进度，解决 computeVerticalScrollOffset() 跳变导致的二次渐变问题
            totalScrollY += dy;
            if (totalScrollY < 0) {
                totalScrollY = 0;
            }
        }

        // 计算渐变进度 fraction
        float fraction;
        if (totalScrollY <= headerHeightPx / 2) {
            fraction = 0f;
        } else if (totalScrollY >= headerHeightPx) {
            fraction = 1f;
        } else {
            fraction = (totalScrollY - headerHeightPx / 2.0f) / (headerHeightPx / 2.0f);
        }

        // 如果 fraction 与上次相同，跳过 UI 更新
        if (fraction == lastFraction) {
            return;
        }
        lastFraction = fraction;

        if (toolbarWeakRef == null) {
            return;
        }
        DynamicToolbar toolbar = toolbarWeakRef.get();
        if (toolbar == null) {
            return;
        }

        // 应用背景颜色渐变
        int currentColor = (int) argbEvaluator.evaluate(fraction, Color.TRANSPARENT, endColor);
        toolbar.setBackgroundColor(currentColor);

        // 处理标题透明度渐变（仅当标题当前可见时）
        TextView titleView = toolbar.getTitleView();
        if (titleView.getVisibility() == View.VISIBLE) {
            titleView.setAlpha(fraction);
        }
    }
}