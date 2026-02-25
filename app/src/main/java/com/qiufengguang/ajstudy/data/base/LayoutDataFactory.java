package com.qiufengguang.ajstudy.data.base;

import androidx.annotation.IntRange;

import java.util.List;

/**
 * 卡片数据工厂方法
 *
 * @author qiufengguang
 * @since 2026/1/29 12:21
 */
public class LayoutDataFactory {
    public static <T extends BaseCardBean> SingleLayoutData<T> createSingle(
        @IntRange int layoutId, T bean) {
        return new SingleLayoutData<>(layoutId, bean);
    }

    public static <T extends BaseCardBean> SingleLayoutData<T> createSingle(
        @IntRange int layoutId, T bean, String cardTitle) {
        return new SingleLayoutData<>(layoutId, bean, cardTitle);
    }

    public static <T extends BaseCardBean> SingleLayoutData<T> createSingle(
        @IntRange int layoutId, T bean, String cardTitle, String uri) {
        return new SingleLayoutData<>(layoutId, bean, cardTitle, uri);
    }

    public static <T extends BaseCardBean> CollectionLayoutData<T> createCollection(
        @IntRange int layoutId, List<T> beans) {
        return new CollectionLayoutData<>(layoutId, beans);
    }

    public static <T extends BaseCardBean> CollectionLayoutData<T> createCollection(
        @IntRange int layoutId, List<T> beans, String cardTitle) {
        return new CollectionLayoutData<>(layoutId, beans, cardTitle);
    }

    public static <T extends BaseCardBean> CollectionLayoutData<T> createCollection(
        @IntRange int layoutId, List<T> beans, String cardTitle, String uri) {
        return new CollectionLayoutData<>(layoutId, beans, cardTitle, uri);
    }
}