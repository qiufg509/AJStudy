package com.qiufengguang.ajstudy.fragment.knowhow;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;

/**
 * 知识列表页列表分割线
 *
 * @author qiufengguang
 * @since 2026/1/3 19:05
 */
public class KnowHowItemDecoration extends RecyclerView.ItemDecoration {

    private final Paint paint;
    private final int perLineNumber;
    private final int dividerHeightPx;

    public KnowHowItemDecoration(Context context, int perLineNumber) {
        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.ajstudy_color_divider);
        paint.setColor(color); // 分割线颜色，可根据需要调整
        paint.setStyle(Paint.Style.FILL);

        this.perLineNumber = perLineNumber;
        dividerHeightPx = 1;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
        @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        int position = parent.getChildAdapterPosition(view);
        RecyclerView.Adapter<?> adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }
        // 最后一个perLineNumber不加分割线
        if (position < adapter.getItemCount() - perLineNumber) {
            outRect.bottom = dividerHeightPx;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas canvas, @NonNull RecyclerView parent,
        @NonNull RecyclerView.State state) {
        super.onDraw(canvas, parent, state);

        int childCount = parent.getChildCount();
        RecyclerView.Adapter<?> adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }
        int itemCount = adapter.getItemCount();

        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            int position = parent.getChildAdapterPosition(child);

            // 判断是否是最后perLineNumber个item
            if (position >= itemCount - perLineNumber) {
                continue;
            }

            int left = parent.getPaddingLeft();
            int right = parent.getWidth() - parent.getPaddingRight();
            int top = child.getBottom();
            int bottom = top + dividerHeightPx;

            canvas.drawRect(left, top, right, bottom, paint);
        }
    }
}