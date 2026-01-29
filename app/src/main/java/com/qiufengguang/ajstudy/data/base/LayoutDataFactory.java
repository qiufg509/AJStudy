package com.qiufengguang.ajstudy.data.base;

import java.util.List;

/**
 * 卡片数据工厂方法
 *
 * @author qiufengguang
 * @since 2026/1/29 12:21
 */
public class LayoutDataFactory {
    public static <T extends BaseCardBean> SingleLayoutData<T> createSingle(T bean) {
        return new SingleLayoutData<>(bean);
    }

    public static <T extends BaseCardBean> SingleLayoutData<T> createSingle(T bean, String cardTitle) {
        return new SingleLayoutData<>(bean, cardTitle);
    }

    public static <T extends BaseCardBean> CollectionLayoutData<T> createCollection(List<T> beans) {
        return new CollectionLayoutData<>(beans);
    }

    public static <T extends BaseCardBean> CollectionLayoutData<T> createCollection(List<T> beans, String cardTitle) {
        return new CollectionLayoutData<>(beans, cardTitle);
    }
}