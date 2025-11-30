package com.qiufengguang.ajstudy.data;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.Objects;

public class DashboardBean {
    private int id;
    private String icon;
    private String title;
    private String subtitle;
    private String brief;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = this.hashCode();
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public @NonNull String getTitle() {
        return TextUtils.isEmpty(title) ? "" : title.trim();
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public @NonNull String getSubtitle() {
        return TextUtils.isEmpty(subtitle) ? "" : subtitle.trim();
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public @NonNull String getBrief() {
        return TextUtils.isEmpty(brief) ? "" : brief.trim();
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    public boolean isSame(DashboardBean bean) {
        if (Objects.isNull(bean)) {
            return false;
        }
        return TextUtils.equals(icon, bean.getIcon())
                && TextUtils.equals(title, bean.getTitle())
                && TextUtils.equals(subtitle, bean.getSubtitle())
                && TextUtils.equals(brief, bean.getBrief());
    }
}
