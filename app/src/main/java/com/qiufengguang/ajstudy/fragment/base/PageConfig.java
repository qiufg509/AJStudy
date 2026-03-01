package com.qiufengguang.ajstudy.fragment.base;

import com.qiufengguang.ajstudy.view.DynamicToolbar;

/**
 * 页面相关配置属性
 *
 * @author qiufengguang
 * @since 2026/1/29 11:29
 */
public class PageConfig {
    StatusBarMode statusBarMode = StatusBarMode.DARK;

    boolean overlayTitleBar;

    boolean hasNaviBar = true;

    boolean overlayNaviBar;

    DynamicToolbar.Mode mode = DynamicToolbar.Mode.TITLE_ONLY;

    private PageConfig() {
    }

    public enum StatusBarMode {
        NONE,
        LIGHT,
        DARK
    }

    public static class Builder {
        StatusBarMode statusBarMode = StatusBarMode.DARK;

        boolean overlayTitleBar;

        boolean hasNaviBar = true;

        boolean overlayNaviBar;

        DynamicToolbar.Mode titleBarMode = DynamicToolbar.Mode.TITLE_ONLY;


        /**
         * 内容是否延伸到半透明底部导航栏下面是否为深色背景
         * 深色背景则设置浅色状态栏文字、icon
         * 浅色背景则设置深色状态栏文字、icon
         * NONE不控制
         *
         * @param statusBarMode 状态栏颜色模式
         * @return Builder
         */
        public PageConfig.Builder setStatusBarMode(StatusBarMode statusBarMode) {
            this.statusBarMode = statusBarMode;
            return this;
        }

        /**
         * 内容是否延伸到标题栏下面
         *
         * @param overlayTitleBar true内容与标题栏重叠 false内容在标题栏下
         * @return Builder
         */
        public PageConfig.Builder setOverlayTitleBar(boolean overlayTitleBar) {
            this.overlayTitleBar = overlayTitleBar;
            return this;
        }

        /**
         * 设置是否存在底部导航栏(以主页为基础，默认 hasNaviBar = true 存在导航栏)
         * 如果不存在，{@link Builder#setOverlayNaviBar(boolean)}无效
         *
         * @param hasNaviBar true存在 false不存在
         * @return Builder
         */
        public PageConfig.Builder setHasNaviBar(boolean hasNaviBar) {
            this.hasNaviBar = hasNaviBar;
            return this;
        }

        /**
         * 内容是否延伸到半透明底部导航栏下面
         *
         * @param overlayNaviBar true内容与导航栏重叠 false内容在导航栏以上
         * @return Builder
         */
        public PageConfig.Builder setOverlayNaviBar(boolean overlayNaviBar) {
            this.overlayNaviBar = overlayNaviBar;
            return this;
        }

        /**
         * 设置标题模式
         * Mode.GONE 时 overlayTitleBar 无效
         *
         * @param titleBarMode {@link DynamicToolbar.Mode}
         * @return DynamicToolbar.Mode
         */
        public PageConfig.Builder setTitleBarMode(DynamicToolbar.Mode titleBarMode) {
            this.titleBarMode = titleBarMode;
            return this;
        }

        public PageConfig create() {
            PageConfig config = new PageConfig();
            config.statusBarMode = this.statusBarMode;
            config.overlayTitleBar = this.overlayTitleBar;
            config.hasNaviBar = this.hasNaviBar;
            config.overlayNaviBar = this.overlayNaviBar;
            config.mode = this.titleBarMode;
            return config;
        }
    }
}
