package com.qiufengguang.ajstudy.data;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

/**
 * 设置卡片数据
 *
 * @author qiufengguang
 * @since 2026/1/31 11:59
 */
public class SettingCardBean extends BaseCardBean {
    public static final String LAYOUT_NAME = "settingCard";

    public static final int LAYOUT_ID = 6;

    private final String title;

    private String content;

    private String destinationAction;

    private boolean isSwitchBtn;

    public SettingCardBean(String title, boolean isSwitchBtn) {
        this.title = title;
        this.isSwitchBtn = isSwitchBtn;
    }

    public SettingCardBean(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public SettingCardBean(String title, String content, String destinationAction) {
        this.title = title;
        this.content = content;
        this.destinationAction = destinationAction;
    }

    @Override
    public String getLayoutName() {
        return LAYOUT_NAME;
    }

    @Override
    public int getLayoutId() {
        return LAYOUT_ID;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getDestinationAction() {
        return destinationAction;
    }

    public boolean isSwitchBtn() {
        return isSwitchBtn;
    }
}
