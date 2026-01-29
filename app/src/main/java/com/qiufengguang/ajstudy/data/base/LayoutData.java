package com.qiufengguang.ajstudy.data.base;

/**
 * 整个卡片数据Bean
 *
 * @author qiufengguang
 * @since 2026/1/19 14:20
 */
public abstract class LayoutData<T> {
    public static final String LAYOUT_NAME = "default";

    public static final int LAYOUT_ID = -1;

    private String layoutName;

    private int layoutId;

    private String cardTitle;

    protected final T data;

    protected LayoutData(T beans) {
        this.data = beans;
    }

    public LayoutData(T beans, String cardTitle) {
        this.data = beans;
        this.cardTitle = cardTitle;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public T getData() {
        return data;
    }

    public String getCardTitle() {
        return cardTitle;
    }

    public abstract boolean isCollection();
}