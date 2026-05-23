package com.qiufengguang.ajstudy.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * 圆形方向控制控件
 * 功能：提供四个方向的视觉引导，支持拖拽和点击，松手后回弹并回调角度。
 * 已优化：解决 RecyclerView 嵌套滑动冲突，限制点击区域，并将三角形移至圆盘内部。
 *
 * @author qiufengguang
 * @since 2026/2/11
 */
public class CircleCtrl extends View {

    // 画笔
    private final Paint diskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint dashedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint trianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // 几何参数
    private float centerX, centerY;
    private float diskRadius;
    private float thumbRadius;
    private float maxThumbMoveRadius; // 小圆圆心能移动的最大半径

    // 当前小圆位置
    private float thumbX, thumbY;
    
    // 状态记录：是否正在有效的拖拽（从圆盘内开始）
    private boolean isDragging = false;

    // 辅助路径（绘制三角形和虚线）
    private final Path path = new Path();

    // 动画
    private ValueAnimator reboundAnimator;

    // 监听器
    private OnDirectionPadListener listener;

    public interface OnDirectionPadListener {
        /**
         * 当手指松开时回调
         *
         * @param context      上下文
         * @param angleDegrees 角度 (0-360)，0°表示正右方，逆时针方向增加。
         *                     例如：正上为 90°，正左为 180°，正下为 270°。
         */
        void onAngleRevealed(Context context, @FloatRange(from = 0.0f, to = 360.0f) float angleDegrees);
    }

    public CircleCtrl(Context context) {
        this(context, null);
    }

    public CircleCtrl(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleCtrl(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 白色圆盘画笔
        diskPaint.setColor(Color.WHITE);
        diskPaint.setStyle(Paint.Style.FILL);
        // 设置阴影（注意：如果预览不显示阴影，是因为硬件加速，此处开启软件渲染层）
        setLayerType(LAYER_TYPE_SOFTWARE, null);
        diskPaint.setShadowLayer(10, 0, 4, 0x44000000);

        // 中心小圆画笔（蓝色）
        thumbPaint.setColor(0xFF2196F3);
        thumbPaint.setStyle(Paint.Style.FILL);

        // 虚线画笔
        dashedLinePaint.setColor(0xFFCCCCCC);
        dashedLinePaint.setStyle(Paint.Style.STROKE);
        dashedLinePaint.setStrokeWidth(2);
        dashedLinePaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

        // 三角形画笔
        trianglePaint.setColor(Color.BLACK);
        trianglePaint.setStyle(Paint.Style.FILL);
    }

    public void setListener(OnDirectionPadListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();

        int contentWidth = w - paddingLeft - paddingRight;
        int contentHeight = h - paddingTop - paddingBottom;

        centerX = paddingLeft + contentWidth / 2f;
        centerY = paddingTop + contentHeight / 2f;

        // 圆盘半径：留出阴影空间
        diskRadius = Math.min(contentWidth, contentHeight) / 2f - 15;
        thumbRadius = diskRadius / 6f;
        maxThumbMoveRadius = diskRadius - thumbRadius;

        // 初始位置在中心
        thumbX = centerX;
        thumbY = centerY;
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // 1. 绘制圆盘
        canvas.drawCircle(centerX, centerY, diskRadius, diskPaint);

        // 2. 绘制虚线
        path.reset();
        path.moveTo(centerX, centerY - diskRadius);
        path.lineTo(centerX, centerY + diskRadius);
        canvas.drawPath(path, dashedLinePaint);
        path.reset();
        path.moveTo(centerX - diskRadius, centerY);
        path.lineTo(centerX + diskRadius, centerY);
        canvas.drawPath(path, dashedLinePaint);

        // 3. 绘制四个三角形 (全部移入圆盘内部)
        float triSize = diskRadius * 0.08f;
        float offset = triSize + 5; // 向内偏移量
        // 上
        drawTriangle(canvas, centerX, centerY - diskRadius + offset, triSize, 0);
        // 下
        drawTriangle(canvas, centerX, centerY + diskRadius - offset, triSize, 180);
        // 左
        drawTriangle(canvas, centerX - diskRadius + offset, centerY, triSize, 270);
        // 右
        drawTriangle(canvas, centerX + diskRadius - offset, centerY, triSize, 90);

        // 4. 绘制中心小圆
        canvas.drawCircle(thumbX, thumbY, thumbRadius, thumbPaint);
    }

    /**
     * 在指定位置绘制三角形
     *
     * @param rotationDegrees 旋转角度，0度向上
     */
    private void drawTriangle(Canvas canvas, float x, float y, float size, float rotationDegrees) {
        canvas.save();
        canvas.translate(x, y);
        canvas.rotate(rotationDegrees);
        path.reset();
        path.moveTo(0, -size);      // 顶点
        path.lineTo(-size / 1.5f, 0); // 左下
        path.lineTo(size / 1.5f, 0);  // 右下
        path.close();
        canvas.drawPath(path, trianglePaint);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        float dx = x - centerX;
        float dy = y - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 1. 点击圆盘外不响应任何事件
                if (distance > diskRadius) {
                    isDragging = false;
                    return false;
                }
                
                isDragging = true;
                // 2. 拖拽开始，禁止父容器（如 RecyclerView）拦截事件
                ViewParent parent = getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }

                if (reboundAnimator != null && reboundAnimator.isRunning()) {
                    reboundAnimator.cancel();
                }
                updateThumbPosition(x, y);
                return true;

            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    updateThumbPosition(x, y);
                }
                // 即使手指滑出圆盘，只要是从圆盘内开始的，就继续消费事件
                return isDragging;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isDragging) {
                    handleActionUp();
                    isDragging = false;
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void updateThumbPosition(float x, float y) {
        float dx = x - centerX;
        float dy = y - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        if (distance <= maxThumbMoveRadius) {
            thumbX = x;
            thumbY = y;
        } else {
            // 边界限制：将坐标等比缩放到边界上
            thumbX = centerX + (dx * maxThumbMoveRadius / distance);
            thumbY = centerY + (dy * maxThumbMoveRadius / distance);
        }
        invalidate();
    }

    private void handleActionUp() {
        if (listener != null) {
            float dx = thumbX - centerX;
            float dy = thumbY - centerY;
            double radians = Math.atan2(-dy, dx);
            float degrees = (float) Math.toDegrees(radians);
            if (degrees < 0) {
                degrees += 360;
            }
            listener.onAngleRevealed(getContext(), degrees);
        }
        startReboundAnimation();
    }

    private void startReboundAnimation() {
        final float startX = thumbX;
        final float startY = thumbY;

        reboundAnimator = ValueAnimator.ofFloat(0f, 1f);
        reboundAnimator.setDuration(200);
        reboundAnimator.setInterpolator(new DecelerateInterpolator());
        reboundAnimator.addUpdateListener(animation -> {
            float fraction = (float) animation.getAnimatedValue();
            thumbX = startX + (centerX - startX) * fraction;
            thumbY = startY + (centerY - startY) * fraction;
            invalidate();
        });
        reboundAnimator.start();
    }
}
