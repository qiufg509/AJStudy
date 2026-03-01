package com.qiufengguang.ajstudy.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.luckywheel.LuckyWheelCard;
import com.qiufengguang.ajstudy.data.model.LuckyWheelCardBean;
import com.qiufengguang.ajstudy.utils.DisplayMetricsHelper;
import com.qiufengguang.ajstudy.utils.ImageUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 旋转转盘控件
 *
 * @author qiufengguang
 * @since 2026/2/4 0:03
 */
public class LuckyWheel extends View {
    private static final int DEFAULT_COLOR_DARK = 0xFF8584;

    private static final int DEFAULT_COLOR_LIGHT = 0xFE6869;

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private int initAngle = 0;
    private int radius = 0;
    private int verPanRadius;
    private int diffRadius;
    public static final int FLING_VELOCITY_DOWNSCALE = 4;

    private GestureDetector detector;
    private OverScroller scroller;

    private int iconSize;
    private int centerBtnSize;
    private Bitmap centerBitmap;

    // 边框相关属性
    private int borderWidth;
    private int shadowWidth;

    // 预分配对象，避免在onDraw中创建
    private final RectF drawRect = new RectF();
    private final RectF borderRect = new RectF();
    private final Path textPath = new Path();
    private final RectF iconRect = new RectF();
    private final RectF centerBtnRect = new RectF();

    /**
     * 圆心坐标x
     */
    private float centerX = 0;
    /**
     * 圆心坐标y
     */
    private float centerY = 0;

    /**
     * 旋转一圈所需要的时间
     */
    private static final long ONE_WHEEL_TIME = 500;

    private List<LuckyWheelCardBean> beans;

    private boolean isTouchEnabled = true;

    private boolean releaseOnSelfDetached = false;

    /**
     * 文字绘制方向：true-朝向圆心方向绘制（直线路径），false-沿着圆弧绘制
     */
    private boolean isTowardCenter = false;

    /**
     * 最小尺寸dp
     */
    private static final int DEFAULT_MIN_SIZE = 200;

    private AnimationEndListener animationEndListener;

    private boolean isFlingFinishedCallbackFired = true;

    public LuckyWheel(Context context) {
        this(context, null);
    }

    public LuckyWheel(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckyWheel(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attrs) {
        try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LuckyWheel)) {
            iconSize = a.getDimensionPixelSize(R.styleable.LuckyWheel_iconSize,
                DisplayMetricsHelper.dp2px(context, 40));
            centerBtnSize = a.getDimensionPixelSize(R.styleable.LuckyWheel_centerBtnSize,
                DisplayMetricsHelper.dp2px(context, 72));

            int centerBtnResId = a.getResourceId(R.styleable.LuckyWheel_centerBtn, 0);
            if (centerBtnResId != 0) {
                loadCenterButtonBitmap(context, centerBtnResId);
            }

            // 读取文字绘制方向属性，默认为false（沿着圆弧绘制）
            isTowardCenter = a.getBoolean(R.styleable.LuckyWheel_isTowardCenter, false);
        }
        detector = new GestureDetector(context, new RotatePanGestureListener());
        scroller = new OverScroller(context);

        // 初始化边框和阴影属性
        borderWidth = DisplayMetricsHelper.dp2px(context, 8);
        shadowWidth = DisplayMetricsHelper.dp2px(context, 4);
        int padding = borderWidth;
        setPadding(padding, padding, padding, padding);

        initPaints();
        setClickable(true);

        // 设置硬件加速下的阴影支持
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    public void setBeans(List<LuckyWheelCardBean> beans) {
        if (beans == null || beans.isEmpty()) {
            return;
        }

        // 释放旧资源
        releaseOldBitmaps();

        // 创建可修改的副本
        this.beans = new ArrayList<>(beans);
        int size = this.beans.size();
        initAngle = 360 / size;
        verPanRadius = 360 / size;
        diffRadius = verPanRadius / 2;

        loadBitmapsAsync(() -> {
            requestLayout();
            invalidate();
        });

        // 重新绘制并请求重新布局
        requestLayout();
        invalidate();
    }

