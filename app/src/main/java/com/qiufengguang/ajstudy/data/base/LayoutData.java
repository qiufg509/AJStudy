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


    private String cardTitle;

    private String uri;

    protected final T data;

    private String ext;

    protected LayoutData(T beans) {
        this.data = beans;
    }

    public LayoutData(T beans, String cardTitle) {
        this.data = beans;
        this.cardTitle = cardTitle;
    }

    public LayoutData(T beans, String cardTitle, String uri) {
        this.data = beans;
        this.cardTitle = cardTitle;
        this.uri = uri;
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

    public String getUri() {
        return uri;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }

    public abstract boolean isCollection();
}