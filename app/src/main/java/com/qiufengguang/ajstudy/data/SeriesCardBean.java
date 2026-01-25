package com.qiufengguang.ajstudy.data;

/**
 * 系列卡片数据bean
 *
 * @author qiufengguang
 * @since 2026/1/25 15:44
 */
public class SeriesCardBean extends BaseCardBean {

    public static final String LAYOUT_NAME = "seriesCard";

    private String title;

    private String imageUrl;

    public SeriesCardBean(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    @Override
    public String getLayoutName() {
        return LAYOUT_NAME;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
