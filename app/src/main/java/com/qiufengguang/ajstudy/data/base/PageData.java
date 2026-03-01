package com.qiufengguang.ajstudy.data.base;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.data.model.TabData;

import java.util.List;

/**
 * 页面数据
 *
 * @author qiufengguang
 * @since 2026/3/1 21:44
 */
public class PageData {
    private List<LayoutData<?>> layoutData;

    private List<TabData> tabs;

    private int hasNextPage;

    private int count;

    private String name;

    private int totalPages;

    private int rtnCode;

    public List<LayoutData<?>> getLayoutData() {
        return layoutData;
    }

    public void setLayoutData(List<LayoutData<?>> layoutData) {
        this.layoutData = layoutData;
    }

    @NonNull
    public List<TabData> getTabs() {
        return tabs;
    }

    public void setTabs(List<TabData> tabs) {
        this.tabs = tabs;
    }

    public int getHasNextPage() {
        return hasNextPage;
    }

    public void setHasNextPage(int hasNextPage) {
        this.hasNextPage = hasNextPage;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getRtnCode() {
        return rtnCode;
    }

    public void setRtnCode(int rtnCode) {
        this.rtnCode = rtnCode;
    }
}
