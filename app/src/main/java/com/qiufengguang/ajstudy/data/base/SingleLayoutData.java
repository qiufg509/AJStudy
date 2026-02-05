package com.qiufengguang.ajstudy.data.base;

import androidx.annotation.IntRange;

/**
 * 包含单个对象的卡片数据
 *
 * @author qiufengguang
 * @since 2026/1/29 12:19
 */
public class SingleLayoutData<T extends BaseCardBean> extends LayoutData<T> {
    public SingleLayoutData(@IntRange int layoutId, T bean) {
        super(bean);
        prepareData(layoutId, bean);
    }

    public SingleLayoutData(@IntRange int layoutId, T bean, String cardTitle) {
        super(bean, cardTitle);
        prepareData(layoutId, bean);
    }

    private void prepareData(@IntRange int layoutId, T bean) {
        if (bean == null) {
            return;
        }
        setLayoutId(layoutId);
    }


    @Override
    public boolean isCollection() {
        return false;
    }
}