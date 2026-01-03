package com.qiufengguang.ajstudy.card.grid;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 格网列表间距
 *
 * @author qiufengguang
 * @since 2025/12/28 17:42
 */
public class GridCardDecoration extends RecyclerView.ItemDecoration {
    /**
     * 网格列数
     */
    private final int spanCount;
    /**
     * 水平间距
     */
    private final int horizontalSpacing;
    /**
     * 垂直间距
     */
    private final int verticalSpacing;
    /**
     * 是否包含边缘间距
     */
    private final boolean includeEdge;

    public GridCardDecoration(int spanCount, int horizontalSpacing,
        int verticalSpacing, boolean includeEdge) {
        this.spanCount = spanCount;
        this.horizontalSpacing = horizontalSpacing;
        this.verticalSpacing = verticalSpacing;
        this.includeEdge = includeEdge;
    }

    public GridCardDecoration(int spanCount, int horizontalSpacing, int verticalSpacing) {
        this(spanCount, horizontalSpacing, verticalSpacing, false);
    }

    public GridCardDecoration(int spanCount, int spacing, boolean includeEdge) {
        this(spanCount, spacing, spacing, includeEdge);
    }

    public GridCardDecoration(int spanCount, int spacing) {
        this(spanCount, spacing, spacing, false);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent,
        @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        if (position == RecyclerView.NO_POSITION) {
            return;
        }

        if (!(parent.getLayoutManager() instanceof GridLayoutManager)) {
            return;
        }

        int column = position % spanCount;
        int totalItemCount = state.getItemCount();
        int rowCount = (totalItemCount + spanCount - 1) / spanCount;
        int currentRow = position / spanCount;

        if (includeEdge) {
            outRect.left = horizontalSpacing - column * horizontalSpacing / spanCount;
            outRect.right = (column + 1) * horizontalSpacing / spanCount;
            // 垂直间距处理
            if (currentRow == 0) {
                outRect.top = verticalSpacing;
            }
            outRect.bottom = (currentRow == rowCount - 1) ? verticalSpacing : 0;
        } else {
            outRect.left = column * horizontalSpacing / spanCount;
            outRect.right = horizontalSpacing - (column + 1) * horizontalSpacing / spanCount;
        }
        // 如果不是最后一行，添加垂直间距
        if (currentRow < rowCount - 1) {
            outRect.bottom = verticalSpacing;
        }
    }
}