    private void initPaints() {
        // 初始化扇形绘制画笔
        paint.setStyle(Paint.Style.FILL);

        // 初始化文字画笔
        textPaint.setColor(Color.WHITE);
        textPaint.setLetterSpacing(0.05f);
        textPaint.setTextSize(DisplayMetricsHelper.dp2px(getContext(), 14));

        // 初始化边框画笔
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(0xFFF2F2F2);
        borderPaint.setStrokeWidth(borderWidth);
        // 设置阴影效果
        borderPaint.setShadowLayer(shadowWidth, 0, 0, 0x80000000);
    }

    private void loadCenterButtonBitmap(Context context, int resId) {
        if (centerBitmap != null && !centerBitmap.isRecycled()) {
            centerBitmap.recycle();
        }

        int densityDpi = DisplayMetricsHelper.getDensityDpi(context);
        centerBitmap = ImageUtil.loadBitmap(context, densityDpi, resId, centerBtnSize);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 修复测量逻辑
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int defaultSize = DisplayMetricsHelper.dp2px(getContext(), DEFAULT_MIN_SIZE);
        int desiredSize;
        if (widthMode == MeasureSpec.EXACTLY && heightMode == MeasureSpec.EXACTLY) {
            // 两者都是EXACTLY，取较小值确保正方形
            desiredSize = Math.min(widthSize, heightSize);
        } else if (widthMode == MeasureSpec.EXACTLY) {
            // 宽度固定，高度根据宽度决定（正方形）
            desiredSize = widthSize;
            if (heightMode == MeasureSpec.AT_MOST) {
                desiredSize = Math.min(desiredSize, heightSize);
            }
        } else if (heightMode == MeasureSpec.EXACTLY) {
            // 高度固定，宽度根据高度决定（正方形）
            desiredSize = heightSize;
            if (widthMode == MeasureSpec.AT_MOST) {
                desiredSize = Math.min(desiredSize, widthSize);
            }
        } else {
            // 两者都不是EXACTLY，使用默认大小
            desiredSize = defaultSize;
            if (widthMode == MeasureSpec.AT_MOST) {
                desiredSize = Math.min(desiredSize, widthSize);
            }
            if (heightMode == MeasureSpec.AT_MOST) {
                desiredSize = Math.min(desiredSize, heightSize);
            }
        }

        // 确保至少有一个最小尺寸
        desiredSize = Math.max(desiredSize, defaultSize);

        // 设置正方形尺寸
        setMeasuredDimension(desiredSize, desiredSize);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // 当视图大小改变时更新绘制区域
        updateDrawRect();

        // 计算圆心坐标
        updateCenterCoordinates();

        // 更新中心按钮的绘制区域
        updateCenterButtonRect();
    }

    private void updateDrawRect() {
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        int minValue = Math.min(width, height);
        radius = minValue / 2;

        // 计算绘制矩形的正确位置，使其在可用空间内居中
        float left = paddingLeft + (width - minValue) / 2.0f;
        float top = paddingTop + (height - minValue) / 2.0f;
        float right = left + minValue;
        float bottom = top + minValue;

        // 更新绘制区域，重用RectF对象
        drawRect.set(left, top, right, bottom);

        // 计算边框矩形（在转盘外部）
        borderRect.set(left, top, right, bottom);

    }

    private void updateCenterCoordinates() {
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        int minValue = Math.min(width, height);

        // 圆心应该位于绘制矩形的中心，而不是整个View的中心
        centerX = paddingLeft + (width - minValue) / 2.0f + minValue / 2.0f;
        centerY = paddingTop + (height - minValue) / 2.0f + minValue / 2.0f;
    }

