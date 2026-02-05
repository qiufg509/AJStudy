package com.qiufengguang.ajstudy.data;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

/**
 * 大图文卡片数据bean
 *
 * @author qiufengguang
 * @since 2026/1/24 23:34
 */
public class LargeGraphicCardBean extends BaseCardBean {
    private final String title;
    private final String subtitle;

    private final String imageUrl;

    public LargeGraphicCardBean(String title, String subtitle, String imageUrl) {
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
