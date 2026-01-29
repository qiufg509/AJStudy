package com.qiufengguang.ajstudy.fragment.base;

/**
 * 页面相关配置属性
 *
 * @author qiufengguang
 * @since 2026/1/29 11:29
 */
public class PageConfig {
    /**
     *
     */
    boolean overlayNaviBar;

    boolean overlayTitleBar;

    boolean isDarkBackground;

    private PageConfig() {
    }

    public static class Builder {
        boolean overlayTitleBar;

        boolean overlayNaviBar;

        boolean isDarkBackground;

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

        public PageConfig create() {
            PageConfig config = new PageConfig();
            config.overlayTitleBar = this.overlayTitleBar;
            config.overlayNaviBar = this.overlayNaviBar;
            config.isDarkBackground = this.isDarkBackground;
            return config;
        }
    }
}
