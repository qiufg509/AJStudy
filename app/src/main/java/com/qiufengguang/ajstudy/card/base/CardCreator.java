package com.qiufengguang.ajstudy.card.base;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.qiufengguang.ajstudy.fragment.base.BaseListFragment;
import com.qiufengguang.ajstudy.fragment.base.BaseStaggeredFragment;
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
     * 卡片内容是否延伸适配到页面左右间距范围内
     * 支持卡片在{@link BaseListFragment}页面展示
     * 以及卡片在{@link com.qiufengguang.ajstudy.fragment.base.BaseGridFragment}页面
     * getSpanSize4/8/12栅格均为Constant.Pln.DEF_4时使用
     * 因为{@link BaseStaggeredFragment}页面边距通过补偿处理，以及BaseGridFragment多栅格下非Constant.Pln.DEF_4时左右间距不对称，此方法不适用
     *
     * @return true延伸 false不延伸
     */
    default boolean isFitToMargin() {
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
