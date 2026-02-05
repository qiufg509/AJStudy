package com.qiufengguang.ajstudy.card.base;

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
public class GridDecoration extends RecyclerView.ItemDecoration {
    /**
     * 网格列数
     */
    private final int spanCount;
    /**
     * 水平间距（item间距）
     */
    private final int horizontalSpacing;
    /**
     * 垂直间距（行间距）
     */
    private final int verticalSpacing;
    /**
     * 起始边间距（左边或右边，取决于布局方向）
     */
    private final int startSpacing;
    /**
     * 结束边间距（右边或左边，取决于布局方向）
     */
    private final int endSpacing;
    /**
     * 顶部间距
     */
    private final int topSpacing;
    /**
     * 底部间距
     */
    private final int bottomSpacing;

    public GridDecoration(Builder builder) {
        if (builder.spanCount <= 0) {
            throw new IllegalArgumentException("spanCount must be greater than 0.");
        }
        this.spanCount = builder.spanCount;
        this.horizontalSpacing = builder.horizontalSpacing;
        this.verticalSpacing = builder.verticalSpacing;
        this.startSpacing = builder.startSpacing;
        this.endSpacing = builder.endSpacing;
        this.topSpacing = builder.topSpacing;
        this.bottomSpacing = builder.bottomSpacing;
    }

    @Override
    public void getItemOffsets(
        @NonNull Rect outRect,
        @NonNull View view,
        RecyclerView parent,
        @NonNull RecyclerView.State state
    ) {
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

        // 计算水平间距
        int leftSpacing;
        int rightSpacing;

        if (spanCount == 1) {
            // 只有一列时，左右使用完整的间距
            leftSpacing = startSpacing;
            rightSpacing = endSpacing;
        } else {
            // 多列时，第一列左侧使用起始间距，其他列左侧使用水平间距的一半
            leftSpacing = column == 0 ? startSpacing : horizontalSpacing / 2;

            // 最后一列右侧使用结束间距，其他列右侧使用水平间距的一半
            rightSpacing = column == spanCount - 1 ? endSpacing : horizontalSpacing / 2;

            // 对于中间列，左右各一半水平间距
            if (column > 0 && column < spanCount - 1) {
                leftSpacing = horizontalSpacing / 2;
                rightSpacing = horizontalSpacing / 2;
            }
        }

        // 计算垂直间距
        int topSpacingValue;
        int bottomSpacingValue;

        if (rowCount == 1) {
            // 只有一行时，上下使用完整的间距
            topSpacingValue = topSpacing;
            bottomSpacingValue = bottomSpacing;
        } else {
            // 第一行顶部使用顶部间距，其他行顶部使用垂直间距的一半
            topSpacingValue = currentRow == 0 ? topSpacing : verticalSpacing / 2;

            // 最后一行底部使用底部间距，其他行底部使用垂直间距的一半
            bottomSpacingValue = currentRow == rowCount - 1 ? bottomSpacing : verticalSpacing / 2;

            // 对于中间行，上下各一半垂直间距
            if (currentRow > 0 && currentRow < rowCount - 1) {
                topSpacingValue = verticalSpacing / 2;
                bottomSpacingValue = verticalSpacing / 2;
            }
        }

        // 设置间距
        outRect.left = leftSpacing;
        outRect.right = rightSpacing;
        outRect.top = topSpacingValue;
        outRect.bottom = bottomSpacingValue;
    }

    /**
     * Builder 类用于创建 GridDecoration
     */
    public static class Builder {
        private int spanCount;
        private int horizontalSpacing = 0;
        private int verticalSpacing = 0;
        private int startSpacing = 0;
        private int endSpacing = 0;
        private int topSpacing = 0;
        private int bottomSpacing = 0;

        public Builder() {
        }

        /**
         * 构造函数，传入网格列数
         *
         * @param spanCount 网格列数
         */
        public Builder(int spanCount) {
            this.spanCount = spanCount;
        }

        /**
         * 设置格网列数
         *
         * @param spanCount 列数
         */
        public void setSpanCount(int spanCount) {
            this.spanCount = spanCount;
        }

        /**
         * 设置item水平和垂直间距（相同值）
         *
         * @param spacing 间距值
         * @return Builder
         */
        public Builder spacing(int spacing) {
            this.horizontalSpacing = spacing;
            this.verticalSpacing = spacing;
            return this;
        }

        /**
         * 设置水平间距
         *
         * @param horizontalSpacing 水平间距
         * @return Builder
         */
        public Builder horizontalSpacing(int horizontalSpacing) {
            this.horizontalSpacing = horizontalSpacing;
            return this;
        }

        /**
         * 设置垂直间距
         *
         * @param verticalSpacing 垂直间距
         * @return Builder
         */
        public Builder verticalSpacing(int verticalSpacing) {
            this.verticalSpacing = verticalSpacing;
            return this;
        }

        /**
         * 设置左边间距
         *
         * @param startSpacing 起始边间距
         * @return Builder
         */
        public Builder startSpacing(int startSpacing) {
            this.startSpacing = startSpacing;
            return this;
        }

        /**
         * 设置右边间距
         *
         * @param endSpacing 结束边间距
         * @return Builder
         */
        public Builder endSpacing(int endSpacing) {
            this.endSpacing = endSpacing;
            return this;
        }

        /**
         * 设置左右边间距（相同的值）
         *
         * @param sideSpacing 左右边间距
         * @return Builder
         */
        public Builder sideSpacing(int sideSpacing) {
            this.startSpacing = sideSpacing;
            this.endSpacing = sideSpacing;
            return this;
        }

        /**
         * 设置顶部间距
         *
         * @param topSpacing 顶部间距
         * @return Builder
         */
        public Builder topSpacing(int topSpacing) {
            this.topSpacing = topSpacing;
            return this;
        }

        /**
         * 设置底部间距
         *
         * @param bottomSpacing 底部间距
         * @return Builder
         */
        public Builder bottomSpacing(int bottomSpacing) {
            this.bottomSpacing = bottomSpacing;
            return this;
        }

        /**
         * 设置上下间距（相同的值）
         *
         * @param verticalEdgeSpacing 上下间距
         * @return Builder
         */
        public Builder verticalEdgeSpacing(int verticalEdgeSpacing) {
            this.topSpacing = verticalEdgeSpacing;
            this.bottomSpacing = verticalEdgeSpacing;
            return this;
        }

        /**
         * 创建 GridDecoration 实例
         *
         * @return GridDecoration
         */
        public GridDecoration build() {
            return new GridDecoration(this);
        }
    }
}