package com.qiufengguang.ajstudy.view;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qiufengguang.ajstudy.R;

/**
 * 具有拖拽回弹功能的滚动布局，内部嵌套ScrollView使用
 *
 * @author qiufengguang
 * @since 2025/12/30 15:29
 */
public class BounceScrollView extends FrameLayout {

    /**
     * 触摸状态
     */
    private static final int TOUCH_STATE_IDLE = 0;

    private static final int TOUCH_STATE_BOUNCING = 2;

    /**
     * 默认配置值
     */
    private static final float DEFAULT_BOUNCE_STRENGTH = 0.5f;

    private static final float DEFAULT_OVERSCROLL_DAMPING = 0.5f;

    /**
     * 配置参数
     */
    private float mBounceStrength = DEFAULT_BOUNCE_STRENGTH;

    private int mMaxOverScroll = 150;

    private float mOverScrollDamping = DEFAULT_OVERSCROLL_DAMPING;

    /**
     * 触摸跟踪
     */
    private int mTouchState = TOUCH_STATE_IDLE;

    private float mLastTouchY;

    private float mLastTouchX;

    private int mTouchSlop;

    private float mInitialTouchY;

    private boolean mIsBeingDragged = false;

    /**
     * 滚动偏移
     */
    private float mCurrentOverScrollY = 0;

    /**
     * 动画
     */
    private ValueAnimator mBounceAnimator;

    /**
     * 子控件相关
     */
    private View mChildView;

    public BounceScrollView(@NonNull Context context) {
        this(context, null);
    }

