package com.qiufengguang.ajstudy.card.base;

import com.qiufengguang.ajstudy.global.Constant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 卡片基类
 *
 * @author qiufengguang
 * @since 2026/1/30 18:30
 */
public abstract class Card {

    private static final Map<Integer, CardCreator> CREATOR = new ConcurrentHashMap<>();

    /**
     * 注册卡片
     *
     * @param layoutId 卡片id
     * @param creator  ViewHolderCreator
     */
    public static void register(int layoutId, CardCreator creator) {
        if (creator == null) {
            return;
        }
        CREATOR.put(layoutId, creator);
    }


    /**
     * 获取卡片占用列数配置
     *
     * @param pln 显示的列数
     * @return 列数配置
     */
    public static Map<Integer, Integer> getSpanSizeMap(int pln) {
        return getSpanSizeMap(pln, pln, pln);
    }

    /**
     * 获取卡片占用列数配置
     *
     * @param pln4 4栅格显示的列数
     * @param pln8 8/12栅格时显示的列数
     * @return 列数配置
     */
    public static Map<Integer, Integer> getSpanSizeMap(int pln4, int pln8) {
        return getSpanSizeMap(pln4, pln8, pln8);
    }

    /**
     * 获取卡片占用列数配置
     *
     * @param pln4  4栅格显示的列数
     * @param pln8  8栅格时显示的列数
     * @param pln12 12栅格时显示的列数
     * @return 列数配置
     */
    public static Map<Integer, Integer> getSpanSizeMap(int pln4, int pln8, int pln12) {
        return Map.of(
            Constant.Grid.COLUMN_DEFAULT, pln4,
            Constant.Grid.COLUMN_8, pln8,
            Constant.Grid.COLUMN_12, pln12
        );
    }

    /**
     * 获取创建器
     *
     * @param layoutId 卡片id
     * @return ViewHolderCreator
     */
    public static CardCreator getCreator(int layoutId) {
        return CREATOR.get(layoutId);
    }
}