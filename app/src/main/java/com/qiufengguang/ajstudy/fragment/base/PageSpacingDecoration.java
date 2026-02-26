package com.qiufengguang.ajstudy.fragment.base;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.global.Constant;

/**
 * 页面动态网格间距装饰器
 * 容器边缘16dp，item之间8dp
 *
 * @author qiufengguang
 * @since 2026/2/6 22:36
 */
public class PageSpacingDecoration extends RecyclerView.ItemDecoration {
    private final int edgeSpacing;
    private final int interItemSpacing;

    public PageSpacingDecoration(Context context) {
        this.edgeSpacing = context.getResources().getDimensionPixelSize(
            R.dimen.activity_horizontal_margin_l);
        this.interItemSpacing = context.getResources().getDimensionPixelSize(
            R.dimen.activity_horizontal_margin_s);
    }

    @Override
    public void getItemOffsets(
        @NonNull Rect outRect,
        @NonNull View view,
        @NonNull RecyclerView parent,
        @NonNull RecyclerView.State state
    ) {
        int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) {
            return;
        }
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            // 格网页面 {@link com.qiufengguang.ajstudy.fragment.base.BaseGridFragment}
            GridLayoutManager manager = (GridLayoutManager) layoutManager;
            int spanCount = manager.getSpanCount();
            GridLayoutManager.SpanSizeLookup spanSizeLookup = manager.getSpanSizeLookup();

            int spanSize = spanSizeLookup.getSpanSize(position);
            int spanIndex = spanSizeLookup.getSpanIndex(position, spanCount);

            // 处理跨列item
            if (spanSize != Constant.Pln.DEF_4) {
                handleFullSpanItem(outRect, spanIndex, spanSize, spanCount);
            } else {
                handleNormalItem(outRect, spanIndex, spanCount);
            }
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            // 瀑布流页面 {@link com.qiufengguang.ajstudy.fragment.base.BaseStaggeredFragment}
            // 结合 {@link BaseStaggeredFragment.setupContent} 左右补偿间距实现
            int quarterSpacing = edgeSpacing / 4;
            outRect.left = quarterSpacing;
            outRect.right = quarterSpacing;
            outRect.top = 0;
            outRect.bottom = 0;
        } else {
            // 列表页面 {@link com.qiufengguang.ajstudy.fragment.base.BaseListFragment}
            outRect.left = edgeSpacing;
            outRect.right = edgeSpacing;
            outRect.top = 0;
            outRect.bottom = 0;
        }
    }

    /**
     * 处理普通item（占1列）
     */
    private void handleNormalItem(Rect outRect, int spanIndex, int spanCount) {
        // 左边距
        if (spanIndex == 0) {
            // 第一列：左边缘间距
            outRect.left = edgeSpacing;
        } else {
            // 其他列：一半的item间距
            outRect.left = interItemSpacing / 2;
        }

        // 右边距
        if (spanIndex == spanCount - 1) {
            // 最后一列：右边缘间距
            outRect.right = edgeSpacing;
        } else {
            // 其他列：一半的item间距
            outRect.right = interItemSpacing / 2;
        }

        // 垂直间距由item自己处理，这里设为0
        outRect.top = 0;
        outRect.bottom = 0;
    }

    /**
     * 处理跨列item
     */
    private void handleFullSpanItem(Rect outRect, int spanIndex, int spanSize, int spanCount) {
        // 跨列item占据整行
        if (spanSize == spanCount) {
            outRect.left = edgeSpacing;
            outRect.right = edgeSpacing;
        } else {
            // 跨多列但未满一行
            int spanEnd = spanIndex + spanSize - 1;

            // 左边距
            if (spanIndex == 0) {
                outRect.left = edgeSpacing;
            } else {
                outRect.left = interItemSpacing / 2;
            }

            // 右边距
            if (spanEnd == spanCount - 1) {
                outRect.right = edgeSpacing;
            } else {
                outRect.right = interItemSpacing / 2;
            }
        }

        // 垂直间距由item自己处理
        outRect.top = 0;
        outRect.bottom = 0;
    }
}