    private void updateCenterButtonRect() {
        if (centerBitmap != null && !centerBitmap.isRecycled()) {
            float halfWidth = centerBitmap.getWidth() / 2.0f;
            float halfHeight = centerBitmap.getHeight() / 2.0f;
            // 考虑指示器凸起部分
            if (halfWidth < halfHeight) {
                centerBtnRect.set(centerX - halfWidth, centerY - halfHeight - (halfHeight - halfWidth),
                    centerX + halfWidth, centerY + halfWidth);
            } else {
                centerBtnRect.set(centerX - halfWidth - (halfWidth - halfHeight), centerY - halfHeight,
                    centerX + halfHeight, centerY + halfHeight);
            }
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        // 绘制边框
        drawBorder(canvas);

        if (beans == null || beans.isEmpty()) {
            // 如果没有数据，绘制一个默认的圆形
            drawEmptyWheel(canvas);
            return;
        }

        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        int minValue = Math.min(width, height);
        radius = minValue / 2;

        // 计算绘制矩形的正确位置，使其在可用空间内居中
        float left = paddingLeft + (width - minValue) / 2.0f;
        float top = paddingTop + (height - minValue) / 2.0f;
        float right = left + minValue;
        float bottom = top + minValue;

        // 更新圆心坐标
        centerX = left + minValue / 2.0f;
        centerY = top + minValue / 2.0f;

        // 重用RectF对象，避免在onDraw中创建新对象
        drawRect.set(left, top, right, bottom);

        int sectorCount = beans.size();
        int angle = initAngle;

        // 绘制扇形
        for (int i = 0; i < sectorCount; i++) {
            LuckyWheelCardBean bean = beans.get(i);
            if (bean != null) {
                int color = bean.getColor();
                if (i % 2 == 0) {
                    paint.setColor(color != 0 ? color : DEFAULT_COLOR_DARK);
                    canvas.drawArc(drawRect, angle, verPanRadius, true, paint);
                } else {
                    paint.setColor(color != 0 ? color : DEFAULT_COLOR_LIGHT);
                    canvas.drawArc(drawRect, angle, verPanRadius, true, paint);
                }
            }
            angle += verPanRadius;
        }

        drawText(canvas);
        drawBitmap(canvas);
        // 绘制中心按钮
        drawCenterButton(canvas);
    }

    private void drawBorder(@NonNull Canvas canvas) {
        if (borderWidth > 0) {
            // 绘制边框
            canvas.drawArc(borderRect, 0, 360, false, borderPaint);
        }
    }

    private void drawCenterButton(@NonNull Canvas canvas) {
        if (centerBitmap != null && !centerBitmap.isRecycled()) {
            // 更新中心按钮的绘制区域
            updateCenterButtonRect();
            // 绘制中心按钮
            canvas.drawBitmap(centerBitmap, null, centerBtnRect, null);
        }
    }

    private void drawEmptyWheel(Canvas canvas) {
        final int paddingLeft = getPaddingLeft();
        final int paddingRight = getPaddingRight();
        final int paddingTop = getPaddingTop();
        final int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;

        int minValue = Math.min(width, height);
        radius = minValue / 2;

        // 计算绘制矩形的正确位置，使其在可用空间内居中
        float left = paddingLeft + (width - minValue) / 2.0f;
        float top = paddingTop + (height - minValue) / 2.0f;
        float right = left + minValue;
        float bottom = top + minValue;

        // 更新圆心坐标
        centerX = left + minValue / 2.0f;
        centerY = top + minValue / 2.0f;

        drawRect.set(left, top, right, bottom);

        // 绘制一个默认的圆形
        Paint defaultPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        defaultPaint.setColor(Color.LTGRAY);
        canvas.drawCircle(centerX, centerY, radius, defaultPaint);

        // 绘制中心按钮（即使没有数据也绘制）
        drawCenterButton(canvas);

        // 绘制提示文字
        Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.DKGRAY);
        textPaint.setTextSize(DisplayMetricsHelper.dp2px(getContext(), 14));
        String text = "没有数据";
        float textWidth = textPaint.measureText(text);
        canvas.drawText(text, centerX - textWidth / 2, centerY, textPaint);
    }

