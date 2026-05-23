package com.qiufengguang.ajstudy.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qiufengguang.ajstudy.R;

/**
 * 圆形方向控制控件
 * 功能：提供四个方向的视觉引导，支持拖拽和点击，松手后回弹并回调角度。
 * 特性：
 * 1. 顺时针角度 (0-360)，正右为 0°。
 * 2. 中心小圆及扇形支持渐变色及阴影，颜色可配置。
 * 3. 动态扇形指示器，支持固定档位（45°/90°）或随动。
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
    private final Paint sectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // 几何参数
    private float centerX, centerY;
    private float diskRadius;
    private float thumbRadius;
    private float maxThumbMoveRadius; // 小圆圆心能移动的最大半径

    // 当前位置与角度
    private float thumbX, thumbY;
    private float currentAngle = 0f; // 顺时针角度，0°为右

    // 状态记录
    private boolean isDragging = false;

    private int thumbColorStart = 0xFF2196F3;
    private int thumbColorEnd = 0xFF1976D2;
    private int sectorColorStart = 0x882196F3;
    private int sectorColorEnd = 0x002196F3;
    private float sectorSweepAngle = 60f;

    // 辅助路径与区域
    private final Path path = new Path();
    private final RectF diskRectF = new RectF();

    // 动画
    private ValueAnimator reboundAnimator;

    // 监听器
    private OnDirectionPadListener listener;

    public interface OnDirectionPadListener {
        /**
         * 当手指松开时回调
         *
         * @param context      上下文
         * @param angleDegrees 角度 (0-360)，0°表示正右方，顺时针方向增加。
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
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        // 配置属性
        int diskColor;
        try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleCtrl)) {
            diskColor = a.getColor(R.styleable.CircleCtrl_diskColor, Color.WHITE);
            thumbColorStart = a.getColor(R.styleable.CircleCtrl_thumbColorStart, 0xFF2196F3);
            thumbColorEnd = a.getColor(R.styleable.CircleCtrl_thumbColorEnd, 0xFF1976D2);
            sectorColorStart = a.getColor(R.styleable.CircleCtrl_sectorColorStart, 0x882196F3);
            sectorColorEnd = a.getColor(R.styleable.CircleCtrl_sectorColorEnd, 0x002196F3);
            sectorSweepAngle = a.getFloat(R.styleable.CircleCtrl_sectorSweepAngle, 60f);
        }

        // 开启软件渲染以支持阴影
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        // 圆盘画笔
        diskPaint.setColor(diskColor);
        diskPaint.setStyle(Paint.Style.FILL);
        diskPaint.setShadowLayer(10, 0, 4, 0x44000000);

        // 小圆画笔
        thumbPaint.setStyle(Paint.Style.FILL);
        thumbPaint.setShadowLayer(8, 0, 2, 0x66000000);

        // 虚线画笔
        dashedLinePaint.setColor(0xFFCCCCCC);
        dashedLinePaint.setStyle(Paint.Style.STROKE);
        dashedLinePaint.setStrokeWidth(2);
        dashedLinePaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

        // 三角形画笔
        trianglePaint.setColor(Color.BLACK);
        trianglePaint.setStyle(Paint.Style.FILL);

        // 扇形画笔
        sectorPaint.setStyle(Paint.Style.FILL);
    }

    public void setListener(OnDirectionPadListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int pl = getPaddingLeft();
        int pt = getPaddingTop();
        int pr = getPaddingRight();
        int pb = getPaddingBottom();

        float cw = w - pl - pr;
        float ch = h - pt - pb;

        centerX = pl + cw / 2f;
        centerY = pt + ch / 2f;

        diskRadius = Math.min(cw, ch) / 2f - 15;
        thumbRadius = diskRadius / 6f;
        maxThumbMoveRadius = diskRadius - thumbRadius;

        thumbX = centerX;
        thumbY = centerY;

        diskRectF.set(centerX - diskRadius, centerY - diskRadius, centerX + diskRadius, centerY + diskRadius);

        // 初始化扇形渐变
        RadialGradient sectorGradient = new RadialGradient(centerX, centerY, diskRadius,
            new int[]{sectorColorStart, sectorColorEnd}, null, Shader.TileMode.CLAMP);
        sectorPaint.setShader(sectorGradient);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // 1. 绘制圆盘
        canvas.drawCircle(centerX, centerY, diskRadius, diskPaint);

        // 2. 绘制方向扇形
        if (isDragging && (thumbX != centerX || thumbY != centerY)) {
            drawSector(canvas);
        }

        // 3. 绘制虚线
        path.reset();
        path.moveTo(centerX, centerY - diskRadius);
        path.lineTo(centerX, centerY + diskRadius);
        canvas.drawPath(path, dashedLinePaint);
        path.reset();
        path.moveTo(centerX - diskRadius, centerY);
        path.lineTo(centerX + diskRadius, centerY);
        canvas.drawPath(path, dashedLinePaint);

        // 4. 绘制四个三角形
        float triSize = diskRadius * 0.08f;
        float offset = triSize + 5;
        drawTriangle(canvas, centerX, centerY - diskRadius + offset, triSize, 0);
        drawTriangle(canvas, centerX, centerY + diskRadius - offset, triSize, 180);
        drawTriangle(canvas, centerX - diskRadius + offset, centerY, triSize, 270);
        drawTriangle(canvas, centerX + diskRadius - offset, centerY, triSize, 90);

        // 5. 绘制中心小圆（带径向渐变）
        RadialGradient thumbGradient = new RadialGradient(thumbX, thumbY, thumbRadius,
            new int[]{thumbColorStart, thumbColorEnd}, null, Shader.TileMode.CLAMP);
        thumbPaint.setShader(thumbGradient);
        canvas.drawCircle(thumbX, thumbY, thumbRadius, thumbPaint);
    }

    private void drawSector(Canvas canvas) {
        float startAngle;
        float sweep = sectorSweepAngle;

        if (Math.abs(sweep - 90f) < 0.1f) {
            // 4个方向: [45, 135), [135, 225), [225, 315), [315, 45)
            if (currentAngle >= 45 && currentAngle < 135) {
                startAngle = 45;
            } else if (currentAngle >= 135 && currentAngle < 225) {
                startAngle = 135;
            } else if (currentAngle >= 225 && currentAngle < 315) {
                startAngle = 225;
            } else {
                startAngle = 315;
            }
        } else if (Math.abs(sweep - 45f) < 0.1f) {
            // 8个方向: [0, 45), [45, 90), ...
            startAngle = ((int) (currentAngle / 45f)) * 45f;
        } else {
            // 普通模式：扇形中心线对准小圆
            startAngle = currentAngle - sweep / 2f;
        }

        canvas.drawArc(diskRectF, startAngle, sweep, true, sectorPaint);
    }

    private void drawTriangle(Canvas canvas, float x, float y, float size, float rotation) {
        canvas.save();
        canvas.translate(x, y);
        canvas.rotate(rotation);
        path.reset();
        path.moveTo(0, -size);
        path.lineTo(-size / 1.5f, 0);
        path.lineTo(size / 1.5f, 0);
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
                if (distance > diskRadius) {
                    isDragging = false;
                    return false;
                }
                isDragging = true;
                ViewParent parent = getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }

                if (reboundAnimator != null && reboundAnimator.isRunning()) {
                    reboundAnimator.cancel();
                }
                updatePosition(x, y, dx, dy);
                return true;

            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    updatePosition(x, y, dx, dy);
                }
                return isDragging;

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isDragging) {
                    if (listener != null) {
                        listener.onAngleRevealed(getContext(), currentAngle);
                    }
                    isDragging = false;
                    startRebound();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void updatePosition(float x, float y, float dx, float dy) {
        // 计算顺时针角度 (0-360)
        double radians = Math.atan2(dy, dx);
        currentAngle = (float) Math.toDegrees(radians);
        if (currentAngle < 0) {
            currentAngle += 360;
        }

        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        if (distance <= maxThumbMoveRadius) {
            thumbX = x;
            thumbY = y;
        } else {
            thumbX = centerX + (dx * maxThumbMoveRadius / distance);
            thumbY = centerY + (dy * maxThumbMoveRadius / distance);
        }
        invalidate();
    }

    private void startRebound() {
        final float startX = thumbX;
        final float startY = thumbY;

        reboundAnimator = ValueAnimator.ofFloat(0f, 1f);
        reboundAnimator.setDuration(200);
        reboundAnimator.setInterpolator(new DecelerateInterpolator());
        reboundAnimator.addUpdateListener(animation -> {
            float f = (float) animation.getAnimatedValue();
            thumbX = startX + (centerX - startX) * f;
            thumbY = startY + (centerY - startY) * f;
            invalidate();
        });
        reboundAnimator.start();
    }
}
