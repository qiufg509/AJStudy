package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

/**
 * 设置卡片数据
 *
 * @author qiufengguang
 * @since 2026/1/31 11:59
 */
public class SettingCardBean extends BaseCardBean {
    private final String title;

    private String content;

    private boolean isSwitchBtn;

    public SettingCardBean(String title, boolean isSwitchBtn) {
        this.title = title;
        this.isSwitchBtn = isSwitchBtn;
    }

    public SettingCardBean(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public SettingCardBean(String title, String content, String uri) {
        this.title = title;
        this.content = content;
        setUri(uri);
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isSwitchBtn() {
        return isSwitchBtn;
    }
}
