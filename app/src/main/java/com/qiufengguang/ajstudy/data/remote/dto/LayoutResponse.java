package com.qiufengguang.ajstudy.data.remote.dto;


import java.util.List;

/**
 * 布局响应数据
 *
 * @author qiufengguang
 * @since 2026/2/26 14:28
 */
public class LayoutResponse {
    private static final int CODE_SUCCESS = 0;

    private List<LayoutDataDTO> layoutData;

    private int rtnCode;

    public boolean isSuccess() {
        return rtnCode == CODE_SUCCESS;
    }

    public List<LayoutDataDTO> getLayoutData() {
        return layoutData;
    }

    public void setLayoutData(List<LayoutDataDTO> layoutData) {
        this.layoutData = layoutData;
    }

    public int getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(int rtnCode) {
        this.rtnCode = rtnCode;
    }
}