    public BounceScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BounceScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 获取触摸阈值
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        // 解析自定义属性
        try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BounceScrollView)) {
            mBounceStrength = a.getFloat(R.styleable.BounceScrollView_bounceStrength, DEFAULT_BOUNCE_STRENGTH);
            mMaxOverScroll = a.getDimensionPixelSize(R.styleable.BounceScrollView_maxOverScroll,
                getResources().getDimensionPixelSize(R.dimen.bounce_max_overscroll));
            mOverScrollDamping = a.getFloat(R.styleable.BounceScrollView_overScrollDamping, DEFAULT_OVERSCROLL_DAMPING);
        }

        // 启用垂直滚动
        forceHasOverlappingRendering(false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // 只能有一个子View
        if (getChildCount() > 0) {
            mChildView = getChildAt(0);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 如果正在回弹动画中，拦截所有触摸事件
        if (mTouchState == TOUCH_STATE_BOUNCING) {
            return true;
        }

        final float x = ev.getX();
        final float y = ev.getY();

        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // 记录触摸起始点
                mLastTouchX = x;
                mLastTouchY = y;
                mInitialTouchY = y;
                mIsBeingDragged = false;
                break;

            case MotionEvent.ACTION_MOVE:
                // 计算移动距离
                final float dx = Math.abs(x - mLastTouchX);
                final float dy = Math.abs(y - mLastTouchY);

                // 如果垂直移动距离大于阈值，且大于水平移动，则拦截
                if (dy > mTouchSlop && dy > dx) {
                    // 检查是否应该拦截（在边界或者子View已滚动到边界）
                    if (shouldInterceptTouchEvent(dy, y - mInitialTouchY > 0)) {
                        mIsBeingDragged = true;
                        mLastTouchX = x;
                        mLastTouchY = y;
                        return true;
                    }
                }
                break;

            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                mIsBeingDragged = false;
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    /**
     * 判断是否应该拦截触摸事件
     */
    private boolean shouldInterceptTouchEvent(float dy, boolean isMovingDown) {
        // 如果子View可以滚动，检查是否已滚动到边界
        if (mChildView != null) {
            if (isMovingDown) {
                // 向下移动，检查是否在顶部边界
                if (isAtTopEdge()) {
                    return true;
                }
            } else {
                // 向上移动，检查是否在底部边界
                if (isAtBottomEdge()) {
                    return true;
                }
            }
        }

        // 如果已经有越界滚动，应该拦截，默认不拦截，让子View处理
        return mCurrentOverScrollY != 0;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mTouchState == TOUCH_STATE_BOUNCING) {
            return true;
        }

        final float x = event.getX();
        final float y = event.getY();

        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                // 如果拦截了DOWN事件，直接返回true
                mLastTouchX = x;
                mLastTouchY = y;
                mInitialTouchY = y;
                mIsBeingDragged = false;
                return true;

            case MotionEvent.ACTION_MOVE:
                if (!mIsBeingDragged) {
                    final float dy = Math.abs(y - mLastTouchY);
                    if (dy > mTouchSlop) {
                        mIsBeingDragged = true;
                        mLastTouchY = y;
                    }
                }

                if (mIsBeingDragged) {
                    return handleMoveEvent(x, y);
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                handleReleaseEvent();
                mIsBeingDragged = false;
                return true;
        }

        return super.onTouchEvent(event);
    }

    private boolean handleMoveEvent(float x, float y) {
        // 计算移动距离
        final float deltaY = y - mLastTouchY;
        mLastTouchY = y;

        // 处理越界滚动
        handleOverScroll(deltaY);
        return true;
    }

    /**
     * 处理越界滚动 - 修复方向问题
     */
    private void handleOverScroll(float deltaY) {
        // 关键修复：明确方向判断
        // deltaY > 0：手指向下移动（从屏幕上方向下滑动）
        // deltaY < 0：手指向上移动（从屏幕下方向上滑动）

        if (deltaY > 0) {
            // 手指向下移动
            if (isAtTopEdge()) {
                // 在顶部边界，手指向下移动 => 顶部越界（内容被向下拉动）
                float overScroll = deltaY * mOverScrollDamping;
                mCurrentOverScrollY = Math.min(mMaxOverScroll, mCurrentOverScrollY + overScroll);
                applyOverScroll();
            }
        } else {
            // 手指向上移动
            if (isAtBottomEdge()) {
                // 在底部边界，手指向上移动 => 底部越界（内容被向上拉动）
                float overScroll = deltaY * mOverScrollDamping;
                mCurrentOverScrollY = Math.max(-mMaxOverScroll, mCurrentOverScrollY + overScroll);
                applyOverScroll();
            }
        }
    }

    /**
     * 处理释放事件
     */
    private void handleReleaseEvent() {
        if (mIsBeingDragged && mCurrentOverScrollY != 0) {
            // 启动回弹动画
            startBounceAnimation();
        }
        mIsBeingDragged = false;
    }

    /**
     * 启动回弹动画
     */
    private void startBounceAnimation() {
        if (mBounceAnimator != null && mBounceAnimator.isRunning()) {
            mBounceAnimator.cancel();
        }

        mTouchState = TOUCH_STATE_BOUNCING;

        // 创建动画
        mBounceAnimator = ValueAnimator.ofFloat(mCurrentOverScrollY, 0);
        mBounceAnimator.setDuration(300);
        mBounceAnimator.setInterpolator(new OvershootInterpolator(mBounceStrength));

        mBounceAnimator.addUpdateListener(animation -> {
            mCurrentOverScrollY = (float) animation.getAnimatedValue();
            applyOverScroll();
        });

        mBounceAnimator.addListener(new android.animation.AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(android.animation.Animator animation) {
                mCurrentOverScrollY = 0;
                applyOverScroll();
                mTouchState = TOUCH_STATE_IDLE;
            }
        });

        mBounceAnimator.start();
    }

    /**
     * 应用越界滚动偏移
     */
    private void applyOverScroll() {
        // 通过改变子View的translation来应用越界滚动
        if (mChildView != null) {
            mChildView.setTranslationY(mCurrentOverScrollY);
        }
    }

    /**
     * 检查是否在顶部边界
     */
    private boolean isAtTopEdge() {
        if (mChildView == null) {
            return true;
        }

        return !mChildView.canScrollVertically(-1);
    }

    /**
     * 检查是否在底部边界
     */
    private boolean isAtBottomEdge() {
        if (mChildView == null) {
            return true;
        }
        return !mChildView.canScrollVertically(1);
    }

    /**
     * 子View是否可以向上滚动
     */
    private boolean canChildScrollUp() {
        if (mChildView == null) {
            return false;
        }
        return mChildView.canScrollVertically(-1);
    }

    /**
     * 子View是否可以向下滚动
     */
    private boolean canChildScrollDown() {
        if (mChildView == null) {
            return false;
        }
        return mChildView.canScrollVertically(1);
    }

    // ========== 公共API ==========

    /**
     * 设置回弹强度
     *
     * @param strength 强度值(0.0-1.0)，建议0.3-0.7
     */
    public void setBounceStrength(float strength) {
        mBounceStrength = Math.max(0f, Math.min(strength, 1f));
    }

    /**
     * 设置最大越界距离
     *
     * @param maxOverScroll 像素值
     */
    public void setMaxOverScroll(int maxOverScroll) {
        mMaxOverScroll = Math.max(0, maxOverScroll);
    }

    /**
     * 设置越界阻尼系数
     *
     * @param damping 阻尼系数(0.0-1.0)，值越小阻力越大
     */
    public void setOverScrollDamping(float damping) {
        mOverScrollDamping = Math.max(0f, Math.min(damping, 1f));
    }

    /**
     * 获取当前回弹强度
     */
    public float getBounceStrength() {
        return mBounceStrength;
    }

    /**
     * 获取最大越界距离
     */
    public int getMaxOverScroll() {
        return mMaxOverScroll;
    }

    /**
     * 获取越界阻尼系数
     */
    public float getOverScrollDamping() {
        return mOverScrollDamping;
    }

    /**
     * 获取当前越界偏移量
     */
    public float getCurrentOverScroll() {
        return mCurrentOverScrollY;
    }
}