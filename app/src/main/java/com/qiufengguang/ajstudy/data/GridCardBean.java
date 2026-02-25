package com.qiufengguang.ajstudy.data;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;
import com.qiufengguang.ajstudy.router.Router;

/**
 * 格网列表卡片数据
 *
 * @author qiufengguang
 * @since 2025/12/28 19:12
 */
public class GridCardBean extends BaseCardBean {
    /**
     * item使用TextView，上icon下文字样式
     */
    public static final int TYPE_TEXT = 0;

    /**
     * item使用ImageView，圆形点中打勾样式
     */
    public static final int TYPE_IMAGE = 1;

    private final int itemType;
    private String title;

    private @DrawableRes int icon;

    private @ColorRes int backgroundTint;

    private String navigateTo;

    public GridCardBean(int backgroundTint) {
        this.backgroundTint = backgroundTint;
        this.itemType = TYPE_IMAGE;
    }

    public GridCardBean(String title, int icon, String navigateTo) {
        this.title = title;
        this.icon = icon;
        this.navigateTo = navigateTo;
        this.itemType = TYPE_TEXT;
        setUri(Router.URI.PAGE_APP_LIST);
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

    public String getNavigateTo() {
        return navigateTo;
    }

    public int getItemType() {
        return itemType;
    }
}
