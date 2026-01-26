package com.qiufengguang.ajstudy.data;

/**
 * 系列卡片数据bean
 *
 * @author qiufengguang
 * @since 2026/1/25 15:44
 */
public class SeriesCardBean {

    public static final String LAYOUT_NAME = "seriesCard";

    private final String title;

    private final String imageUrl;

    public SeriesCardBean(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
