package com.qiufengguang.ajstudy.data.base;

import androidx.annotation.IntRange;

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
}