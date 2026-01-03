package com.qiufengguang.ajstudy.card.grid;

import android.content.Context;

import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.data.GridCardBean;

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

    private Context context;

    private RecyclerView recyclerView;

    private GridCardAdapter adapter;

    private int spanCount;

    @IntRange(from = 0, to = 1)
    private int itemType;

    private int horizontalSpacing;

    private int verticalSpacing;

    private boolean includeEdge;

    private GridCardAdapter.OnItemClickListener listener;

    public void setData(List<GridCardBean> beans) {
        if (adapter == null) {
            adapter = new GridCardAdapter(itemType, null);
        }
        adapter.setData(beans);
    }

    public void show() {
        GridLayoutManager layoutManager = new GridLayoutManager(context, spanCount);
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
        private final Context context;

        private RecyclerView recyclerView;

        private int spanCount;

        private int itemType;

        private int horizontalSpacing;

        private int verticalSpacing;

        private int spacing;

        private boolean includeEdge;

        private GridCardAdapter.OnItemClickListener listener;

        public Builder(@NonNull Context context) {
            this.context = context;
        }

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
            GridCardWrapper wrapper = new GridCardWrapper();
            wrapper.context = this.context;
            wrapper.recyclerView = this.recyclerView;
            wrapper.spanCount = this.spanCount;
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
        if (recyclerView != null) {
            recyclerView = null;
        }
        if (this.context != null) {
            this.context = null;
        }
    }
}
