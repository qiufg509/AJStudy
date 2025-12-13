package com.qiufengguang.ajstudy.view;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {
    private static final int START_PAGE_INDEX = 0;

    /**
     * 提前加载的阈值（倒数第几条）
     */
    private static final int VISIBLE_THRESHOLD = 2;
    private int currentPage = 0;
    private int previousTotalItemCount = 0;
    private boolean loading = true;

    private final LinearLayoutManager layoutManager;

    public EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
    }

    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if (dy <= 0) return; // 只处理向上滑动

        int totalItemCount = layoutManager.getItemCount();
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();

        // 如果数据为空且之前有数据，重置状态
        if (totalItemCount < previousTotalItemCount) {
            this.currentPage = START_PAGE_INDEX;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0) {
                this.loading = true;
            }
        }

        // 如果正在加载且数据数量发生变化，更新状态
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // 判断是否需要加载更多
        if (!loading && (lastVisibleItemPosition + VISIBLE_THRESHOLD) >= totalItemCount) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount, recyclerView);
            loading = true;
        }
    }

    public abstract void onLoadMore(int page, int totalItemsCount, RecyclerView view);

    // 重置状态
    public void resetState() {
        this.currentPage = START_PAGE_INDEX;
        this.previousTotalItemCount = 0;
        this.loading = true;
    }
}