package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

/**
 * 轮播banner数据bean
 *
 * @author qiufengguang
 * @since 2025/12/19 15:41
 */
public class BannerBean extends BaseCardBean {

    String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
