package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

/**
 * 纯文字卡片数据
 *
 * @author qiufengguang
 * @since 2026/3/1 16:37
 */
public class TextCardBean extends BaseCardBean {
    String text;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
