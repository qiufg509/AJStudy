package com.qiufengguang.ajstudy.data;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

/**
 * 系列卡片数据bean
 *
 * @author qiufengguang
 * @since 2026/1/25 15:44
 */
public class SeriesCardBean extends BaseCardBean {

    public static final String LAYOUT_NAME = "seriesCard";

    public static final int LAYOUT_ID = 2;

    private final String title;

    private final String imageUrl;

    private String tag;

    private String viewCount;

    private String totalDuration;


    public SeriesCardBean(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public SeriesCardBean(String title, String imageUrl, String tag) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.tag = tag;
    }

    public SeriesCardBean(String title, String imageUrl, String viewCount, String totalDuration) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.viewCount = viewCount;
        this.totalDuration = totalDuration;
    }

    public SeriesCardBean(String title, String imageUrl, String tag, String viewCount, String totalDuration) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.tag = tag;
        this.viewCount = viewCount;
        this.totalDuration = totalDuration;
    }

    @Override
    public String getLayoutName() {
        return LAYOUT_NAME;
    }

    @Override
    public int getLayoutId() {
        return LAYOUT_ID;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTag() {
        return tag;
    }

    public String getViewCount() {
        return viewCount;
    }

    public String getTotalDuration() {
        return totalDuration;
    }
}
