package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

import java.util.Objects;

/**
 * 格网列表卡片数据
 *
 * @author qiufengguang
 * @since 2025/12/28 19:12
 */
public class GridCardBean extends BaseCardBean {
    private String icon;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        GridCardBean that = (GridCardBean) o;
        return Objects.equals(icon, that.icon);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), icon);
    }
}
