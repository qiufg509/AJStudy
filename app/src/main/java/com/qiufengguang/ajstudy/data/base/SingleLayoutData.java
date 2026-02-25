package com.qiufengguang.ajstudy.data.base;

import androidx.annotation.IntRange;

import com.qiufengguang.ajstudy.card.empty.EmptyCard;

/**
 * 包含单个对象的卡片数据
 *
 * @author qiufengguang
 * @since 2026/1/29 12:19
 */
public class SingleLayoutData<T extends BaseCardBean> extends LayoutData<T> {
    public SingleLayoutData(@IntRange int layoutId, T bean) {
        super(bean);
        prepareData(layoutId);
    }

    public SingleLayoutData(@IntRange int layoutId, T bean, String cardTitle) {
        super(bean, cardTitle);
        prepareData(layoutId);
    }

    public SingleLayoutData(@IntRange int layoutId, T bean, String cardTitle, String uri) {
        super(bean, cardTitle, uri);
        prepareData(layoutId);
    }

    private void prepareData(@IntRange int layoutId) {
        if (layoutId < EmptyCard.LAYOUT_ID_1) {
            setLayoutId(EmptyCard.LAYOUT_ID_2);
            return;
        }
        setLayoutId(layoutId);
    }


    @Override
    public boolean isCollection() {
        return false;
    }
}