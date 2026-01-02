package com.qiufengguang.ajstudy.card.banner;

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

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 轮播banner卡片
 *
 * @author qiufengguang
 * @since 2025/12/19 16:33
 */
public class BannerWrapper {

    /**
     * 自动轮播滚动间隔4秒
     */
    private static final long AUTO_SCROLL_DELAY = 4000L;

    private WeakReference<RecyclerView> recyclerBannerRef;

    private WeakReference<LinearLayout> indicatorContainerRef;

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

    /**
     * 上一次指示器位置，用于避免频繁刷新
     */
    private int lastIndicatorPosition = -1;

    /**
     * 指示器刷新防抖动最小更新间隔（毫秒）
     */
    private static final long INDICATOR_UPDATE_THROTTLE_MS = 100L;

    private long lastIndicatorUpdateTime = 0;

    public BannerWrapper(@NonNull RecyclerView recyclerBanner, LinearLayout indicatorContainer,
        BannerAdapter.OnBannerClickListener clickListener) {
        this.recyclerBannerRef = new WeakReference<>(recyclerBanner);
        this.indicatorContainerRef = new WeakReference<>(indicatorContainer);

        setupBanner(clickListener);
    }

    /**
     * 设置轮播数据
     *
     * @param bannerBeans List<BannerBean>
     */
    public void setBannerBeans(List<BannerBean> bannerBeans) {
        if (bannerBeans == null || bannerBeans.isEmpty()) {
            return;
        }
        adapter.setBannerBeans(bannerBeans);
        // 初始化指示器
        int size = bannerBeans.size();
        setupIndicator(size);
        // 调整位置到第一条数据上
        currentPosition += size - currentPosition % size;
        if (recyclerBannerRef == null) {
            return;
        }
        RecyclerView recyclerBanner = recyclerBannerRef.get();
        if (recyclerBanner == null) {
            return;
        }
        recyclerBanner.scrollToPosition(currentPosition);
        // 重置上一次位置
        lastIndicatorPosition = -1;
    }

    private void setupBanner(BannerAdapter.OnBannerClickListener clickListener) {
        if (recyclerBannerRef == null) {
            return;
        }
        RecyclerView recyclerBanner = recyclerBannerRef.get();
        if (recyclerBanner == null) {
            return;
        }
        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerBanner.getContext(),
            LinearLayoutManager.HORIZONTAL, false);
        recyclerBanner.setLayoutManager(layoutManager);

        // 配置SnapHelper实现页面吸附效果
        PagerSnapHelper snapHelper = new PagerSnapHelper();
        snapHelper.attachToRecyclerView(recyclerBanner);

        // 设置适配器
        adapter = new BannerAdapter();
        recyclerBanner.setAdapter(adapter);

        // 1. 使用ViewPool复用（在setupBanner方法中添加）
        recyclerBanner.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        // 2. 优化Item缓存（根据实际需要调整）
        recyclerBanner.setItemViewCacheSize(5);
        recyclerBanner.setHasFixedSize(true);
        // 3. 使用预加载
        layoutManager.setItemPrefetchEnabled(true);
        layoutManager.setInitialPrefetchItemCount(3);

        // 设置滚动监听
        setupScrollListener();

