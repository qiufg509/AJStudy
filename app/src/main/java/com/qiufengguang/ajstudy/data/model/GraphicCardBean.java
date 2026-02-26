package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;
import com.qiufengguang.ajstudy.global.Constant;

/**
 * 图文卡片数据bean
 *
 * @author qiufengguang
 * @since 2026/1/24 23:34
 */
public class GraphicCardBean extends BaseCardBean {
    private final String title;
    private final String subtitle;

    private final String imageUrl;

    public GraphicCardBean(String title, String subtitle, String imageUrl) {
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
        setUri(Constant.Data.DETAIL_SPRING);
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
