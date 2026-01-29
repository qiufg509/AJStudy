package com.qiufengguang.ajstudy.data;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

/**
 * 轮播banner数据bean
 *
 * @author qiufengguang
 * @since 2025/12/19 15:41
 */
public class BannerBean extends BaseCardBean {
    public static final String LAYOUT_NAME = "bannerCard";

    public static final int LAYOUT_ID = 0;

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

    @Override
    public int getLayoutId() {
        return LAYOUT_ID;
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
