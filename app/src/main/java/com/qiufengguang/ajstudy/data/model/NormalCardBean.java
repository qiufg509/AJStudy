package com.qiufengguang.ajstudy.data.model;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

/**
 * 普通卡片数据bean
 *
 * @author qiufengguang
 * @since 2025/5/5 23:54
 */
public class NormalCardBean extends BaseCardBean {
    private int id;
    private String icon;
    private String title;
    private String subtitle;
    private String brief;

    private String navigateTo;

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

    public String getNavigateTo() {
        return navigateTo;
    }

    public void setNavigateTo(String navigateTo) {
        this.navigateTo = navigateTo;
    }
}
