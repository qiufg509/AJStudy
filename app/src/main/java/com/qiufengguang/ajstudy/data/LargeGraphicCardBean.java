package com.qiufengguang.ajstudy.data;

/**
 * 大图文卡片数据bean
 *
 * @author qiufengguang
 * @since 2026/1/24 23:34
 */
public class LargeGraphicCardBean extends BaseCardBean {
    public static final String LAYOUT_NAME = "largeGraphicCard";

    private String title;
    private String subtitle;

    private String imageUrl;

    public LargeGraphicCardBean(String title, String subtitle, String imageUrl) {
        this.title = title;
        this.subtitle = subtitle;
        this.imageUrl = imageUrl;
    }

    @Override
    public String getLayoutName() {
        return LAYOUT_NAME;
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