    private void drawText(@NonNull Canvas canvas) {
        if (beans == null || beans.isEmpty()) {
            return;
        }
        int sectorCount = beans.size();
        int textAngle = initAngle;
        for (int i = 0; i < sectorCount; i++) {
            LuckyWheelCardBean bean = beans.get(i);
            if (bean != null && !TextUtils.isEmpty(bean.getContent())) {
                int min = Math.min(6, bean.getContent().length());
                String content = bean.getContent().substring(0, min);
                float textWidth = textPaint.measureText(content);

                float ratio = 1 / 8.0f;
                if (isTowardCenter) {
                    // 朝向圆心方向绘制：创建直线路径
                    textPath.reset();

                    // 计算当前扇形的中心角度
                    float centerAngle = textAngle + verPanRadius / 2.0f;
                    float centerAngleRad = (float) Math.toRadians(centerAngle);
                    // 计算文字的起点和终点
                    float startX = (float) (centerX + (radius - 12) * Math.cos(centerAngleRad));
                    float startY = (float) (centerY + (radius - 12) * Math.sin(centerAngleRad));
                    // 创建直线路径
                    textPath.moveTo(startX, startY);
                    textPath.lineTo(centerX, centerY);

                    canvas.drawTextOnPath(content, textPath, 0, 0, textPaint);
                } else {
                    // 当前效果：沿着圆弧绘制
                    textPath.reset();
                    textPath.addArc(drawRect, textAngle, verPanRadius);

                    // 圆弧的垂直偏移
                    float vOffset = (float) radius * ratio;
                    // 圆弧的水平偏移
                    float hOffset = (float) (radius * (1.0f - ratio) * Math.PI / sectorCount - textWidth / 2);

                    canvas.drawTextOnPath(content, textPath, hOffset, vOffset, textPaint);
                }
            }
            textAngle += verPanRadius;
        }
    }

    private void drawBitmap(@NonNull Canvas canvas) {
        if (beans == null || beans.isEmpty()) {
            return;
        }
        int sectorCount = beans.size();
        // 计算每个图标的起始角度（扇形中心角度）
        float startAngle = initAngle + diffRadius; // diffRadius = verPanRadius / 2

        for (int i = 0; i < sectorCount; i++) {
            Bitmap bitmap = beans.get(i).getBitmap();
            if (bitmap == null || bitmap.isRecycled()) {
                continue;
            }
            // 计算当前扇形中心的角度（弧度）
            float centerAngle = startAngle + i * verPanRadius;
            float centerAngleRad = (float) Math.toRadians(centerAngle);

            // 计算图标位置（在半径的 2/3 处）
            float iconRadius = radius * 2.0f / 3.0f;
            float x = centerX + iconRadius * (float) Math.cos(centerAngleRad);
            float y = centerY + iconRadius * (float) Math.sin(centerAngleRad);

            float halfSize = iconSize / 2.0f;
            iconRect.set(x - halfSize, y - halfSize, x + halfSize, y + halfSize);

            canvas.drawBitmap(bitmap, null, iconRect, null);
        }
    }

    /**
     * 检查触摸点是否在圆形区域外
     *
     * @param x 触摸点x坐标
     * @param y 触摸点y坐标
     * @return true-在圆形区域外，false-在圆形区域内
     */
    private boolean isPointOutWheel(float x, float y) {
        if (radius <= 0) {
            return false;
        }

        // 计算触摸点到圆心的距离
        float dx = x - centerX;
        float dy = y - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        // 如果距离大于半径，说明在圆形区域外; 圆心按钮只允许点击
        return distance > radius || isPointInCenterButton(x, y);
    }

