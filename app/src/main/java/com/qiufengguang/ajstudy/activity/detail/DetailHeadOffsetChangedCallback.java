package com.qiufengguang.ajstudy.activity.detail;

import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.ImageViewCompat;

import com.google.android.material.appbar.AppBarLayout;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.utils.StatusBarUtil;

/**
 * 详情页头部滑动监听回调
 *
 * @author qiufengguang
 * @since 2025/12/11 18:44
 */
public class DetailHeadOffsetChangedCallback implements AppBarLayout.OnOffsetChangedListener {
    private final ArgbEvaluator argbEvaluator = new ArgbEvaluator();

    /**
     * 详情页面Activity
     */
    private Activity activity;

    /**
     * 标题栏Toolbar
     */
    private Toolbar toolbar;

    /**
     * 标题控件
     */
    private TextView barTitleView;

    /**
     * 返回控件
     */
    private ImageButton barBackView;

    /**
     * 分享控件
     */
    private ImageButton barShareView;

    /**
     * statusBar、actionBar背景从透明渐变至目标颜色
     */
    private int bgEndColor = 0;

    /**
     * actionBar标题、图标浅色
     */
    private int lightColor;

    /**
     * actionBar图标浅色
     */
    private ColorStateList lightTintList;


    /**
     * actionBar图标深色
     */
    private ColorStateList darkTintList;

    /**
     * 期望滚动距离
     */
    private int expectedScrollRange;

    /**
     * 当前状态栏是否为浅色模式
     */
    private boolean isStatusBarLight = false;

    /**
     * 上次记录的比例值（滑动距离/期望距离）
     */
    private float lastFraction = -1f;

    /**
     * statusBar、toolbar的图标颜色切换阈值
     */
    private static final float THRESHOLD = 0.5f;

    public DetailHeadOffsetChangedCallback(DetailActivity activity, @NonNull Toolbar toolbar,
        @NonNull ImageButton barBackView, @NonNull ImageButton barShareView,
        @NonNull TextView barTitleView, int expectedScrollRange) {
        this.activity = activity;
        this.toolbar = toolbar;
        this.barBackView = barBackView;
        this.barShareView = barShareView;
        this.barTitleView = barTitleView;
        this.expectedScrollRange = expectedScrollRange;
    }

    public void release() {
        this.toolbar = null;
        this.barBackView = null;
        this.barShareView = null;
        this.barTitleView = null;
        this.activity = null;
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int totalScrollRange = appBarLayout.getTotalScrollRange();
        if (expectedScrollRange <= 0) {
            expectedScrollRange = totalScrollRange;
        }

        setToolbarBackgroundColor(verticalOffset);

        // 计算滚动比例 (0: 完全展开, 1: 完全收缩)
        float expScrollPercent = 0f;
        if (expectedScrollRange != 0) {
            expScrollPercent = (float) Math.abs(verticalOffset) / expectedScrollRange;
        }
        float fraction = Math.max(0f, Math.min(1f, expScrollPercent));

        setToolbarTitleColor(fraction);

        // 只有在比例明显变化时才更新状态栏
        if (Math.abs(fraction - lastFraction) > 0.01f) {
            boolean shouldBeLight = fraction >= THRESHOLD;
            // 只有当模式发生变化时才更新
            if (shouldBeLight != isStatusBarLight) {
                isStatusBarLight = shouldBeLight;
                StatusBarUtil.setLightStatusBar(activity, shouldBeLight);
                setToolbarIconColor(shouldBeLight);
                barTitleView.setVisibility(shouldBeLight ? View.VISIBLE : View.GONE);
            }
            lastFraction = fraction;
        }
    }

    /**
     * Toolbar背景颜色渐变，从透明渐变到主色
     * 期望滚动距离滚动一半之后才开始渐变
     *
     * @param verticalOffset 滚动距离
     */
    private void setToolbarBackgroundColor(int verticalOffset) {
        int absOffset = Math.abs(verticalOffset);
        if (absOffset < expectedScrollRange / 2.0f) {
            if (toolbar.getBackgroundTintList() == null
                || toolbar.getBackgroundTintList().getDefaultColor() != Color.TRANSPARENT) {
                toolbar.setBackgroundColor(Color.TRANSPARENT);
            }
            return;
        }

        float expScrollPercent = 0f;
        if (expectedScrollRange != 0) {
            expScrollPercent = Math.abs(absOffset - expectedScrollRange / 2.0f) / (expectedScrollRange / 2.0f);
        }
        float fraction = Math.max(0f, Math.min(1f, expScrollPercent));
        if (this.bgEndColor == 0) {
            this.bgEndColor = ContextCompat.getColor(toolbar.getContext(),
                R.color.ajstudy_color_toolbar_bg);
        }
        int currentColor = (int) argbEvaluator.evaluate(fraction, Color.TRANSPARENT, bgEndColor);
        toolbar.setBackgroundColor(currentColor);
    }

    /**
     * Toolbar标题颜色
     *
     * @param fraction 滚动比例
     */
    private void setToolbarTitleColor(float fraction) {
        if (barTitleView.getVisibility() == View.GONE) {
            return;
        }
        if (lightColor == 0) {
            lightColor = ContextCompat.getColor(toolbar.getContext(),
                R.color.ajstudy_color_toolbar_text);
        }
        int currentColor = (int) argbEvaluator.evaluate(fraction, Color.TRANSPARENT, lightColor);
        barTitleView.setTextColor(currentColor);
    }

    /**
     * 设置 Toolbar 图标颜色
     */
    private void setToolbarIconColor(boolean shouldBeLight) {
        // 设置返回按钮图标颜色
        ColorStateList tintList;
        if (shouldBeLight) {
            if (lightTintList == null) {
                if (lightColor == 0) {
                    lightColor = ContextCompat.getColor(toolbar.getContext(),
                        R.color.ajstudy_color_toolbar_text);
                }
                lightTintList = ColorStateList.valueOf(lightColor);
            }
            tintList = lightTintList;
        } else {
            if (darkTintList == null) {
                darkTintList = ColorStateList.valueOf(ContextCompat.getColor(toolbar.getContext(),
                    R.color.ajstudy_color_toolbar_icon_dark));
            }
            tintList = darkTintList;
        }
        ImageViewCompat.setImageTintList(barBackView, tintList);
        ImageViewCompat.setImageTintList(barShareView, tintList);
    }
}
