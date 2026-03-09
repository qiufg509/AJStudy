package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

import java.util.Objects;

/**
 * 系列卡片数据bean
 *
 * @author qiufengguang
 * @since 2026/1/25 15:44
 */
public class SeriesCardBean extends BaseCardBean {
    private String imageUrl;

    private String tag;

    private String viewCount;

    private String totalDuration;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public String getTotalDuration() {
        return totalDuration;
    }

    public void setTotalDuration(String totalDuration) {
        this.totalDuration = totalDuration;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        SeriesCardBean that = (SeriesCardBean) o;
        return Objects.equals(imageUrl, that.imageUrl)
            && Objects.equals(tag, that.tag)
            && Objects.equals(viewCount, that.viewCount)
            && Objects.equals(totalDuration, that.totalDuration);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), imageUrl, tag, viewCount, totalDuration);
    }
}
