package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ScreenshotCardBean that = (ScreenshotCardBean) o;
        return Objects.equals(images, that.images);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), images);
    }
}
