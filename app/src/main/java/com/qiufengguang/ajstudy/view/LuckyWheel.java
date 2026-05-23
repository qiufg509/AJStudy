package com.qiufengguang.ajstudy.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.LruCache;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.OverScroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.model.LuckyWheelCardBean;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.utils.DisplayMetricsHelper;
import com.qiufengguang.ajstudy.utils.ImageUtil;
import com.qiufengguang.ajstudy.utils.SpUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 旋转转盘控件
 * <p>
 * 1. 修复中心图标丢失：记录 centerBtnResId，并在 onAttachedToWindow 中自动恢复 centerBitmap。
 * 2. 解决崩溃：彻底移除对 Glide 位图的手动 recycle() 调用，交由 Glide 自动管理生命周期。
 * 3. 性能优化：静态边框阴影使用全局 LruCache 共享。
 * 4. 保持功能：图标位置随动，但自身角度保持正向 (Upright)。
 *
 * @author qiufengguang
 * @since 2026/2/4
 */
public class LuckyWheel extends View {
    private static final String TAG = "LuckyWheel";

    private static final int DEFAULT_COLOR_DARK = 0xFF8584;
    private static final int DEFAULT_COLOR_LIGHT = 0xFE6869;

    private static final LruCache<String, Bitmap> S_BORDER_CACHE = new LruCache<>(10);

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint borderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float initAngle = 0;
    private int radius = 0;
    private float verPanRadius;
    private float diffRadius;
    public static final int FLING_VELOCITY_DOWNSCALE = 4;

    private GestureDetector detector;
    private OverScroller scroller;

    private int iconSize;
    private int centerBtnSize;
    private int centerBtnResId = 0;
    private Bitmap centerBitmap;

    private int borderWidth;
    private int shadowWidth;

    private final RectF drawRect = new RectF();
    private final RectF borderRect = new RectF();
    private final Path textPath = new Path();
    private final RectF iconRect = new RectF();
    private final RectF centerBtnRect = new RectF();

    private float centerX = 0;
    private float centerY = 0;

    private static final long ONE_WHEEL_TIME = 500;
    private List<LuckyWheelCardBean> beans;
    private boolean isTouchEnabled = true;
    private boolean isTowardCenter = false;
    private static final int DEFAULT_MIN_SIZE = 200;

    private AnimationEndListener animationEndListener;
    private boolean isFlingFinishedCallbackFired = true;

    private static SoundPool soundPool;
    private static int tickSoundId;
    private static boolean soundPoolInitialized = false;
    private boolean enableTickSound = false;
    private int lastPlayedSectorIndex = -1;

    private final List<Target<?>> glideTargets = new ArrayList<>();

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

