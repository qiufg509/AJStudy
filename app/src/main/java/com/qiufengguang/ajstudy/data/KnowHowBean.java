package com.qiufengguang.ajstudy.data;

import android.text.TextUtils;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;

import java.util.Objects;

/**
 * 知识列表页数据bean
 *
 * @author qiufengguang
 * @since 2025/5/5 23:54
 */
public class KnowHowBean {
    /**
     * 点击跳转详情页
     */
    public static final int TARGET_PAGE_DETAIL = 0;

    /**
     * 点击跳转Markdown页面（展示应用文件目录KotlinCourse文件夹下的文件）
     */
    public static final int TARGET_PAGE_MARKDOWN = 1;

    private int id;
    private String icon;
    private String title;
    private String subtitle;
    private String brief;

    @IntRange(from = 0, to = 3)
    private int targetPage = TARGET_PAGE_DETAIL;

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

    public int getTargetPage() {
        return targetPage;
    }

    public void setTargetPage(@IntRange(from = 0, to = 3) int targetPage) {
        this.targetPage = targetPage;
    }

    public boolean isSame(KnowHowBean bean) {
        if (Objects.isNull(bean)) {
            return false;
        }
        return TextUtils.equals(icon, bean.getIcon())
            && TextUtils.equals(title, bean.getTitle())
            && TextUtils.equals(subtitle, bean.getSubtitle())
            && TextUtils.equals(brief, bean.getBrief());
    }
}
