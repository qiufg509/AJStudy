package com.qiufengguang.ajstudy.data.base;

import java.util.Objects;

/**
 * 卡片数据基类Bean
 *
 * @author qiufengguang
 * @since 2026/1/19 14:20
 */
public abstract class BaseCardBean {
    private String title;

    private String detailId;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BaseCardBean that = (BaseCardBean) o;
        if (!Objects.equals(title, that.title)) {
            return false;
        }
        return Objects.equals(detailId, that.detailId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, detailId);
    }
}
