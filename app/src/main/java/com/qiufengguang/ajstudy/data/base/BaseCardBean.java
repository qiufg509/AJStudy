package com.qiufengguang.ajstudy.data.base;

/**
 * 卡片数据基类Bean
 *
 * @author qiufengguang
 * @since 2026/1/19 14:20
 */
public abstract class BaseCardBean {
    public static final String LAYOUT_NAME = "default";

    public static final int LAYOUT_ID = -1;

    public abstract String getLayoutName();

    public abstract int getLayoutId();
}
