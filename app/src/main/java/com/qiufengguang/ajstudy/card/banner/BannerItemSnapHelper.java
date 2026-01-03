package com.qiufengguang.ajstudy.card.banner;

import android.util.DisplayMetrics;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import java.lang.ref.WeakReference;

/**
 * banner自定义吸附位置的 SnapHelper
 * 将 item 吸附到 RecyclerView 靠左指定距离的位置
 *
 * @author qiufengguang
 * @since 2026/1/3 16:55
 */
public class BannerItemSnapHelper extends SnapHelper {
    /**
     * 滚动速度
     */
    private static final float MILLISECONDS_PER_INCH = 100f;

    /**
     * 吸附位置（像素）
     */
    private final int snapPosition;


    public WeakReference<RecyclerView> recyclerViewRef;


    public BannerItemSnapHelper(int snapDistancePx) {
        // 将 dp 转换为像素
        this.snapPosition = snapDistancePx;
    }

    @Override
    public void attachToRecyclerView(RecyclerView recyclerView) throws IllegalStateException {
        super.attachToRecyclerView(recyclerView);
        if (recyclerView == null) {
            return;
        }

        // 确保 RecyclerView 是水平布局
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager) ||
            ((LinearLayoutManager) layoutManager).getOrientation() != LinearLayoutManager.HORIZONTAL) {
            throw new IllegalArgumentException("只支持水平 LinearLayoutManager");
        }
        recyclerViewRef = new WeakReference<>(recyclerView);
    }

    @Override
    public int[] calculateDistanceToFinalSnap(
        @NonNull RecyclerView.LayoutManager layoutManager,
        @NonNull View targetView) {
        int[] out = new int[2];

        if (layoutManager.canScrollHorizontally()) {
            // 计算需要滚动的距离
            out[0] = distanceToSnapPosition(targetView);
        }
        return out;
    }

    @Override
    public View findSnapView(RecyclerView.LayoutManager layoutManager) {
        return findViewClosestToSnapPosition(layoutManager);
    }

    @Override
    public int findTargetSnapPosition(
        RecyclerView.LayoutManager layoutManager, int velocityX, int velocityY) {
        if (!(layoutManager instanceof LinearLayoutManager)) {
            return RecyclerView.NO_POSITION;
        }

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) layoutManager;

        // 如果没有子View，返回无效位置
        if (layoutManager.getChildCount() == 0) {
            return RecyclerView.NO_POSITION;
        }

        // 找到当前最靠近吸附位置的View
        View snapView = findViewClosestToSnapPosition(layoutManager);
        if (snapView == null) {
            return RecyclerView.NO_POSITION;
        }

        int currentPosition = linearLayoutManager.getPosition(snapView);
        if (currentPosition == RecyclerView.NO_POSITION) {
            return RecyclerView.NO_POSITION;
        }

        // 根据滚动方向决定目标位置
        boolean forwardDirection;
        if (linearLayoutManager.canScrollHorizontally()) {
            forwardDirection = velocityX > 0;
        } else {
            forwardDirection = velocityY > 0;
        }

        // 根据滚动方向调整目标位置
        int targetPosition;
        if (forwardDirection) {
            // 向右滚动，选择下一个item
            targetPosition = currentPosition + 1;
        } else {
            // 向左滚动，选择上一个item
            targetPosition = currentPosition - 1;
        }

        // 确保目标位置有效
        int itemCount = layoutManager.getItemCount();
        if (targetPosition < 0 || targetPosition >= itemCount) {
            return RecyclerView.NO_POSITION;
        }

        return targetPosition;
    }

    @Override
    protected RecyclerView.SmoothScroller createScroller(@NonNull RecyclerView.LayoutManager layoutManager) {
        if (!(layoutManager instanceof RecyclerView.SmoothScroller.ScrollVectorProvider)) {
            return null;
        }
        if (recyclerViewRef == null) {
            return null;
        }
        RecyclerView recyclerView = recyclerViewRef.get();
        if (recyclerView == null) {
            return null;
        }
        return new LinearSmoothScroller(recyclerView.getContext()) {
            @Override
            protected void onTargetFound(View targetView, RecyclerView.State state, Action action) {
                int[] snapDistances = calculateDistanceToFinalSnap(layoutManager, targetView);
                if (snapDistances == null || snapDistances.length != 2) {
                    return;
                }
                int dx = snapDistances[0];
                int dy = snapDistances[1];

                // 计算滚动时间
                int time = calculateTimeForDeceleration(Math.max(Math.abs(dx), Math.abs(dy)));
                if (time > 0) {
                    action.update(dx, dy, time, mDecelerateInterpolator);
                }
            }

            @Override
            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                return MILLISECONDS_PER_INCH / displayMetrics.densityDpi;
            }

            @Override
            protected int getHorizontalSnapPreference() {
                return SNAP_TO_START;
            }

            @Override
            protected int getVerticalSnapPreference() {
                return SNAP_TO_START;
            }
        };
    }

    /**
     * 计算 View 到吸附位置的距离
     */
    private int distanceToSnapPosition(View targetView) {
        // 计算 View 的中心位置
        int viewCenter = getViewCenterX(targetView);

        // 计算吸附位置
        int snapPos = getSnapPosition();

        // 返回需要滚动的距离（负值表示需要向右滚动，正值表示需要向左滚动）
        return viewCenter - snapPos;
    }

    /**
     * 获取 View 的水平中心位置
     */
    private int getViewCenterX(View view) {
        return (view.getLeft() + view.getRight()) / 2;
    }

    /**
     * 获取吸附位置（RecyclerView 靠左 snapPosition 的位置）
     */
    private int getSnapPosition() {
        if (recyclerViewRef == null) {
            return snapPosition;
        }
        RecyclerView recyclerView = recyclerViewRef.get();
        if (recyclerView == null) {
            return snapPosition;
        }
        // 吸附位置 = RecyclerView 左边距 + 指定距离
        return recyclerView.getPaddingLeft() + snapPosition;
    }

    /**
     * 找到最靠近吸附位置的 View
     */
    private View findViewClosestToSnapPosition(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager.getChildCount() == 0) {
            return null;
        }

        int snapPosition = getSnapPosition();
        View closestView = null;
        int minDistance = Integer.MAX_VALUE;

        // 遍历所有可见的 item
        for (int i = 0; i < layoutManager.getChildCount(); i++) {
            View child = layoutManager.getChildAt(i);
            if (child == null) {
                continue;
            }

            int viewCenter = getViewCenterX(child);
            int distance = Math.abs(viewCenter - snapPosition);

            // 如果找到更近的 View，更新
            if (distance < minDistance) {
                minDistance = distance;
                closestView = child;
            }
        }

        return closestView;
    }
}