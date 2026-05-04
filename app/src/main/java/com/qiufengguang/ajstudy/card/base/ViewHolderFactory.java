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
        if (creator.isMinHeightEnable() && holder != null) {
            setMinHeight(holder, parent.getMeasuredHeight());
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
        if (params instanceof StaggeredGridLayoutManager.LayoutParams layoutParams) {
            layoutParams.setFullSpan(true);
        }
    }

    /**
     * 设置卡片最小高度
     *
     * @param holder             BaseViewHolder
     * @param recyclerViewHeight RecyclerView测量高度
     */
    private static void setMinHeight(@NonNull BaseViewHolder<?> holder, int recyclerViewHeight) {
        int minimumHeight = holder.itemView.getMinimumHeight();
        if (minimumHeight == 0 || minimumHeight < recyclerViewHeight) {
            return;
        }
        ViewGroup.LayoutParams params = holder.itemView.getLayoutParams();
        params.height = minimumHeight;
        holder.itemView.setLayoutParams(params);
    }
}
