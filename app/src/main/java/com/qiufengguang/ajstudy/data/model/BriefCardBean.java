package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        BriefCardBean that = (BriefCardBean) o;
        return Objects.equals(body, that.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), body);
    }
}
