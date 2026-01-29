package com.qiufengguang.ajstudy.data.base;

import java.util.List;

/**
 * 包含集合对象的卡片数据
 *
 * @author qiufengguang
 * @since 2026/1/29 12:20
 */
public class CollectionLayoutData<T extends BaseCardBean> extends LayoutData<List<T>> {
    public CollectionLayoutData(List<T> beans) {
        super(beans);
        prepareData(beans);
    }

    public CollectionLayoutData(List<T> beans, String cardTitle) {
        super(beans, cardTitle);
        prepareData(beans);
    }

    private void prepareData(List<T> beans) {
        if (beans.isEmpty()) {
            return;
        }
        T t = beans.get(0);
        setLayoutName(t.getLayoutName());
        setLayoutId(t.getLayoutId());
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
