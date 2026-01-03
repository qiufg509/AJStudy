package com.qiufengguang.ajstudy.fragment.recommendation;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.utils.DisplayMetricsHelper;

/**
 * 详情页-推荐子页面列表分割线
 *
 * @author qiufengguang
 * @since 2025/12/12 19:13
 */
public class RecommendationItemDecoration extends RecyclerView.ItemDecoration {

    private final Paint paint;
    private final int startMarginPx;
    private final int endMarginPx;
    private final int dividerHeightPx;

    public RecommendationItemDecoration(Context context) {
        paint = new Paint();
        int color = ContextCompat.getColor(context, R.color.ajstudy_color_divider);
        paint.setColor(color); // 分割线颜色，可根据需要调整
        paint.setStyle(Paint.Style.FILL);

        startMarginPx = DisplayMetricsHelper.dp2px(context, 76);
        endMarginPx = context.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_l);
        dividerHeightPx = 1;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
        @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);

        RecyclerView.Adapter<?> adapter = parent.getAdapter();
        if (adapter == null) {
            return;
        }
        int position = parent.getChildAdapterPosition(view);
        // 最后一个item不加分割线
        if (position < adapter.getItemCount() - 1) {
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

            // 最后一个item不绘制分割线
            if (position == itemCount - 1) {
                continue;
            }

            int left = parent.getPaddingLeft() + startMarginPx;
            int right = parent.getWidth() - parent.getPaddingRight() - endMarginPx;
            int top = child.getBottom();
            int bottom = top + dividerHeightPx;

            canvas.drawRect(left, top, right, bottom, paint);
        }
    }
}