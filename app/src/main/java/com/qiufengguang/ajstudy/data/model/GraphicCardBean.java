package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

/**
 * 图文卡片数据bean
 *
 * @author qiufengguang
 * @since 2026/1/24 23:34
 */
public class GraphicCardBean extends BaseCardBean {
    private String subtitle;

    private String imageUrl;

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
