package com.qiufengguang.ajstudy.card.banner;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 轮播banner间距
 *
 * @author qiufengguang
 * @since 2026/1/2 17:23
 */
public class BannerDecoration extends RecyclerView.ItemDecoration {
    /**
     * 水平间距
     */
    private final int horizontalSpacing;
    /**
     * 垂直间距
     */
    private final int verticalSpacing;

    public BannerDecoration(int horizontalSpacing, int verticalSpacing) {
        this.horizontalSpacing = horizontalSpacing / 2;
        this.verticalSpacing = verticalSpacing / 2;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent,
        @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        long itemCount = state.getItemCount();
        if (position == RecyclerView.NO_POSITION || itemCount <= 0) {
            return;
        }
        outRect.left = horizontalSpacing;
        outRect.right = horizontalSpacing;
        outRect.top = verticalSpacing;
        outRect.bottom = verticalSpacing;
    }
}