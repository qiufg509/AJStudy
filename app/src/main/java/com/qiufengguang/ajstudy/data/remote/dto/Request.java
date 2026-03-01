package com.qiufengguang.ajstudy.data.remote.dto;

/**
 * 请求体
 *
 * @author qiufengguang
 * @since 2026/2/28 13:38
 */
public class Request {
    private int page;
    private int pageSize;
    private String directory;

    public Request() {
    }

    public Request(String directory) {
        this.directory = directory;
    }

    public Request(int page, int pageSize, String directory) {
        this.page = page;
        this.pageSize = pageSize;
        this.directory = directory;
    }

    // getters (必须，用于序列化)
    public int getPage() {
        return page;
    }

    public int getPageSize() {
        return pageSize;
    }

    public String getDirectory() {
        return directory;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }
}
