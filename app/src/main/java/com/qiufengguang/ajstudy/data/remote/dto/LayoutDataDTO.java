package com.qiufengguang.ajstudy.data.remote.dto;

import com.google.gson.JsonArray;

/**
 * 数据传输对象
 *
 * @author qiufengguang
 * @since 2026/2/26 14:51
 */
public class LayoutDataDTO {
    private int layoutId;

    private String layoutName;

    private String name;

    private String subtitle;

    private JsonArray dataList;

    private String detailId;

    public int getLayoutId() {
        return layoutId;
    }

    public void setLayoutId(int layoutId) {
        this.layoutId = layoutId;
    }

    public String getLayoutName() {
        return layoutName;
    }

    public void setLayoutName(String layoutName) {
        this.layoutName = layoutName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public JsonArray getDataList() {
        return dataList;
    }

    public void setDataList(JsonArray dataList) {
        this.dataList = dataList;
    }

    public String getDetailId() {
        return detailId;
    }

    public void setDetailId(String detailId) {
        this.detailId = detailId;
    }
}