package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

import java.util.Objects;

/**
 * 设置卡片数据
 *
 * @author qiufengguang
 * @since 2026/1/31 11:59
 */
public class SettingCardBean extends BaseCardBean {
    private int id;

    private String content;

    private boolean isSwitch;

    private boolean isChecked;

    public SettingCardBean() {
    }

    public SettingCardBean(int id, String title, String detailId) {
        this.id = id;
        setTitle(title);
        setDetailId(detailId);
    }

    public SettingCardBean(int id, String title, boolean isSwitch, boolean isChecked) {
        this.id = id;
        setTitle(title);
        this.isSwitch = isSwitch;
        this.isChecked = isChecked;
    }

    public SettingCardBean(int id, String title, String content, String detailId) {
        this.id = id;
        setTitle(title);
        setDetailId(detailId);
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        SettingCardBean that = (SettingCardBean) o;
        return id == that.id
            && isSwitch == that.isSwitch
            && isChecked == that.isChecked
            && Objects.equals(content, that.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, content, isSwitch, isChecked);
    }
}
