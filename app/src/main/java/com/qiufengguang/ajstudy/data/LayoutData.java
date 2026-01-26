package com.qiufengguang.ajstudy.data;

import android.text.TextUtils;

/**
 * 整个卡片数据Bean
 *
 * @author qiufengguang
 * @since 2026/1/19 14:20
 */
public class LayoutData<T> {
    public static final String LAYOUT_NAME = "default";

    private final String layoutName;

    private String cardTitle;

    private final T beans;

    public LayoutData(String layoutName, T beans) {
        this.beans = beans;
        this.layoutName = layoutName;
    }

    public LayoutData(String layoutName, T beans, String cardTitle) {
        this.layoutName = layoutName;
        this.beans = beans;
        this.cardTitle = cardTitle;
    }

    public String getLayoutName() {
        return TextUtils.isEmpty(layoutName) ? LAYOUT_NAME : layoutName;
    }

    public T getBeans() {
        return beans;
    }

    public String getCardTitle() {
        return cardTitle;
    }
}
