package com.qiufengguang.ajstudy.data.model;

import java.util.List;

/**
 * 详情-介绍子页面数据bean
 *
 * @author qiufengguang
 * @since 2025/12/13 12:15
 */
public class DetailIntroduction {
    public static final String LAYOUT_NAME_SCREEN = "detailscreencard";

    public static final String LAYOUT_NAME_EDITOR_RECOMMEND = "detaileditorrecommendcard";

    public static final String LAYOUT_NAME_APP_INFO = "pcappinfocard";

    public static final String LAYOUT_NAME_APP_INTRO = "detailappintrocard";

    private List<String> images;

    private String editorRecommend;

    private AboutApp aboutApp;

    private String developer;

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public String getEditorRecommend() {
        return editorRecommend;
    }

    public void setEditorRecommend(String editorRecommend) {
        this.editorRecommend = editorRecommend;
    }

    public AboutApp getAboutApp() {
        return aboutApp;
    }

    public void setAboutApp(AboutApp aboutApp) {
        this.aboutApp = aboutApp;
    }

    public String getDeveloper() {
        return developer;
    }

    public void setDeveloper(String developer) {
        this.developer = developer;
    }

    public static class AboutApp {
        private String tariffDesc;
        private String version;
        private String appIntro;

        public String getTariffDesc() {
            return tariffDesc;
        }

        public void setTariffDesc(String tariffDesc) {
            this.tariffDesc = tariffDesc;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getAppIntro() {
            return appIntro;
        }

        public void setAppIntro(String appIntro) {
            this.appIntro = appIntro;
        }
    }
}
