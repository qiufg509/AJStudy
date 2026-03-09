package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

import java.util.Objects;

/**
 * 普通卡片数据bean
 *
 * @author qiufengguang
 * @since 2025/5/5 23:54
 */
public class NormalCardBean extends BaseCardBean {
    private int id;
    private String icon;
    private String subtitle;
    private String brief;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        NormalCardBean that = (NormalCardBean) o;
        return id == that.id
            && Objects.equals(icon, that.icon)
            && Objects.equals(subtitle, that.subtitle)
            && Objects.equals(brief, that.brief);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, icon, subtitle, brief);
    }
}
