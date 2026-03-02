package com.qiufengguang.ajstudy.card.screenshot;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 截图卡间距
 *
 * @author qiufengguang
 * @since 2026/3/2 11:40
 */
public class ScreenshotDecoration extends RecyclerView.ItemDecoration {
    /**
     * 水平间距
     */
    private final int horizontalSpacing;

    public ScreenshotDecoration(int horizontalSpacing) {
        this.horizontalSpacing = horizontalSpacing / 2;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, RecyclerView parent,
                               @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        long itemCount = state.getItemCount();
        if (position == RecyclerView.NO_POSITION || itemCount <= 0) {
            return;
        }
        if (position == 0) {
            outRect.left = 0;
            outRect.right = horizontalSpacing / 2;
        } else if (position == itemCount - 1) {
            outRect.left = horizontalSpacing / 2;
            outRect.right = 0;
        } else {
            outRect.left = horizontalSpacing / 2;
            outRect.right = horizontalSpacing / 2;
        }
        outRect.top = 0;
        outRect.bottom = 0;
    }
}