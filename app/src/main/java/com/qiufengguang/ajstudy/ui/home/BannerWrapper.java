package com.qiufengguang.ajstudy.ui.home;

import android.os.Handler;
import android.os.Looper;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.BannerBean;

import java.util.List;

/**
 * 轮播banner组件
 *
 * @author qiufengguang
 * @since 2025/12/19 16:33
 */
public class BannerWrapper {

    /**
     * 自动轮播滚动间隔4秒
     */
    private static final long AUTO_SCROLL_DELAY = 4000L;

    private RecyclerView recyclerBanner;

    private LinearLayout indicatorContainer;

    private BannerAdapter adapter;

    private Handler autoScrollHandler;

    private Runnable autoScrollRunnable;

    /**
     * 是否自动轮播滚动
     */
    private boolean isAutoScrolling = true;

    /**
     * 是否用户拖拽滚动操作中
     */
    private boolean isUserScrolling = false;

    /**
     * 设置初始位置到中间，实现无限循环
     */
    private int currentPosition = Integer.MAX_VALUE / 2;

    public BannerWrapper(@NonNull RecyclerView recyclerBanner, LinearLayout indicatorContainer,
        BannerAdapter.OnBannerClickListener clickListener) {
        this.recyclerBanner = recyclerBanner;
        this.indicatorContainer = indicatorContainer;

        setupBanner(clickListener);
    }

    /**
     * 设置轮播数据
     *
     * @param bannerBeans List<BannerBean>
     */
    public void setBannerBeans(List<BannerBean> bannerBeans) {
        adapter.setBannerBeans(bannerBeans);
        // 初始化指示器
        int size = bannerBeans == null ? 0 : bannerBeans.size();
        setupIndicator(size);
        // 调整位置到第一条数据上
        currentPosition += size - currentPosition % size;
        recyclerBanner.scrollToPosition(currentPosition);
    }

    private void setupBanner(BannerAdapter.OnBannerClickListener clickListener) {
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerBanner.getContext(),
            LinearLayoutManager.HORIZONTAL, false);
        recyclerBanner.setLayoutManager(layoutManager);

        // 配置SnapHelper实现页面吸附效果
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerBanner);

        // 设置适配器
        adapter = new BannerAdapter();
        recyclerBanner.setAdapter(adapter);

        // 启用预取功能
        recyclerBanner.setItemViewCacheSize(3);
        recyclerBanner.setHasFixedSize(true);

        // 设置滚动监听
        setupScrollListener();

        // 处理Banner点击事件
        adapter.setOnBannerClickListener(clickListener);
    }

    private void setupScrollListener() {
        recyclerBanner.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                int action = e.getAction();
                if (action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_MOVE) {
                    pauseAutoScroll();
                } else {
                    resumeAutoScroll();
                }
                return super.onInterceptTouchEvent(rv, e);
            }
        });
        recyclerBanner.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        // 获取当前显示的第一个可见项位置
                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        if (layoutManager != null) {
                            int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
                            int firstCompletelyVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();
                            // 对于轮播图，通常取第一个完全可见项作为当前页,如果没有完全可见项，取第一个可见项
                            int position = firstCompletelyVisiblePosition != -1
                                ? firstCompletelyVisiblePosition : firstVisiblePosition;
                            if (currentPosition != position) {
                                currentPosition = position;
                                updateIndicator(currentPosition);
                            }
                        }
                        // 滚动停止，恢复自动滚动
                        isUserScrolling = false;
                        resumeAutoScroll();
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                        // 用户拖拽，暂停自动滚动
                        isUserScrolling = true;
                        pauseAutoScroll();
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        // 惯性滚动中
                        break;
                }
            }
        });
    }

    private void setupIndicator(int size) {
        if (indicatorContainer == null || size <= 0) {
            return;
        }
        indicatorContainer.removeAllViews();
        int indicatorSize = indicatorContainer.getResources().getDimensionPixelSize(R.dimen.banner_indicator_size);
        int indicatorMargin = indicatorContainer.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_xs);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
            indicatorSize, indicatorSize
        );
        params.setMargins(indicatorMargin, 0, indicatorMargin, 0);
        for (int index = 0; index < size; index++) {
            ImageView indicator = new ImageView(indicatorContainer.getContext());
            indicator.setLayoutParams(params);
            indicator.setImageResource(R.drawable.banner_indicator_inactive);
            indicator.setAlpha(0.5f);
            indicatorContainer.addView(indicator);
        }
        // 更新指示器状态
        updateIndicator(0);
    }

    private void updateIndicator(int currentPosition) {
        if (indicatorContainer == null) {
            return;
        }
        int realPosition = adapter.getRealPosition(currentPosition);
        for (int index = 0, sum = indicatorContainer.getChildCount(); index < sum; index++) {
            ImageView indicator = (ImageView) indicatorContainer.getChildAt(index);
            if (index == realPosition) {
                indicator.setImageResource(R.drawable.banner_indicator_active);
                indicator.setAlpha(1.0f);
            } else {
                indicator.setImageResource(R.drawable.banner_indicator_inactive);
                indicator.setAlpha(0.5f);
            }
        }
    }

    /**
     * 开启自动轮播
     */
    public void startAutoScroll() {
        if (!isAutoScrolling || isUserScrolling || adapter.getRealItemCount() <= 0) {
            return;
        }
        autoScrollHandler = new Handler(Looper.getMainLooper());
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (!isAutoScrolling || isUserScrolling) {
                    return;
                }
                // 计算下一个位置
                currentPosition = (currentPosition + 1) % (Integer.MAX_VALUE);

                // 平滑滚动到下一个位置
                recyclerBanner.smoothScrollToPosition(currentPosition);
                updateIndicator(currentPosition);

                // 重新安排下一次滚动
                autoScrollHandler.postDelayed(this, AUTO_SCROLL_DELAY);
            }
        };

        // 开始自动滚动
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }

    /**
     * 暂停自动轮播
     * 页面onPause时调用
     */
    public void pauseAutoScroll() {
        if (isAutoScrolling) {
            isAutoScrolling = false;
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }

    /**
     * 恢复自动轮播
     * 页面onResume时调用
     */
    public void resumeAutoScroll() {
        if (!isAutoScrolling && !isUserScrolling) {
            isAutoScrolling = true;
            autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
        }
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        if (autoScrollHandler != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
        if (recyclerBanner != null) {
            recyclerBanner = null;
        }
        if (indicatorContainer != null) {
            indicatorContainer = null;
        }
    }
}
