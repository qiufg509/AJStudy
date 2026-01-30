package com.qiufengguang.ajstudy.card.base;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewbinding.ViewBinding;

import java.util.Map;

/**
 * 创建ViewHolder工厂方法接口
 *
 * @author qiufengguang
 * @since 2026/1/30 17:11
 */
public interface CardCreator {
    /**
     * 构造卡片ViewHolder
     *
     * @param parent         卡片父容器
     * @param lifecycleOwner LifecycleOwner
     * @return BaseViewHolder
     */
    BaseViewHolder<? extends ViewBinding> create(
        @NonNull ViewGroup parent,
        LifecycleOwner lifecycleOwner
    );

    /**
     * 4/8/12栅格下卡片占用大小
     *
     * @return 卡片占用大小集合（key为栅格数，value为占用大小）
     */
    Map<Integer, Integer> getSpanSize();
}
