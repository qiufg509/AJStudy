package com.qiufengguang.ajstudy.card.grid;

import androidx.annotation.IntRange;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.GridCardBean;
import com.qiufengguang.ajstudy.global.Constant;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 格网卡片
 *
 * @author qiufengguang
 * @since 2025/12/28 18:50
 */
public class GridCardWrapper {
    /**
     * item使用TextView，上icon下文字样式
     */
    public static final int TYPE_TEXT = 0;
    /**
     * item使用ImageView，圆形点中打勾样式
     */
    public static final int TYPE_IMAGE = 1;

    private WeakReference<RecyclerView> recyclerViewRef;

    private GridCardAdapter adapter;

    private int spanCount;

    @IntRange(from = 0, to = 1)
    private int itemType;

    private int horizontalSpacing;

    private int verticalSpacing;

    private boolean includeEdge;

    private GridCardAdapter.OnItemClickListener listener;

    private GridCardWrapper() {
    }

    public void setData(List<GridCardBean> beans) {
        if (adapter == null) {
            adapter = new GridCardAdapter(itemType, null);
        }
        adapter.setData(beans);
    }

    public void show() {
        if (recyclerViewRef == null) {
            return;
        }
        RecyclerView recyclerView = recyclerViewRef.get();
        if (recyclerView == null) {
            return;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(recyclerView.getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        if (adapter == null) {
            adapter = new GridCardAdapter(itemType, null);
        }
        adapter.setOnItemClickListener(listener);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new GridCardDecoration(spanCount,
            horizontalSpacing, verticalSpacing, includeEdge));
    }

    public static class Builder {
        private RecyclerView recyclerView;

        private int spanCount;

        private int itemType;

        private int horizontalSpacing;

        private int verticalSpacing;

        private int spacing;

        private boolean includeEdge;

        private GridCardAdapter.OnItemClickListener listener;

        /**
         * 设置格网卡片布局控件
         *
         * @param recyclerView RecyclerView
         * @return Builder
         */
        public GridCardWrapper.Builder setRecyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return this;
        }

        /**
         * 设置格网卡片列数
         *
         * @param spanCount 列数
         * @return Builder
         */
        public GridCardWrapper.Builder setSpanCount(int spanCount) {
            this.spanCount = spanCount;
            return this;
        }

        /**
         * 设置格网卡片item类型
         *
         * @param itemType 0-TextView展示，上边icon，下边文字；1-ImageView展示
         * @return Builder
         */
        public GridCardWrapper.Builder setItemType(@IntRange(from = 0, to = 1) int itemType) {
            this.itemType = itemType;
            return this;
        }

        /**
         * 设置格网卡片水平间距
         *
         * @param horizontalSpacing 水平间距大小
         * @return Builder
         */
        public GridCardWrapper.Builder setHorizontalSpacing(int horizontalSpacing) {
            this.horizontalSpacing = horizontalSpacing;
            return this;
        }

        /**
         * 设置格网卡片垂直间距
         *
         * @param verticalSpacing 垂直间距大小
         * @return Builder
         */
        public GridCardWrapper.Builder setVerticalSpacing(int verticalSpacing) {
            this.verticalSpacing = verticalSpacing;
            return this;
        }

        /**
         * 设置格网卡片间距
         *
         * @param spacing 间距大小
         * @return Builder
         */
        public GridCardWrapper.Builder setSpacing(int spacing) {
            this.spacing = spacing;
            return this;
        }

        /**
         * 设置格网卡片间距是否包含边距
         *
         * @param includeEdge 默认false不包含
         * @return Builder
         */
        public GridCardWrapper.Builder setIncludeEdge(boolean includeEdge) {
            this.includeEdge = includeEdge;
            return this;
        }

        /**
         * 设置格网卡片item点击事件
         *
         * @param listener GridCardAdapter.OnItemClickListener
         * @return Builder
         */
        public GridCardWrapper.Builder setListener(GridCardAdapter.OnItemClickListener listener) {
            this.listener = listener;
            return this;
        }

        public GridCardWrapper create() {
            if (this.recyclerView == null) {
                throw new UnsupportedOperationException(
                    "recyclerView is null, call setRecyclerView first.");
            }
            GridCardWrapper wrapper = new GridCardWrapper();
            wrapper.recyclerViewRef = new WeakReference<>(this.recyclerView);
            if (this.spanCount > 0) {
                wrapper.spanCount = this.spanCount;
            } else {
                int column = this.recyclerView.getResources().getInteger(R.integer.ajstudy_column_count);
                wrapper.spanCount = column == Constant.Grid.COLUMN_DEFAULT ? Constant.Pln.GRID_4 :
                    (column == Constant.Grid.COLUMN_8 ? Constant.Pln.GRID_8 : Constant.Pln.GRID_12);
            }
            wrapper.itemType = this.itemType;
            wrapper.horizontalSpacing = this.horizontalSpacing == 0 && this.spacing != 0
                ? this.spacing : this.horizontalSpacing;
            wrapper.verticalSpacing = this.verticalSpacing == 0 && this.spacing != 0
                ? this.spacing : this.verticalSpacing;
            wrapper.includeEdge = this.includeEdge;
            wrapper.listener = this.listener;
            return wrapper;
        }
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        if (recyclerViewRef != null) {
            RecyclerView recyclerBanner = recyclerViewRef.get();
            if (recyclerBanner != null) {
                recyclerBanner.setAdapter(null);
                recyclerViewRef.clear();
            }
            recyclerViewRef = null;
        }
        if (adapter != null) {
            adapter.setOnItemClickListener(null);
            adapter = null;
        }
    }
}
