package com.qiufengguang.ajstudy.fragment.base;

import com.qiufengguang.ajstudy.view.DynamicToolbar;

/**
 * 页面相关配置属性
 *
 * @author qiufengguang
 * @since 2026/1/29 11:29
 */
public class PageConfig {
    boolean isDarkBackground;

    boolean overlayTitleBar;

    boolean hasNaviBar = true;

    boolean overlayNaviBar;

    DynamicToolbar.Mode mode = DynamicToolbar.Mode.TITLE_ONLY;

    private PageConfig() {
    }

    public static class Builder {
        boolean isDarkBackground;

        boolean overlayTitleBar;

        boolean hasNaviBar = true;

        boolean overlayNaviBar;

        DynamicToolbar.Mode titleBarMode = DynamicToolbar.Mode.TITLE_ONLY;


        /**
         * 内容是否延伸到半透明底部导航栏下面是否为深色背景
         * 深色背景则设置浅色状态栏文字、icon
         * 浅色背景则设置深色状态栏文字、icon
         *
         * @param darkBackground true深色 false浅色
         * @return Builder
         */
        public PageConfig.Builder setDarkBackground(boolean darkBackground) {
            isDarkBackground = darkBackground;
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
            config.isDarkBackground = this.isDarkBackground;
            config.overlayTitleBar = this.overlayTitleBar;
            config.hasNaviBar = this.hasNaviBar;
            config.overlayNaviBar = this.overlayNaviBar;
            config.mode = this.titleBarMode;
            return config;
        }
    }
}
