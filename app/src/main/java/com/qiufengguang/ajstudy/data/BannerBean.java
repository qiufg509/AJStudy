package com.qiufengguang.ajstudy.data;

/**
 * 轮播banner数据bean
 *
 * @author qiufengguang
 * @since 2025/12/19 15:41
 */
public class BannerBean extends BaseCardBean {
    public static final String LAYOUT_NAME = "bannerCard";

    String title;

    String imageUrl;

    public BannerBean(String title, String imageUrl) {
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

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
