package com.qiufengguang.ajstudy.card.base;

import com.qiufengguang.ajstudy.global.CardRegistrar;
import com.qiufengguang.ajstudy.global.Constant;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 卡片基类
 * [性能专家重构]：支持按需动态注册 Creator，降低启动负载
 *
 * @author qiufengguang
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
     * 获取创建器
     * [性能重构点]：如果当前未注册，则触发 CardRegistrar 进行局部按需注册
     *
     * @param layoutId 卡片id
     * @return ViewHolderCreator
     */
    public static CardCreator getCreator(int layoutId) {
        CardCreator creator = CREATOR.get(layoutId);
        if (creator == null) {
            // 按需延迟加载
            CardRegistrar.registerOnDemand(layoutId);
            creator = CREATOR.get(layoutId);
        }
        return creator;
    }

    /**
     * 获取卡片占用列数配置
     */
    public static Map<Integer, Integer> getSpanSizeMap(int pln4, int pln8, int pln12) {
        return Map.of(
            Constant.Grid.COLUMN_DEFAULT, pln4,
            Constant.Grid.COLUMN_8, pln8,
            Constant.Grid.COLUMN_12, pln12
        );
    }

    public static Map<Integer, Integer> getSpanSizeMap(int pln) {
        return getSpanSizeMap(pln, pln, pln);
    }

    public static Map<Integer, Integer> getSpanSizeMap(int pln4, int pln8) {
        return getSpanSizeMap(pln4, pln8, pln8);
    }
}