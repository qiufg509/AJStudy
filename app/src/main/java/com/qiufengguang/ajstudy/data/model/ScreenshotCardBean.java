package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

import java.util.List;

/**
 * 截图卡数据
 *
 * @author qiufengguang
 * @since 2026/3/1 16:54
 */
public class ScreenshotCardBean extends BaseCardBean {
    List<String> images;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }
}
