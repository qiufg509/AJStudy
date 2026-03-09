package com.qiufengguang.ajstudy.data.base;

import androidx.annotation.IntRange;

import java.util.Objects;

/**
 * 整个卡片数据Bean
 *
 * @author qiufengguang
 * @since 2026/1/19 14:20
 */
public abstract class LayoutData<T> {

    @IntRange
    private int layoutId;

    private String name;

    private String detailId;

    protected final T data;

    private String ext;

    protected LayoutData(T beans) {
        this.data = beans;
    }

    public LayoutData(T beans, String name) {
        this.data = beans;
        this.name = name;
    }

    public LayoutData(T beans, String name, String detailId) {
        this.data = beans;
        this.name = name;
        this.detailId = detailId;
    }

    @IntRange
    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(@IntRange int layoutId) {
        this.layoutId = layoutId;
    }

    public T getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public String getDetailId() {
        return detailId;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public abstract boolean isCollection();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        LayoutData<?> that = (LayoutData<?>) o;
        if (layoutId != that.layoutId) {
            return false;
        }
        if (!Objects.equals(name, that.name)) {
            return false;
        }
        if (!Objects.equals(detailId, that.detailId)) {
            return false;
        }
        if (!Objects.equals(ext, that.ext)) {
            return false;
        }
        return Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(layoutId, name, detailId, ext, data);
    }
}