package com.qiufengguang.ajstudy.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.LruCache;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.FloatRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qiufengguang.ajstudy.R;

import java.util.Locale;

/**
 * 圆形方向控制控件 (极致性能优化版)
 * <p>
 * 优化方案：
 * 1. 静态位图池 (Static LruCache)：利用全局静态缓存共享烘焙后的位图。相同规格的卡片不再重复创建位图，
 * 彻底消除滚动过程中的 Bitmap 分配和 GC 抖动。
 * 2. 移除 recycle() 停顿：滑动出屏时仅解绑引用，不再手动 recycle，避免 GPU 同步导致的掉帧。
 * 3. 硬件加速友好：保持硬件加速，仅在生成离屏缓存时使用软件层处理阴影。
 * 4. 零分配 onDraw：绘图路径中无任何对象分配。
 *
 * @author qiufengguang
 * @since 2026/2/11
 */
public class CircleCtrl extends View {

    // 全局静态位图缓存，最大存储 20 张规格不同的位图
    private static final LruCache<String, Bitmap> S_BITMAP_CACHE = new LruCache<>(20);

    // 画笔 (预配置)
    private final Paint diskPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint thumbPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint dashedLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint trianglePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint sectorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    // 几何参数
    private float centerX, centerY;
    private float diskRadius;
    private float thumbRadius;
    private float maxThumbMoveRadius;

    // 当前位置与角度
    private float thumbX, thumbY;
    private float currentAngle = 0f;

    // 状态记录
    private boolean isDragging = false;

    // 配置属性
    private int diskColor = Color.WHITE;
    private int thumbColorStart = 0xFF2196F3;
    private int thumbColorEnd = 0xFF1976D2;
    private int sectorColorStart = 0x882196F3;
    private int sectorColorEnd = 0x002196F3;
    private float sectorSweepAngle = 60f;

    // 当前引用的缓存位图
    private Bitmap diskCacheBitmap;
    private Bitmap thumbCacheBitmap;

    // 辅助对象
    private final Path tempPath = new Path();
    private final RectF diskRectF = new RectF();

    private ValueAnimator reboundAnimator;
    private OnDirectionPadListener listener;

    public interface OnDirectionPadListener {
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
        if (attrs != null) {
            try (TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CircleCtrl)) {
                diskColor = a.getColor(R.styleable.CircleCtrl_diskColor, Color.WHITE);
                thumbColorStart = a.getColor(R.styleable.CircleCtrl_thumbColorStart, 0xFF2196F3);
                thumbColorEnd = a.getColor(R.styleable.CircleCtrl_thumbColorEnd, 0xFF1976D2);
                sectorColorStart = a.getColor(R.styleable.CircleCtrl_sectorColorStart, 0x882196F3);
                sectorColorEnd = a.getColor(R.styleable.CircleCtrl_sectorColorEnd, 0x002196F3);
                sectorSweepAngle = a.getFloat(R.styleable.CircleCtrl_sectorSweepAngle, 60f);
            }
        }

        diskPaint.setColor(diskColor);
        diskPaint.setStyle(Paint.Style.FILL);
        diskPaint.setShadowLayer(10, 0, 4, 0x44000000);

        thumbPaint.setStyle(Paint.Style.FILL);
        thumbPaint.setShadowLayer(8, 0, 2, 0x66000000);

        dashedLinePaint.setColor(0xFFCCCCCC);
        dashedLinePaint.setStyle(Paint.Style.STROKE);
        dashedLinePaint.setStrokeWidth(2);
        dashedLinePaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

        trianglePaint.setColor(Color.BLACK);
        trianglePaint.setStyle(Paint.Style.FILL);

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

        RadialGradient sectorGradient = new RadialGradient(centerX, centerY, diskRadius,
            new int[]{sectorColorStart, sectorColorEnd}, null, Shader.TileMode.CLAMP);
        sectorPaint.setShader(sectorGradient);

