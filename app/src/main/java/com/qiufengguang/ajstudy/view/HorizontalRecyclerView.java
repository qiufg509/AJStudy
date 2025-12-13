package com.qiufengguang.ajstudy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 优先自己消费横向滑动事件的RecyclerView
 * 用于嵌套的子列表控件
 *
 * @author qiufengguang
 * @since 2025/12/13 20:31
 */
public class HorizontalRecyclerView extends RecyclerView {
    private float startX;
    private float startY;
    private int touchSlop;

    public HorizontalRecyclerView(@NonNull Context context) {
        super(context);
        init(context);
    }

    public HorizontalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public HorizontalRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startX = e.getX();
                startY = e.getY();
                // 请求父View不拦截事件
                requestParentDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = Math.abs(e.getX() - startX);
                float dy = Math.abs(e.getY() - startY);

                // 判断是否为水平滑动
                if (dx > touchSlop && dx > dy) {
                    // 如果是水平滑动，自己处理
                    requestParentDisallowInterceptTouchEvent(true);
                    return true;
                } else if (dy > touchSlop) {
                    // 如果是垂直滑动，让父View处理
                    requestParentDisallowInterceptTouchEvent(false);
                    return false;
                }
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                requestParentDisallowInterceptTouchEvent(false);
                break;
        }

        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                requestParentDisallowInterceptTouchEvent(true);
                break;

            case MotionEvent.ACTION_MOVE:
                float dx = e.getX() - startX;

                // 检查是否滑动到边界
                boolean canScrollHorizontally = canScrollHorizontally((int) -dx);
                // 如果到达边界，允许父View拦截，否则自己处理
                requestParentDisallowInterceptTouchEvent(canScrollHorizontally);
                break;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                requestParentDisallowInterceptTouchEvent(false);
                break;
        }

        return super.onTouchEvent(e);
    }

    private void requestParentDisallowInterceptTouchEvent(boolean disallow) {
        if (getParent() != null) {
            getParent().requestDisallowInterceptTouchEvent(disallow);
        }
    }
}