    /**
     * 检查触摸点是否在中心按钮范围内
     *
     * @param x 触摸点x坐标
     * @param y 触摸点y坐标
     * @return true-在中心按钮范围内，false-不在
     */
    private boolean isPointInCenterButton(float x, float y) {
        if (centerBitmap == null || centerBitmap.isRecycled()) {
            return false;
        }

        // 检查触摸点是否在中心按钮的圆形范围内
        float dx = x - centerX;
        float dy = y - centerY;
        float distance = (float) Math.sqrt(dx * dx + dy * dy);

        return distance <= (centerBtnSize / 2.0f);
    }

    /**
     * 开始转动
     *
     * @param pos 如果 pos = -1 则随机，如果指定某个值，则转到某个指定区域
     */
    public void startRotate(int pos) {
        if (beans == null || beans.isEmpty()) {
            return;
        }

        // 禁用触摸交互
        isTouchEnabled = false;

        // Rotate lap
        int lap = (int) (Math.random() * 6) + 3;

        int angle = 0;
        if (pos < 0) {
            angle = (int) (Math.random() * 360);
            int currentAngle = initAngle % 360;
            if (currentAngle < 0) currentAngle += 360;
            // 计算需要旋转的角度差
            angle = angle - currentAngle;
            if (angle < 0) {
                angle += 360;
            }
        } else {
            int initPos = queryPosition();
            if (pos > initPos) {
                angle = (pos - initPos) * verPanRadius;
                lap -= 1;
                angle = 360 - angle;
            } else if (pos < initPos) {
                angle = (initPos - pos) * verPanRadius;
            }
            // 如果相等，不做处理
        }

        int increaseDegree = lap * 360 + angle;
        long time = (lap + angle / 360) * ONE_WHEEL_TIME;
        int desRotate = increaseDegree + initAngle;

        ValueAnimator animator = ValueAnimator.ofInt(initAngle, desRotate);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.setDuration(time);
        animator.addUpdateListener(animation -> {
            int updateValue = (int) animation.getAnimatedValue();
            int newAngle = (updateValue % 360 + 360) % 360;
            if (initAngle != newAngle) {
                initAngle = newAngle;
                invalidate();
            }
        });

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                // 重新启用触摸交互
                isTouchEnabled = true;
                if (animationEndListener == null || beans == null || beans.isEmpty()) {
                    return;
                }
                int pos = queryPosition();
                if (pos >= beans.size() || pos < 0) {
                    return;
                }
                animationEndListener.endAnimation(getContext(), beans.get(pos));
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                super.onAnimationCancel(animation);
                isTouchEnabled = true;
            }
        });
        animator.start();
    }

    /**
     * 开始随机转动
     */
    public void startRandomRotate() {
        startRotate(-1);
    }

    private int queryPosition() {
        if (beans == null || beans.isEmpty()) {
            return 0;
        }

        int sectorCount = beans.size();
        int sectorAngle = verPanRadius; // 每个扇形的角度 = 360 / sectorCount

        // 将 initAngle 规范化到 [0, 360) 范围
        int normalizedAngle = ((initAngle % 360) + 360) % 360;

        // Android 坐标系中，0度在右侧，90度在底部，180度在左侧，270度在顶部
        // 我们需要找到顶部（270度）对应的扇形

        // 计算当前顶部位置对应的角度
        // 顶部角度 = 270度，转盘旋转后，原来的顶部现在在 (270 - normalizedAngle) 度
        int topAngle = (270 - normalizedAngle) % 360;
        if (topAngle < 0) {
            topAngle += 360;
        }

        // 计算扇形索引（从0开始）
        int pos = topAngle / sectorAngle;

        // 确保 pos 在有效范围内
        if (pos >= sectorCount) {
            pos = sectorCount - 1;
        } else if (pos < 0) {
            pos = 0;
        }
        return pos;
    }

    @Override
    protected void onDetachedFromWindow() {
        // 停止所有旋转
        stopAllRotations();

        clearAnimation();
        // 释放资源
        if (this.releaseOnSelfDetached) {
            releaseOldBitmaps();
            if (centerBitmap != null && !centerBitmap.isRecycled()) {
                centerBitmap.recycle();
                centerBitmap = null;
            }
        }
        super.onDetachedFromWindow();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果触摸被禁用，直接返回false
        if (!isTouchEnabled) {
            return false;
        }

        float x = event.getX();
        float y = event.getY();

        // 首先检查是否点击了中心按钮
        if (isPointInCenterButton(x, y)) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    return true;

                case MotionEvent.ACTION_UP:
                    // 触发中心按钮点击事件
                    performClick();
                    startRandomRotate();
                    return true;
            }
        }

        // 检查触摸点是否在圆形区域外
        if (isPointOutWheel(x, y)) {
            // 如果不在圆形区域内，不处理触摸事件
            return super.onTouchEvent(event);
        }

        // 使用detector处理手势
        if (detector.onTouchEvent(event)) {
            if (getParent() != null && getParent().getParent() != null) {
                getParent().getParent().requestDisallowInterceptTouchEvent(true);
            }
            // 如果处理了点击事件，调用 performClick() 确保辅助功能正常工作
            if (event.getAction() == MotionEvent.ACTION_UP) {
                performClick();
            }
            return true;
        }

        // 调用父类处理
        boolean superResult = super.onTouchEvent(event);

        // 如果是点击事件，确保调用 performClick()
        if (event.getAction() == MotionEvent.ACTION_UP) {
            performClick();
        }

        return superResult;
    }

    @Override
    public boolean performClick() {
        // 调用父类的 performClick 以确保辅助功能正常工作
        super.performClick();
        return true;
    }

    public void setRotate(int rotation) {
        rotation = (rotation % 360 + 360) % 360;
        if (initAngle != rotation) {
            initAngle = rotation;
            invalidate();
        }
    }

    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            // 还在滚动中，更新角度
            int rotation = scroller.getCurrY();
            rotation = (rotation % 360 + 360) % 360;

            if (initAngle != rotation) {
                initAngle = rotation;
                invalidate();
            }

            // 继续滚动
            postInvalidateOnAnimation();
        } else {
            // 滚动结束，检查是否需要触发回调
            if (!isFlingFinishedCallbackFired) {
                isFlingFinishedCallbackFired = true;
                // 滚动结束，重新启用触摸
                isTouchEnabled = true;

                // 延迟一小段时间确保动画完全停止
                postDelayed(() -> {
                    if (animationEndListener != null && beans != null && !beans.isEmpty()) {
                        int pos = queryPosition();
                        if (pos >= 0 && pos < beans.size()) {
                            animationEndListener.endAnimation(getContext(), beans.get(pos));
                        }
                    }
                }, 50); // 50ms 延迟确保转盘完全停止
            }
        }
        super.computeScroll();
    }

    private class RotatePanGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(@NonNull MotionEvent e) {
            // 如果触摸被禁用，不处理手势
            if (!isTouchEnabled) {
                return false;
            }

            // 检查触摸点是否在中心按钮上
            if (isPointInCenterButton(e.getX(), e.getY())) {
                return false; // 中心按钮事件单独处理
            }

            // 检查触摸点是否在圆形区域内
            if (isPointOutWheel(e.getX(), e.getY())) {
                return false; // 不在圆形区域内，不处理
            }

            if (!scroller.isFinished()) {
                scroller.abortAnimation();
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            float centerX = (getLeft() + getRight()) * 0.5f;
            float centerY = (getTop() + getBottom()) * 0.5f;

            float scrollTheta = vectorToScalarScroll(distanceX, distanceY,
                e2.getX() - centerX, e2.getY() - centerY);
            int rotate = initAngle - (int) scrollTheta / FLING_VELOCITY_DOWNSCALE;

            setRotate(rotate);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float centerX = (getLeft() + getRight()) * 0.5f;
            float centerY = (getTop() + getBottom()) * 0.5f;

            float scrollTheta = vectorToScalarScroll(velocityX, velocityY,
                e2.getX() - centerX, e2.getY() - centerY);

            scroller.abortAnimation();
            // fling开始时禁用触摸
            isTouchEnabled = false;
            // 开始 fling，重置回调标志
            isFlingFinishedCallbackFired = false;
            scroller.fling(0, initAngle, 0, (int) scrollTheta / FLING_VELOCITY_DOWNSCALE,
                0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            // 开始滚动
            postInvalidateOnAnimation();
            return true;
        }
    }

    /**
     * 判断滑动的方向
     *
     * @param dx dx
     * @param dy dy
     * @param x  x
     * @param y  y
     * @return float
     */
    private float vectorToScalarScroll(float dx, float dy, float x, float y) {
        float l = (float) Math.sqrt(dx * dx + dy * dy);
        float dot = (-y * dx + x * dy);
        float sign = Math.signum(dot);
        return l * sign;
    }

    /**
     * 停止所有旋转和滚动，重置触摸状态
     */
    public void stopAllRotations() {
        // 停止动画
        clearAnimation();

        // 停止scroller
        if (!scroller.isFinished()) {
            scroller.abortAnimation();
            scroller.forceFinished(true);
        }

        // 重置所有状态
        isTouchEnabled = true;
        isFlingFinishedCallbackFired = true;
    }

    private void releaseOldBitmaps() {
        if (this.beans != null && !this.beans.isEmpty()) {
            for (LuckyWheelCardBean bean : this.beans) {
                if (bean == null) {
                    continue;
                }
                Bitmap bitmap = bean.getBitmap();
                if (bitmap != null && !bitmap.isRecycled()) {
                    bitmap.recycle();
                    bean.setBitmap(null);
                }
            }
        }
    }

    /**
     * 控件onDetachedFromWindow时是否释放自身资源，默认不释放
     * 在列表中使用时，item的ViewHolder有自身的声明周期管理，在{@link LuckyWheelCard#release()}中释放，避免滚动列表后显示异常
     * 在固定的页面则需要调用 releaseOnSelfDetached = true 避免内存泄露
     *
     * @param releaseOnSelfDetached true：释放 false 不释放
     */
    public void releaseOnSelfDetached(boolean releaseOnSelfDetached) {
        this.releaseOnSelfDetached = releaseOnSelfDetached;
    }

    public interface AnimationEndListener {
        void endAnimation(Context context, LuckyWheelCardBean bean);
    }

    public void setAnimationEndListener(AnimationEndListener listener) {
        this.animationEndListener = listener;
    }

    private void loadBitmapsAsync(LoadBitmapsCallback callback) {
        if (beans == null || beans.isEmpty()) {
            if (callback != null) callback.onAllLoaded();
            return;
        }

        // 使用原子计数器保证线程安全
        AtomicInteger counter = new AtomicInteger(beans.size());

        for (LuckyWheelCardBean bean : beans) {
            if (bean == null || TextUtils.isEmpty(bean.getImageUrl())) {
                if (counter.decrementAndGet() == 0) {
                    callback.onAllLoaded();
                }
                continue;
            }

            Glide.with(getContext())
                .asBitmap()
                .load(bean.getImageUrl())
                .override(iconSize, iconSize)
                .centerCrop()
                .into(new CustomTarget<Bitmap>(iconSize, iconSize) {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        bean.setBitmap(resource);
                        if (counter.decrementAndGet() == 0) {
                            callback.onAllLoaded();
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        bean.setBitmap(null);
                        if (counter.decrementAndGet() == 0) {
                            callback.onAllLoaded();
                        }
                    }
                });
        }
    }

    private interface LoadBitmapsCallback {
        void onAllLoaded();
    }
}