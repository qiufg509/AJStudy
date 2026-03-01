package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

/**
 * 详情页一句话卡片数据
 *
 * @author qiufengguang
 * @since 2026/3/1 15:58
 */
public class BriefCardBean extends BaseCardBean {
    String body;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