        // 处理Banner点击事件
        adapter.setOnBannerClickListener(clickListener);
    }

    private void setupScrollListener() {
        if (recyclerBannerRef == null) {
            return;
        }
        RecyclerView recyclerBanner = recyclerBannerRef.get();
        if (recyclerBanner == null) {
            return;
        }
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
                        updateIndicatorFromScroll();
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

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 在滚动过程中实时更新指示器
                if (isUserScrolling || recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING) {
                    updateIndicatorFromScroll();
                }
            }
        });
    }

    /**
     * 从滚动状态获取当前位置并更新指示器
     */
    private void updateIndicatorFromScroll() {
        // 防抖动处理
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastIndicatorUpdateTime < INDICATOR_UPDATE_THROTTLE_MS) {
            return;
        }

        if (recyclerBannerRef == null) {
            return;
        }
        RecyclerView recyclerBanner = recyclerBannerRef.get();
        if (recyclerBanner == null) {
            return;
        }
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerBanner.getLayoutManager();
        if (layoutManager == null) {
            return;
        }
        int firstVisiblePosition = layoutManager.findFirstVisibleItemPosition();
        int firstCompletelyVisiblePosition = layoutManager.findFirstCompletelyVisibleItemPosition();

        int position = firstCompletelyVisiblePosition != -1
            ? firstCompletelyVisiblePosition : firstVisiblePosition;

        if (position != RecyclerView.NO_POSITION && currentPosition != position) {
            currentPosition = position;
            updateIndicator(currentPosition);
            lastIndicatorUpdateTime = currentTime;
        }
    }

    private void setupIndicator(int size) {
        if (indicatorContainerRef == null) {
            return;
        }
        LinearLayout indicatorContainer = indicatorContainerRef.get();
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
        // 重置上一次位置
        lastIndicatorPosition = -1;
        // 更新指示器状态
        updateIndicator(0);
    }

    private void updateIndicator(int currentPosition) {
        if (indicatorContainerRef == null) {
            return;
        }
        LinearLayout indicatorContainer = indicatorContainerRef.get();
        if (indicatorContainer == null) {
            return;
        }
        int realPosition = adapter.getRealPosition(currentPosition);

        // 避免频繁刷新相同的指示器位置
        if (realPosition == lastIndicatorPosition) {
            return;
        }

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
        lastIndicatorPosition = realPosition;
    }

    /**
     * 开启自动轮播
     */
    public void startAutoScroll() {
        if (!isAutoScrolling || isUserScrolling || adapter.getRealItemCount() <= 0) {
            return;
        }

        if (autoScrollHandler == null) {
            autoScrollHandler = new Handler(Looper.getMainLooper());
        }

        if (autoScrollRunnable == null) {
            autoScrollRunnable = new Runnable() {
                @Override
                public void run() {
                    if (!isAutoScrolling || isUserScrolling) {
                        return;
                    }

                    int itemCount = adapter.getRealItemCount();
                    if (itemCount <= 0) {
                        return;
                    }

                    // 计算下一个位置（考虑真实数据数量）
                    int nextPosition = currentPosition + 1;
                    // 确保不会超出Integer.MAX_VALUE，但保持循环逻辑
                    if (nextPosition >= Integer.MAX_VALUE - itemCount) {
                        // 重置到安全位置
                        nextPosition = Integer.MAX_VALUE / 2;
                    }

                    currentPosition = nextPosition;
                    if (recyclerBannerRef == null) {
                        return;
                    }
                    RecyclerView recyclerBanner = recyclerBannerRef.get();
                    if (recyclerBanner == null) {
                        return;
                    }
                    recyclerBanner.smoothScrollToPosition(currentPosition);
                    updateIndicator(currentPosition);

                    if (autoScrollHandler != null) {
                        autoScrollHandler.postDelayed(this, AUTO_SCROLL_DELAY);
                    }
                }
            };
        }

        // 移除之前的回调，避免重复
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
        autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
    }

    /**
     * 暂停自动轮播
     * 页面onPause时调用
     */
    public void pauseAutoScroll() {
        if (isAutoScrolling && autoScrollHandler != null) {
            isAutoScrolling = false;
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }

    /**
     * 恢复自动轮播
     * 页面onResume时调用
     */
    public void resumeAutoScroll() {
        if (!isAutoScrolling && !isUserScrolling && autoScrollHandler != null) {
            isAutoScrolling = true;
            autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
        }
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        pauseAutoScroll();
        if (autoScrollHandler != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
            autoScrollHandler = null;
        }
        if (recyclerBannerRef != null) {
            RecyclerView recyclerBanner = recyclerBannerRef.get();
            if (recyclerBanner != null) {
                recyclerBanner.clearOnScrollListeners();
                recyclerBanner.setAdapter(null);
                recyclerBannerRef.clear();
            }
            recyclerBannerRef = null;
        }
        if (indicatorContainerRef != null) {
            LinearLayout indicatorContainer = indicatorContainerRef.get();
            if (indicatorContainer != null) {
                indicatorContainer.removeAllViews();
                indicatorContainerRef.clear();
            }
            indicatorContainerRef = null;
        }
        if (adapter != null) {
            adapter.setOnBannerClickListener(null);
            adapter = null;
        }
    }
}
