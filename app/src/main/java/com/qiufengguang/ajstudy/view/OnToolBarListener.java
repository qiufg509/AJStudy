package com.qiufengguang.ajstudy.view;

/**
 * 标题栏DynamicToolbar点击事件监听
 *
 * @author qiufengguang
 * @since 2026/3/23 17:30
 */
public interface OnToolBarListener {
    /**
     * 返回按钮点击
     */
    default void onBackClick() {
    }

    /**
     * 分享按钮点击
     */
    default void onShareClick() {
    }

    /**
     * 菜单按钮点击
     */
    default void onMenuClick() {
    }

    /**
     * 关闭钮点击
     */
    default void onCloseClick() {
    }
}
