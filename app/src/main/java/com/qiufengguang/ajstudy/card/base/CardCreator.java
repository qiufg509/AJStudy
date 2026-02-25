package com.qiufengguang.ajstudy.card.base;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.qiufengguang.ajstudy.global.Constant;

import java.util.Map;

/**
 * 创建卡片工厂方法接口
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
    default Map<Integer, Integer> getSpanSize() {
        return Card.getSpanSizeMap(Constant.Pln.DEF_4);
    }

    /**
     * 卡片是否需要监听 onResume 和 onPause（默认不需要）
     *
     * @return true需要 false不需要
     */
    default boolean needObserveLifecycle() {
        return false;
    }

    /**
     * 在瀑布流中占满整屏宽度
     *
     * @param holder BaseViewHolder
     */
    default void setFullSpanInStaggeredPage(BaseViewHolder<?> holder) {
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if (params instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) params;
            layoutParams.setFullSpan(true);
        }
    }
}
