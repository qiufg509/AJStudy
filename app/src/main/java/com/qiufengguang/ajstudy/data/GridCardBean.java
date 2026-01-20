package com.qiufengguang.ajstudy.data;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

/**
 * 格网列表卡片数据
 *
 * @author qiufengguang
 * @since 2025/12/28 19:12
 */
public class GridCardBean extends BaseCardBean {
    public static final String LAYOUT_NAME = "gridCard";

    private String title;

    private @DrawableRes int icon;

    private @ColorRes int backgroundTint;

    private String navigatePage;

    public GridCardBean(String title, int icon, String navigatePage) {
        this.title = title;
        this.icon = icon;
        this.navigatePage = navigatePage;
    }

    public GridCardBean(int backgroundTint) {
        this.backgroundTint = backgroundTint;
    }


    @Override
    public String getLayoutName() {
        return LAYOUT_NAME;
    }

    public String getTitle() {
        return title;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public int getIcon() {
        return icon;
    }

    public int getBackgroundTint() {
        return backgroundTint;
    }

    public String getNavigatePage() {
        return navigatePage;
    }
}