            centerBtnResId = a.getResourceId(R.styleable.LuckyWheel_centerBtn, 0);
            if (centerBtnResId != 0) {
                loadCenterButtonBitmap(context, centerBtnResId);
            }
            isTowardCenter = a.getBoolean(R.styleable.LuckyWheel_isTowardCenter, false);
        }
        detector = new GestureDetector(context, new RotatePanGestureListener());
        scroller = new OverScroller(context);

        borderWidth = DisplayMetricsHelper.dp2px(context, 8);
        shadowWidth = DisplayMetricsHelper.dp2px(context, 4);
        int padding = borderWidth + shadowWidth + 5;
        setPadding(padding, padding, padding, padding);

        initPaints();
        setClickable(true);
        // 只有边框阴影耗时，通过 LruCache 解决。View 开启硬件加速以提升列表滚动性能。
        setLayerType(LAYER_TYPE_HARDWARE, null);

        enableTickSound = SpUtils.getInstance().getBoolean(Constant.Sp.KEY_TICK_SOUND, false);
        initSoundPool(context.getApplicationContext());
    }

    private void initPaints() {
        paint.setStyle(Paint.Style.FILL);
        textPaint.setColor(Color.WHITE);
        textPaint.setLetterSpacing(0.05f);
        textPaint.setTextSize(DisplayMetricsHelper.dp2px(getContext(), 14));
        borderPaint.setStyle(Paint.Style.STROKE);
        borderPaint.setColor(0xFFF2F2F2);
        borderPaint.setStrokeWidth(borderWidth);
        borderPaint.setShadowLayer(shadowWidth, 0, 0, 0x80000000);
    }

    public void setBeans(List<LuckyWheelCardBean> beans) {
        if (beans == null || beans.isEmpty()) return;
        this.beans = new ArrayList<>(beans);
        int size = this.beans.size();
        verPanRadius = 360f / size;
        diffRadius = verPanRadius / 2f;
        loadBitmapsAsync(this::invalidate);
        requestLayout();
        invalidate();
        lastPlayedSectorIndex = -1;
    }

    private void loadCenterButtonBitmap(Context context, int resId) {
        if (centerBitmap != null && !centerBitmap.isRecycled()) centerBitmap.recycle();
        int densityDpi = DisplayMetricsHelper.getDensityDpi(context);
        centerBitmap = ImageUtil.loadBitmap(context, densityDpi, resId, centerBtnSize);
        updateCenterButtonRect();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int defaultSize = DisplayMetricsHelper.dp2px(getContext(), DEFAULT_MIN_SIZE);
        int size = Math.max(Math.min(widthSize, heightSize), defaultSize);
        setMeasuredDimension(size, size);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        final int pl = getPaddingLeft(), pr = getPaddingRight(), pt = getPaddingTop(), pb = getPaddingBottom();
        int aw = w - pl - pr, ah = h - pt - pb;
        int minValue = Math.min(aw, ah);
        radius = minValue / 2;
        float left = pl + (aw - minValue) / 2.0f, top = pt + (ah - minValue) / 2.0f;
        drawRect.set(left, top, left + minValue, top + minValue);
        borderRect.set(left, top, left + minValue, top + minValue);
        centerX = left + minValue / 2.0f;
        centerY = top + minValue / 2.0f;
        updateCenterButtonRect();
    }

    private void updateCenterButtonRect() {
        if (centerBitmap != null && !centerBitmap.isRecycled()) {
            float hw = centerBitmap.getWidth() / 2.0f, hh = centerBitmap.getHeight() / 2.0f;
            if (hw < hh) centerBtnRect.set(centerX - hw, centerY - hh - (hh - hw), centerX + hw, centerY + hw);
            else centerBtnRect.set(centerX - hw - (hw - hh), centerY - hh, centerX + hh, centerY + hh);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 关键：恢复中心指示器
        if ((centerBitmap == null || centerBitmap.isRecycled()) && centerBtnResId != 0) {
            loadCenterButtonBitmap(getContext(), centerBtnResId);
        }
        // 关键：恢复 Item 图片加载
        if (beans != null && !beans.isEmpty()) {
            boolean needReload = false;
            for (LuckyWheelCardBean bean : beans) {
                if (bean.getBitmap() == null && !TextUtils.isEmpty(bean.getImageUrl())) {
                    needReload = true;
                    break;
                }
            }
            if (needReload) loadBitmapsAsync(this::invalidate);
        }
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);
        if (radius <= 0) return;

        // 1. 绘制边框 (共享缓存)
        drawBorder(canvas);

        if (beans == null || beans.isEmpty()) {
            drawEmptyWheel(canvas);
            return;
        }

        // 2. 绘制扇形背景
        drawSectors(canvas);

        // 3. 绘制文字和图标 (图标本身不旋转)
        drawText(canvas);
        drawIcons(canvas);

        // 4. 绘制中心按钮
        if (centerBitmap != null && !centerBitmap.isRecycled()) {
            canvas.drawBitmap(centerBitmap, null, centerBtnRect, null);
        }
    }

    private void drawBorder(Canvas canvas) {
        String key = String.format(Locale.getDefault(), "border_r%d_w%d", radius, getWidth());
        Bitmap border = S_BORDER_CACHE.get(key);
        if (border == null || border.isRecycled()) {
            int size = Math.max(getWidth(), getHeight());
            if (size <= 0) return;
            border = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            Canvas c = new Canvas(border);
            c.drawArc(borderRect, 0, 360, false, borderPaint);
            S_BORDER_CACHE.put(key, border);
        }
        canvas.drawBitmap(border, 0, 0, null);
    }

    private void drawSectors(Canvas canvas) {
        int sectorCount = beans.size();
        canvas.save();
        canvas.rotate(initAngle, centerX, centerY);
        float angle = 0;
        for (int i = 0; i < sectorCount; i++) {
            LuckyWheelCardBean bean = beans.get(i);
            if (bean != null) {
                paint.setColor(i % 2 == 0 ? (bean.getColor() != 0 ? bean.getColor() : DEFAULT_COLOR_DARK) : (bean.getColor() != 0 ? bean.getColor() : DEFAULT_COLOR_LIGHT));
                canvas.drawArc(drawRect, angle, verPanRadius, true, paint);
            }
            angle += verPanRadius;
        }
        canvas.restore();
    }

    private void drawText(@NonNull Canvas canvas) {
        int sectorCount = beans.size();
        for (int i = 0; i < sectorCount; i++) {
            LuckyWheelCardBean bean = beans.get(i);
            if (bean != null && !TextUtils.isEmpty(bean.getContent())) {
                String content = bean.getContent().substring(0, Math.min(6, bean.getContent().length()));
                float tw = textPaint.measureText(content);
                float curAngle = initAngle + i * verPanRadius;

                if (isTowardCenter) {
                    textPath.reset();
                    float caRad = (float) Math.toRadians(curAngle + verPanRadius / 2.0f);
                    textPath.moveTo((float)(centerX + (radius - 12) * Math.cos(caRad)), (float)(centerY + (radius - 12) * Math.sin(caRad)));
                    textPath.lineTo(centerX, centerY);
                    canvas.drawTextOnPath(content, textPath, 0, 0, textPaint);
                } else {
                    textPath.reset();
                    textPath.addArc(drawRect, curAngle, verPanRadius);
                    canvas.drawTextOnPath(content, textPath, (float)(radius * 0.875 * Math.PI / sectorCount - tw / 2), radius * 0.125f, textPaint);
                }
            }
        }
    }

    private void drawIcons(@NonNull Canvas canvas) {
        int sectorCount = beans.size();
        for (int i = 0; i < sectorCount; i++) {
            Bitmap bitmap = beans.get(i).getBitmap();
            if (bitmap == null || bitmap.isRecycled()) continue;

            float curAngle = initAngle + i * verPanRadius + diffRadius;
            float caRad = (float) Math.toRadians(curAngle);
            float ir = radius * 0.66f;
            float x = centerX + ir * (float) Math.cos(caRad), y = centerY + ir * (float) Math.sin(caRad);
            float hs = iconSize / 2.0f;
            iconRect.set(x - hs, y - hs, x + hs, y + hs);
            // 直接在 Canvas 上绘制位图，图标保持正向
            canvas.drawBitmap(bitmap, null, iconRect, null);
        }
    }

    private void drawEmptyWheel(Canvas canvas) {
        paint.setColor(Color.LTGRAY);
        canvas.drawCircle(centerX, centerY, radius, paint);
    }

    public void startRotate(int pos) {
        if (beans == null || beans.isEmpty()) return;
        isTouchEnabled = false;
        int lap = (int) (Math.random() * 6) + 3;
        float angle = 0;
        if (pos < 0) {
            angle = (float) (Math.random() * 360);
            float cur = ((initAngle % 360) + 360) % 360;
            angle = (angle - cur + 360) % 360;
        } else {
            int initPos = queryPosition();
            if (pos > initPos) angle = 360 - (pos - initPos) * verPanRadius;
            else if (pos < initPos) angle = (initPos - pos) * verPanRadius;
        }
        float totalAngle = lap * 360 + angle;
        ValueAnimator animator = ValueAnimator.ofFloat(initAngle, initAngle + totalAngle);
        animator.setInterpolator(new DecelerateInterpolator());
        animator.setDuration((long) (totalAngle * ONE_WHEEL_TIME / 360));
        animator.addUpdateListener(animation -> {
            initAngle = (float) animation.getAnimatedValue();
            invalidate();
            checkAndPlayTick();
        });
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                isTouchEnabled = true;
                if (animationEndListener != null) {
                    int p = queryPosition();
                    if (p >= 0 && p < beans.size()) animationEndListener.endAnimation(getContext(), beans.get(p));
                }
            }
            @Override
            public void onAnimationCancel(Animator animation) { isTouchEnabled = true; }
        });
        animator.start();
    }

    public void startRandomRotate() { startRotate(-1); }

    private int queryPosition() {
        if (beans == null || beans.isEmpty()) return 0;
        float norm = ((initAngle % 360) + 360) % 360;
        float top = (270 - norm + 360) % 360;
        int pos = (int) (top / verPanRadius);
        return Math.min(pos, beans.size() - 1);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isTouchEnabled) return false;
        float x = event.getX(), y = event.getY();
        if (isPointInCenterButton(x, y)) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                performClick();
                startRandomRotate();
            }
            return true;
        }
        if (isPointOutWheel(x, y)) return super.onTouchEvent(event);
        if (detector.onTouchEvent(event)) {
            if (getParent() != null) getParent().requestDisallowInterceptTouchEvent(true);
            return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() { return super.performClick(); }

    private boolean isPointOutWheel(float x, float y) {
        return Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)) > radius;
    }

    private boolean isPointInCenterButton(float x, float y) {
        return Math.sqrt(Math.pow(x - centerX, 2) + Math.pow(y - centerY, 2)) <= (centerBtnSize / 2.0f);
    }

    public void setRotate(float rotation) {
        initAngle = rotation;
        invalidate();
        checkAndPlayTick();
    }

    private void clearGlideTasks() {
        Context ctx = getContext();
        if (!(ctx instanceof AppCompatActivity act) || act.isFinishing() || act.isDestroyed()) {
            glideTargets.clear();
            return;
        }
        for (Target<?> t : glideTargets) {
            try {
                Glide.with(ctx).clear(t);
            } catch (Exception ignored) {}
        }
        glideTargets.clear();
    }

    /**
     * 显式释放实例级资源。
     * 重要：不再手动回收 beans 内部位图。
     */
    public void release() {
        clearGlideTasks();
        if (centerBitmap != null && !centerBitmap.isRecycled()) {
            centerBitmap.recycle();
            centerBitmap = null;
        }
    }

    public void stopAllRotations() {
        if (!scroller.isFinished()) scroller.abortAnimation();
        isTouchEnabled = true;
        isFlingFinishedCallbackFired = true;
    }

    public interface AnimationEndListener {
        void endAnimation(Context context, LuckyWheelCardBean bean);
    }

    public void setAnimationEndListener(AnimationEndListener listener) {
        this.animationEndListener = listener;
    }

    private void loadBitmapsAsync(Runnable callback) {
        if (beans == null || beans.isEmpty()) {
            if (callback != null) callback.run();
            return;
        }
        AtomicInteger counter = new AtomicInteger(beans.size());
        for (LuckyWheelCardBean bean : beans) {
            if (bean == null || TextUtils.isEmpty(bean.getImageUrl())) {
                if (counter.decrementAndGet() == 0 && callback != null) callback.run();
                continue;
            }
            CustomTarget<Bitmap> target = new CustomTarget<Bitmap>(iconSize, iconSize) {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    bean.setBitmap(resource);
                    if (counter.decrementAndGet() == 0 && callback != null) callback.run();
                }
                @Override
                public void onLoadCleared(@Nullable Drawable p) {
                    bean.setBitmap(null);
                }
            };
            glideTargets.add(target);
            Glide.with(getContext()).asBitmap().load(bean.getImageUrl()).override(iconSize, iconSize).centerCrop().into(target);
        }
    }

    private static void initSoundPool(Context context) {
        if (soundPoolInitialized) return;
        synchronized (LuckyWheel.class) {
            if (soundPoolInitialized) return;
            try {
                AudioAttributes attr = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME).setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
                soundPool = new SoundPool.Builder().setMaxStreams(1).setAudioAttributes(attr).build();
                AssetFileDescriptor afd = context.getAssets().openFd(Constant.LUCK_WHEEL_TICK_FILE);
                tickSoundId = soundPool.load(afd, 1);
                soundPoolInitialized = true;
            } catch (IOException ignored) {}
        }
    }

    private void checkAndPlayTick() {
        if (!enableTickSound || beans == null || beans.isEmpty()) return;
        int current = queryPosition();
        if (lastPlayedSectorIndex != -1 && lastPlayedSectorIndex != current) {
            if (soundPool != null && tickSoundId != 0) soundPool.play(tickSoundId, 1.0f, 1.0f, 1, 0, 1.0f);
        }
        lastPlayedSectorIndex = current;
    }

    @Override
    protected void onDetachedFromWindow() {
        if (!scroller.isFinished()) scroller.abortAnimation();
        super.onDetachedFromWindow();
    }

    private class RotatePanGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(@NonNull MotionEvent e) {
            if (!isTouchEnabled || isPointInCenterButton(e.getX(), e.getY()) || isPointOutWheel(e.getX(), e.getY())) return false;
            if (!scroller.isFinished()) scroller.abortAnimation();
            return true;
        }
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
            float st = vectorToScalarScroll(dx, dy, e2.getX() - centerX, e2.getY() - centerY);
            setRotate(initAngle - st / FLING_VELOCITY_DOWNSCALE);
            return true;
        }
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float vx, float vy) {
            float st = vectorToScalarScroll(vx, vy, e2.getX() - centerX, e2.getY() - centerY);
            scroller.abortAnimation();
            isTouchEnabled = false;
            isFlingFinishedCallbackFired = false;
            scroller.fling(0, (int)initAngle, 0, (int) st / FLING_VELOCITY_DOWNSCALE, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
            postInvalidateOnAnimation();
            return true;
        }
    }

    private float vectorToScalarScroll(float dx, float dy, float x, float y) {
        float l = (float) Math.sqrt(dx * dx + dy * dy);
        float dot = (-y * dx + x * dy);
        return l * Math.signum(dot);
    }
    
    @Override
    public void computeScroll() {
        if (scroller.computeScrollOffset()) {
            setRotate(scroller.getCurrY());
            postInvalidateOnAnimation();
        } else if (!isFlingFinishedCallbackFired) {
            isFlingFinishedCallbackFired = true;
            isTouchEnabled = true;
            postDelayed(() -> {
                if (animationEndListener != null && beans != null) {
                    int pos = queryPosition();
                    if (pos >= 0 && pos < beans.size()) animationEndListener.endAnimation(getContext(), beans.get(pos));
                }
            }, 50);
        }
        super.computeScroll();
    }
}
