package com.qiufengguang.ajstudy.data.base;

import androidx.annotation.IntRange;

/**
 * 整个卡片数据Bean
 *
 * @author qiufengguang
 * @since 2026/1/19 14:20
 */
public abstract class LayoutData<T> {
    public static final int LAYOUT_ID = 0;

    @IntRange
    private int layoutId;

    private String cardTitle;

    protected final T data;

    private String ext;

    protected LayoutData(T beans) {
        this.data = beans;
    }

    public LayoutData(T beans, String cardTitle) {
        this.data = beans;
        this.cardTitle = cardTitle;
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

    public String getCardTitle() {
        return cardTitle;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public abstract boolean isCollection();
}