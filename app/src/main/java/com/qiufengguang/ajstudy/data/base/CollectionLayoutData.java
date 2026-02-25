package com.qiufengguang.ajstudy.data.base;

import androidx.annotation.IntRange;

import java.util.List;

/**
 * 包含集合对象的卡片数据
 *
 * @author qiufengguang
 * @since 2026/1/29 12:20
 */
public class CollectionLayoutData<T extends BaseCardBean> extends LayoutData<List<T>> {
    public CollectionLayoutData(@IntRange int layoutId, List<T> beans) {
        super(beans);
        prepareData(layoutId, beans);
    }

    public CollectionLayoutData(@IntRange int layoutId, List<T> beans, String cardTitle) {
        super(beans, cardTitle);
        prepareData(layoutId, beans);
    }

    public CollectionLayoutData(@IntRange int layoutId, List<T> beans, String cardTitle, String uri) {
        super(beans, cardTitle, uri);
        prepareData(layoutId, beans);
    }

    private void prepareData(@IntRange int layoutId, List<T> beans) {
        if (beans.isEmpty()) {
            return;
        }
        setLayoutId(layoutId);
    }

    @Override
    public boolean isCollection() {
        return true;
    }

    public int size() {
        return data.size();
    }

    public T get(int index) {
        return data.get(index);
    }
}