        // 获取缓存位图，不存在则在此生成
        loadBitmapsFromCache();
    }

    /**
     * 利用全局静态缓存，避免在列表滚动过程中重复创建位图。
     */
    private void loadBitmapsFromCache() {
        if (diskRadius <= 0) return;

        // 生成唯一标识 Key (规格 + 颜色)
        String diskKey = String.format(Locale.getDefault(), "disk_%.1f_%d", diskRadius, diskColor);
        diskCacheBitmap = S_BITMAP_CACHE.get(diskKey);
        if (diskCacheBitmap == null || diskCacheBitmap.isRecycled()) {
            int bgExtra = 25;
            int bgBmpSize = (int) ((diskRadius + bgExtra) * 2);
            diskCacheBitmap = Bitmap.createBitmap(bgBmpSize, bgBmpSize, Bitmap.Config.ARGB_8888);
            Canvas bgCanvas = new Canvas(diskCacheBitmap);
            float offset = bgBmpSize / 2f;
            bgCanvas.drawCircle(offset, offset - 2, diskRadius, diskPaint);
            bgCanvas.drawLine(offset, offset - diskRadius, offset, offset + diskRadius, dashedLinePaint);
            bgCanvas.drawLine(offset - diskRadius, offset, offset + diskRadius, offset, dashedLinePaint);
            float triSize = diskRadius * 0.08f;
            float triOffset = triSize + 5;
            drawStaticTriangle(bgCanvas, offset, offset - diskRadius + triOffset, triSize, 0);
            drawStaticTriangle(bgCanvas, offset, offset + diskRadius - triOffset, triSize, 180);
            drawStaticTriangle(bgCanvas, offset - diskRadius + triOffset, offset, triSize, 270);
            drawStaticTriangle(bgCanvas, offset + diskRadius - triOffset, offset, triSize, 90);
            S_BITMAP_CACHE.put(diskKey, diskCacheBitmap);
        }

        String thumbKey = String.format(Locale.getDefault(), "thumb_%.1f_%d_%d", thumbRadius, thumbColorStart, thumbColorEnd);
        thumbCacheBitmap = S_BITMAP_CACHE.get(thumbKey);
        if (thumbCacheBitmap == null || thumbCacheBitmap.isRecycled()) {
            int thumbExtra = 20;
            int thumbBmpSize = (int) ((thumbRadius + thumbExtra) * 2);
            thumbCacheBitmap = Bitmap.createBitmap(thumbBmpSize, thumbBmpSize, Bitmap.Config.ARGB_8888);
            Canvas tCanvas = new Canvas(thumbCacheBitmap);
            float tCenter = thumbBmpSize / 2f;
            RadialGradient thumbGradient = new RadialGradient(tCenter, tCenter, thumbRadius,
                new int[]{thumbColorStart, thumbColorEnd}, null, Shader.TileMode.CLAMP);
            thumbPaint.setShader(thumbGradient);
            tCanvas.drawCircle(tCenter, tCenter, thumbRadius, thumbPaint);
            S_BITMAP_CACHE.put(thumbKey, thumbCacheBitmap);
        }
    }

    private void drawStaticTriangle(Canvas canvas, float x, float y, float size, float rotation) {
        canvas.save();
        canvas.translate(x, y);
        canvas.rotate(rotation);
        tempPath.reset();
        tempPath.moveTo(0, -size);
        tempPath.lineTo(-size / 1.5f, 0);
        tempPath.lineTo(size / 1.5f, 0);
        tempPath.close();
        canvas.drawPath(tempPath, trianglePaint);
        canvas.restore();
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        // 自动恢复检查
        if (diskCacheBitmap == null || diskCacheBitmap.isRecycled() || thumbCacheBitmap == null || thumbCacheBitmap.isRecycled()) {
            loadBitmapsFromCache();
        }

        // 1. 绘制静态背景位图 (极其高效的内存贴图)
        if (diskCacheBitmap != null) {
            canvas.drawBitmap(diskCacheBitmap, centerX - diskCacheBitmap.getWidth() / 2f,
                centerY - diskCacheBitmap.getHeight() / 2f + 2, null);
        }

        // 2. 绘制动态扇形 (开启硬件加速时，drawArc 的 CPU/GPU 负担极小)
        if (isDragging && (thumbX != centerX || thumbY != centerY)) {
            drawSector(canvas);
        }

        // 3. 绘制中心小圆位图
        if (thumbCacheBitmap != null) {
            canvas.drawBitmap(thumbCacheBitmap, thumbX - thumbCacheBitmap.getWidth() / 2f,
                thumbY - thumbCacheBitmap.getHeight() / 2f, null);
        }
    }

    private void drawSector(Canvas canvas) {
        float startAngle;
        float sweep = sectorSweepAngle;
        if (Math.abs(sweep - 90f) < 0.1f) {
            if (currentAngle >= 45 && currentAngle < 135) startAngle = 45;
            else if (currentAngle >= 135 && currentAngle < 225) startAngle = 135;
            else if (currentAngle >= 225 && currentAngle < 315) startAngle = 225;
            else startAngle = 315;
        } else if (Math.abs(sweep - 45f) < 0.1f) {
            startAngle = ((int) (currentAngle / 45f)) * 45f;
        } else {
            startAngle = currentAngle - sweep / 2f;
        }
        canvas.drawArc(diskRectF, startAngle, sweep, true, sectorPaint);
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
                if (parent != null) parent.requestDisallowInterceptTouchEvent(true);
                if (reboundAnimator != null && reboundAnimator.isRunning())
                    reboundAnimator.cancel();
                updatePosition(x, y, dx, dy);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (isDragging) updatePosition(x, y, dx, dy);
                return isDragging;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (isDragging) {
                    if (listener != null) listener.onAngleRevealed(getContext(), currentAngle);
                    isDragging = false;
                    startRebound();
                }
                return true;
        }
        return super.onTouchEvent(event);
    }

    private void updatePosition(float x, float y, float dx, float dy) {
        double radians = Math.atan2(dy, dx);
        currentAngle = (float) Math.toDegrees(radians);
        if (currentAngle < 0) currentAngle += 360;
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
        reboundAnimator.setDuration(100);
        reboundAnimator.setInterpolator(new DecelerateInterpolator());
        reboundAnimator.addUpdateListener(animation -> {
            float f = (float) animation.getAnimatedValue();
            thumbX = startX + (centerX - startX) * f;
            thumbY = startY + (centerY - startY) * f;
            invalidate();
        });
        reboundAnimator.start();
    }

    /**
     * 解除对缓存位图的引用。
     * 注意：由于采用全局静态 LruCache，不再手动调用 recycle()，
     * 这样可以消除滑动过程中回收位图造成的 CPU/GPU 同步抖动。
     */
    public void release() {
        diskCacheBitmap = null;
        thumbCacheBitmap = null;
        if (reboundAnimator != null) {
            reboundAnimator.cancel();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (reboundAnimator != null) {
            reboundAnimator.cancel();
        }
        // 滑出屏幕时不回收，保持在缓存池中实现复用
    }
}
