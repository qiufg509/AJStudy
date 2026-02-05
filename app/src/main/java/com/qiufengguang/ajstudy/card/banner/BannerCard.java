package com.qiufengguang.ajstudy.card.banner;

import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.BannerBean;
import com.qiufengguang.ajstudy.databinding.CardBannerBinding;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.global.GlobalApp;
import com.qiufengguang.ajstudy.utils.DisplayMetricsHelper;
import com.qiufengguang.ajstudy.view.RoundedFrameLayout;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 轮播banner卡片
 *
 * @author qiufengguang
 * @since 2025/12/19 16:33
 */
public class BannerCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 1;

    /**
     * 自动轮播默认滚动间隔4秒
     */
    private static final long AUTO_SCROLL_DELAY = 4000L;

    /**
     * 8/12栅格item间距
     */
    private static final int GAP = DisplayMetricsHelper.dp2px(GlobalApp.getContext(), 8);

    private WeakReference<RecyclerView> recyclerBannerRef;

    private WeakReference<LinearLayout> indicatorContainerRef;

    /**
     * 栅格数（4栅格显示1个、8栅格显示2个、12栅格显示3个）
     */
    private int column = Constant.Grid.COLUMN_DEFAULT;

    private long carouselInterval = AUTO_SCROLL_DELAY;
    /**
     * 图片宽高比
     */
    private float itemRatio = 9.0f / 16.0f;

    /**
     * 圆角大小
     */
    private int cornerRadius = -1;

    private BannerAdapter adapter;

    private int itemWidth;

    private int itemHeight;

    private OnItemClickListener<BannerBean> clickListener;

    private Handler autoScrollHandler;

    private Runnable autoScrollRunnable;

    /**
     * 是否自动轮播滚动
     */
    private boolean isAutoScrolling = false;

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

    private BannerDecoration decor;

    private BannerCard() {
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

        readyAutoScroll();
    }

    /**
     * 初始化
     */
    private void init() {
        if (recyclerBannerRef == null) {
            return;
        }
        RecyclerView recyclerBanner = recyclerBannerRef.get();
        if (recyclerBanner == null) {
            return;
        }
        Resources resources = recyclerBanner.getResources();
        float horizontalMargin = resources.getDimension(R.dimen.banner_horizontal_margin);
        int screenWidth = DisplayMetricsHelper.getScreenWidth(recyclerBanner.getContext());
        float bannerWidth = screenWidth - horizontalMargin * 2;
        int snapDistancePx;
        switch (column) {
            case Constant.Grid.COLUMN_8:
                this.itemWidth = (int) (bannerWidth - GAP * Constant.Pln.DEF_8) / Constant.Pln.DEF_8;
                this.itemHeight = (int) ((bannerWidth - GAP * Constant.Pln.DEF_8) / Constant.Pln.DEF_8 * itemRatio);
                snapDistancePx = (this.itemWidth + GAP) / 2;
                break;
            case Constant.Grid.COLUMN_12:
                this.itemWidth = (int) (bannerWidth - GAP * Constant.Pln.DEF_12) / Constant.Pln.DEF_12;
                this.itemHeight = (int) ((bannerWidth - GAP * Constant.Pln.DEF_12) / Constant.Pln.DEF_12 * itemRatio);
                snapDistancePx = (this.itemWidth + GAP) / 2;
                break;
            default:
                this.itemWidth = (int) bannerWidth;
                this.itemHeight = (int) (bannerWidth * itemRatio);
                snapDistancePx = this.itemWidth / 2;
                break;
        }

        setupBanner(snapDistancePx);
    }

    /**
     * 初始化banner
     *
     * @param snapDistancePx 吸附位置
     */
    private void setupBanner(int snapDistancePx) {
        if (recyclerBannerRef == null) {
            return;
        }
        RecyclerView recyclerBanner = recyclerBannerRef.get();
        if (recyclerBanner == null) {
            return;
        }

        // 检查是否已经设置了 SnapHelper
        if (recyclerBanner.getOnFlingListener() != null) {
            // 如果已经有 SnapHelper，先清理
            recyclerBanner.setOnFlingListener(null);
        }

        setBannerRootRadius(recyclerBanner.getParent(), cornerRadius);
        if (this.column != Constant.Grid.COLUMN_DEFAULT) {
            if (decor == null) {
                decor = new BannerDecoration(GAP, 0);
            }
            recyclerBanner.removeItemDecoration(decor);
            recyclerBanner.addItemDecoration(decor);
        }

        LinearLayoutManager layoutManager = new LinearLayoutManager(recyclerBanner.getContext(),
            LinearLayoutManager.HORIZONTAL, false);
        recyclerBanner.setLayoutManager(layoutManager);

        // 配置SnapHelper实现页面吸附效果
        BannerItemSnapHelper snapHelper = new BannerItemSnapHelper(snapDistancePx);
        snapHelper.attachToRecyclerView(recyclerBanner);

        // 设置适配器
        adapter = new BannerAdapter(this.itemWidth, this.itemHeight,
            this.column == Constant.Grid.COLUMN_DEFAULT ? 0 : this.cornerRadius);
        recyclerBanner.setAdapter(adapter);

        // 1. 使用ViewPool复用（在setupBanner方法中添加）
        recyclerBanner.setRecycledViewPool(new RecyclerView.RecycledViewPool());
        // 2. 优化Item缓存（根据实际需要调整）
        recyclerBanner.setItemViewCacheSize(5);
        recyclerBanner.setHasFixedSize(true);
        // 3. 使用预加载
        layoutManager.setItemPrefetchEnabled(true);
        layoutManager.setInitialPrefetchItemCount(3);

        setupScrollListener();

        // 处理Banner点击事件
        adapter.setOnBannerClickListener(clickListener);
    }

    /**
     * 设置滚动监听
     */
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
                    startOrResumeAutoScroll();
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
                        startOrResumeAutoScroll();
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
                if (isUserScrolling && recyclerView.getScrollState() == RecyclerView.SCROLL_STATE_SETTLING) {
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

    /**
     * 设置指示器
     *
     * @param size 指示器数量
     */
    private void setupIndicator(int size) {
        if (indicatorContainerRef == null) {
            return;
        }
        LinearLayout indicatorContainer = indicatorContainerRef.get();
        if (indicatorContainer == null || size <= 0) {
            return;
        }
        indicatorContainer.removeAllViews();
        Resources resources = indicatorContainer.getResources();
        int indicatorSize = resources.getDimensionPixelSize(R.dimen.banner_indicator_size);
        int indicatorMargin = resources.getDimensionPixelSize(R.dimen.activity_horizontal_margin_xs);
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

    /**
     * 更新指示器
     *
     * @param currentPosition 当前位置
     */
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
     * 轮播就绪
     */
    private void readyAutoScroll() {
        if (adapter.getRealItemCount() <= 0) {
            return;
        }

        if (autoScrollHandler == null) {
            autoScrollHandler = new Handler(Looper.getMainLooper());
        }

        if (autoScrollRunnable == null) {
            autoScrollRunnable = this::autoScroll;
        }

        // 移除之前的回调，避免重复
        autoScrollHandler.removeCallbacks(autoScrollRunnable);
    }

    /**
     * 自动轮播
     */
    private void autoScroll() {
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

        if (autoScrollHandler != null && autoScrollRunnable != null) {
            autoScrollHandler.postDelayed(autoScrollRunnable, carouselInterval);
        }
    }

    /**
     * 暂停自动轮播
     * 页面onPause时调用  或 卡片不可见时调用
     */
    public void pauseAutoScroll() {
        if (isAutoScrolling && autoScrollHandler != null) {
            isAutoScrolling = false;
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }

    /**
     * 开始轮播/恢复自动轮播
     * 页面onResume时调用 或 卡片恢复可见时调用
     */
    public void startOrResumeAutoScroll() {
        if (!isAutoScrolling && !isUserScrolling && autoScrollHandler != null) {
            isAutoScrolling = true;
            autoScrollHandler.postDelayed(autoScrollRunnable, carouselInterval);
        }
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        pauseAutoScroll();
        if (autoScrollHandler != null) {
            autoScrollHandler = null;
        }
        if (recyclerBannerRef != null) {
            RecyclerView recyclerBanner = recyclerBannerRef.get();
            if (recyclerBanner != null) {
                recyclerBanner.clearOnScrollListeners();
                recyclerBanner.setOnFlingListener(null);
                recyclerBanner.setAdapter(null);
                recyclerBanner.setLayoutManager(null);
                recyclerBanner.removeItemDecoration(decor);
                recyclerBannerRef.clear();
            }
            recyclerBannerRef = null;
        }
        decor = null;
        releaseIndicator();
        if (adapter != null) {
            adapter.setOnBannerClickListener(null);
            adapter = null;
        }
    }

    /**
     * 是否导航点相关资源
     */
    private void releaseIndicator() {
        if (indicatorContainerRef != null) {
            LinearLayout indicatorContainer = indicatorContainerRef.get();
            if (indicatorContainer != null) {
                indicatorContainer.removeAllViews();
                indicatorContainerRef.clear();
            }
            indicatorContainerRef = null;
        }
    }

    /**
     * 设置banner根布局RoundedFrameLayout圆角
     *
     * @param parent banner根布局RoundedFrameLayout
     * @param radius 圆角大小
     */
    private void setBannerRootRadius(ViewParent parent, int radius) {
        if (radius < 0) {
            return;
        }
        if (parent instanceof RoundedFrameLayout) {
            ((RoundedFrameLayout) parent).setCornerRadius(radius);
        }
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardBannerBinding binding = CardBannerBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new BannerCardHolder(binding, lifecycleOwner);
        }

        @Override
        public boolean needObserveLifecycle() {
            return true;
        }
    }

    public static class Builder {
        private RecyclerView recyclerView;

        private LinearLayout indicatorContainer;

        private int column;

        private long carouselInterval = AUTO_SCROLL_DELAY;

        private float itemRatio = 9.0f / 16.0f;

        private int cornerRadius = -1;

        private OnItemClickListener<BannerBean> clickListener;

        /**
         * 设置banner布局控件
         *
         * @param root 轮播banner根布局
         * @return Builder
         */
        public BannerCard.Builder setBannerLayout(@NonNull View root) {
            if (!(root instanceof ViewGroup)) {
                throw new IllegalArgumentException("root must be of type RoundedFrameLayout.");
            }
            ViewGroup group = (ViewGroup) root;
            for (int index = 0, sum = group.getChildCount(); index < sum; index++) {
                View child = group.getChildAt(index);
                if (child instanceof RecyclerView) {
                    this.recyclerView = (RecyclerView) child;
                }
                if (child instanceof LinearLayout) {
                    this.indicatorContainer = (LinearLayout) child;
                }
            }
            return this;
        }

        /**
         * 设置banner布局控件
         *
         * @param recyclerView RecyclerView
         * @return Builder
         */
        public BannerCard.Builder setRecyclerView(@NonNull RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return this;
        }

        /**
         * 设置banner指示器布局
         *
         * @param indicatorContainer LinearLayout
         * @return Builder
         */
        public BannerCard.Builder setIndicatorContainer(LinearLayout indicatorContainer) {
            this.indicatorContainer = indicatorContainer;
            return this;
        }

        /**
         * 设置栅格数（4栅格显示1个、8栅格显示2个、12栅格显示3个，默认系统根据屏幕宽度读取ajstudy_column_count）
         *
         * @param column 栅格数 {@link Constant.Grid}
         * @return Builder
         */
        public BannerCard.Builder setColumn(int column) {
            this.column = column;

            return this;
        }

        /**
         * 设置轮播间隔时间（默认4s，设置大于500L有效）
         *
         * @param carouselInterval 间隔时间ms
         * @return Builder
         */
        public BannerCard.Builder setCarouselInterval(long carouselInterval) {
            this.carouselInterval = carouselInterval;
            return this;
        }

        /**
         * 设置图片高宽比（默认9.0f / 16.0f）
         *
         * @param itemRatio 宽高比值
         * @return Builder
         */
        public BannerCard.Builder setItemRatio(float itemRatio) {
            this.itemRatio = itemRatio;
            return this;
        }

        /**
         * 设置圆角大小（默认16dp，设置0为直角）
         *
         * @param cornerRadius 圆角大小
         * @return Builder
         */
        public BannerCard.Builder setCornerRadius(int cornerRadius) {
            this.cornerRadius = cornerRadius;
            return this;
        }

        /**
         * 设置item点击回调
         *
         * @param clickListener {@link OnItemClickListener}
         * @return Builder
         */
        public BannerCard.Builder setClickListener(
            OnItemClickListener<BannerBean> clickListener) {
            this.clickListener = clickListener;
            return this;
        }

        public BannerCard create() {
            if (this.recyclerView == null) {
                throw new UnsupportedOperationException(
                    "recyclerView is null, the root must contain a sub-child of RecyclerView.");
            }
            BannerCard wrapper = new BannerCard();
            wrapper.recyclerBannerRef = new WeakReference<>(this.recyclerView);
            if (this.column > 0) {
                wrapper.column = this.column;
            } else {
                wrapper.column = this.recyclerView.getResources()
                    .getInteger(R.integer.ajstudy_column_count);
            }
            if (wrapper.column == Constant.Grid.COLUMN_DEFAULT && this.indicatorContainer != null) {
                wrapper.indicatorContainerRef = new WeakReference<>(this.indicatorContainer);
            }
            if (this.carouselInterval > 500L) {
                wrapper.carouselInterval = this.carouselInterval;
            }
            if (this.itemRatio > 0) {
                wrapper.itemRatio = this.itemRatio;
            }
            if (this.cornerRadius >= 0) {
                wrapper.cornerRadius = this.cornerRadius;
            } else {
                wrapper.cornerRadius = this.recyclerView.getResources()
                    .getDimensionPixelSize(R.dimen.radius_l);
            }
            if (this.clickListener != null) {
                wrapper.clickListener = this.clickListener;
            }
            wrapper.init();
            return wrapper;
        }
    }
}
