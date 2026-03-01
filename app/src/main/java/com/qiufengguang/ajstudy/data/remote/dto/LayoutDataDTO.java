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

    private String subTitle;

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

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
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