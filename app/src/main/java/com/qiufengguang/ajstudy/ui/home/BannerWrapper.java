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

    private static final long AUTO_SCROLL_DELAY = 4000L; // 4秒间隔

    private RecyclerView recyclerBanner;

    private LinearLayout indicatorContainer;

    private BannerAdapter adapter;

    private Handler autoScrollHandler;

    private Runnable autoScrollRunnable;

    private boolean isAutoScrolling = true;

    private boolean isUserScrolling = false;

    private int currentPosition = 0;

    public BannerWrapper(@NonNull RecyclerView recyclerBanner, LinearLayout indicatorContainer, BannerAdapter.OnBannerClickListener clickListener) {
        this.recyclerBanner = recyclerBanner;
        this.indicatorContainer = indicatorContainer;

        setupBanner(clickListener);
    }

    public void setBannerBeans(List<BannerBean> bannerBeans) {
        adapter.setBannerBeans(bannerBeans);
        // 初始化指示器
        int size = bannerBeans == null ? 0 : bannerBeans.size();
        setupIndicator(size);
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

        // 设置初始位置到中间，实现无限循环
        currentPosition = Integer.MAX_VALUE / 2;
        recyclerBanner.scrollToPosition(currentPosition);

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

    public void pauseAutoScroll() {
        if (isAutoScrolling) {
            isAutoScrolling = false;
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }

    public void resumeAutoScroll() {
        if (!isAutoScrolling && !isUserScrolling) {
            isAutoScrolling = true;
            autoScrollHandler.postDelayed(autoScrollRunnable, AUTO_SCROLL_DELAY);
        }
    }

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
