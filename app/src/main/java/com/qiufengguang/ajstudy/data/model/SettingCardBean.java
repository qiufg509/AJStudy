package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

/**
 * 设置卡片数据
 *
 * @author qiufengguang
 * @since 2026/1/31 11:59
 */
public class SettingCardBean extends BaseCardBean {
    private String content;

    private boolean isSwitch;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isSwitch() {
        return isSwitch;
    }

    public void setSwitch(boolean aSwitch) {
        isSwitch = aSwitch;
    }
}
