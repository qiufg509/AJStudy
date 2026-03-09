package com.qiufengguang.ajstudy.data.model;

import androidx.annotation.ColorRes;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

import java.util.Objects;

/**
 * 主题选择卡片数据
 *
 * @author qiufengguang
 * @since 2026/3/9 23:45
 */
public class SelectThemeCardBean extends BaseCardBean {
    private int themeIndex;

    @ColorRes
    private int color;

    private boolean isSelected;

    public SelectThemeCardBean(int themeIndex, String title, @ColorRes int color) {
        this.themeIndex = themeIndex;
        setTitle(title);
        this.color = color;
        this.isSelected = false;
    }

    public int getThemeIndex() {
        return themeIndex;
    }

    public void setThemeIndex(int themeIndex) {
        this.themeIndex = themeIndex;
    }

    @ColorRes
    public int getColor() {
        return color;
    }

    public void setColor(@ColorRes int color) {
        this.color = color;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        SelectThemeCardBean that = (SelectThemeCardBean) o;
        return themeIndex == that.themeIndex
            && color == that.color
            && isSelected == that.isSelected;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), themeIndex, color, isSelected);
    }
}
