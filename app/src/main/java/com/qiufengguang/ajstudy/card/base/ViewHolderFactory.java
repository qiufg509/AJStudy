package com.qiufengguang.ajstudy.card.base;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.qiufengguang.ajstudy.card.empty.EmptyCard;

/**
 * 卡片ViewHolder工厂方法
 *
 * @author qiufengguang
 * @since 2026/1/29 13:00
 */
public class ViewHolderFactory {

    /**
     * 创建ViewHolder
     *
     * @param parent         卡片父控件
     * @param viewType       卡片id {\\@link XxxCard#LAYOUT_ID}
     * @param lifecycleOwner LifecycleOwner
     * @return BaseViewHolder
     */
    public static BaseViewHolder<? extends ViewBinding> createViewHolder(
        @NonNull ViewGroup parent,
        int viewType,
        LifecycleOwner lifecycleOwner
    ) {
        CardCreator creator = Card.getCreator(viewType);
        if (creator == null) {
            creator = new EmptyCard.Creator();
        }
        BaseViewHolder<? extends ViewBinding> holder = creator.create(parent, lifecycleOwner);
        if (creator.isFullSpanInStaggeredPage() && holder != null) {
            setFullSpanInStaggeredPage(holder);
        }
        return holder;
    }

    /**
     * 在瀑布流中占满整屏宽度
     *
     * @param holder BaseViewHolder
     */
    private static void setFullSpanInStaggeredPage(@NonNull BaseViewHolder<?> holder) {
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        if (params instanceof StaggeredGridLayoutManager.LayoutParams) {
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) params;
            layoutParams.setFullSpan(true);
        }
    }
}
