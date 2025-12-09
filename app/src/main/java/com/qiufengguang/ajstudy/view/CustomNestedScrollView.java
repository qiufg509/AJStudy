package com.qiufengguang.ajstudy.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.core.widget.NestedScrollView;

public class CustomNestedScrollView extends NestedScrollView {

    private float startY;
    private float touchSlop;
    private boolean isIntercepted = false;

    public CustomNestedScrollView(Context context) {
        super(context);
        init();
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        touchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                startY = ev.getY();
                isIntercepted = false;
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isIntercepted) {
                    final float y = ev.getY();
                    final float dy = y - startY;
                    final float absDy = Math.abs(dy);

                    // 判断是否需要拦截事件
                    if (absDy > touchSlop) {
                        // 如果向上滑动且已经到达顶部，或者向下滑动且已经到达底部，则不拦截
                        if ((dy > 0 && !canScrollUp()) || (dy < 0 && !canScrollDown())) {
                            isIntercepted = true;
                            return true;
                        }
                    }
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();

        if (action == MotionEvent.ACTION_MOVE) {
            if (isIntercepted) {
                // 重置拦截标志
                isIntercepted = false;
            }
        }

        return super.onTouchEvent(ev);
    }

    /**
     * 判断是否可以向上滚动（即内容是否未滚动到底部）
     */
    private boolean canScrollUp() {
        return getChildAt(0).getBottom() > (getHeight() + getScrollY());
    }

    /**
     * 判断是否可以向下滚动（即内容是否未滚动到顶部）
     */
    private boolean canScrollDown() {
        return getScrollY() > 0;
    }
}