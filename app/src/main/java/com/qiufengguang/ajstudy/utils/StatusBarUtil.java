package com.qiufengguang.ajstudy.utils;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.view.WindowInsetsControllerCompat;

/**
 * 状态栏工具类
 * 1.透明状态栏
 * 2.状态栏深浅色
 *
 * @author qiufengguang
 * @since 2025/11/27 17:42
 */
public class StatusBarUtil {
    private static final String TAG = "StatusBarUtil";


    /**
     * 最近更新状态栏的时间
     */
    private static long lastStatusBarUpdateTime = 0;

    /**
     * 最小更新间隔50ms
     */
    private static final long STATUS_BAR_UPDATE_INTERVAL = 50;

    private StatusBarUtil() {
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }

    /**
     * 设置内容延伸到状态栏，状态栏背景透明
     * setContentView之后调用，否则window.getInsetsController()抛空指针异常
     *
     * @param activity Activity页面
     */
    public static void makeStatusBarTransparent(Activity activity) {
        Window window = getWindow(activity);
        if (window == null) {
            return;
        }

        // 1. 清除半透明标志
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        // 2. 允许绘制系统栏背景
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        // 3. 设置状态栏为完全透明
        window.setStatusBarColor(Color.TRANSPARENT);

        // 设置Window允许内容延伸到系统窗口区域
        // Android 5.0+ (API 21+)：使用 setDecorFitsSystemWindows(false)
        // Android 4.4 (API 19-20)：使用 SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | SYSTEM_UI_FLAG_LAYOUT_STABLE
        WindowCompat.setDecorFitsSystemWindows(window, false);

        // 隐藏导航栏（WindowInsetsControllerCompat内部做了兼容处理）
        WindowInsetsControllerCompat controllerCompat = WindowCompat.getInsetsController(
            window, window.getDecorView());
        controllerCompat.hide(WindowInsetsCompat.Type.navigationBars());
    }

    /**
     * 设置状态栏文字icon深浅色
     *
     * @param activity Activity页面
     * @param isLight  true浅色 false深色
     */
    public static void setLightStatusBar(Activity activity, boolean isLight) {
        setLightStatusBar(getWindow(activity), isLight);
    }

    /**
     * 设置状态栏文字icon深浅色
     *
     * @param window  当前窗口Window
     * @param isLight true浅色 false深色
     */
    public static void setLightStatusBar(Window window, boolean isLight) {
        if (window == null) {
            return;
        }

        // WindowInsetsControllerCompat内部做了兼容处理
        WindowInsetsControllerCompat controllerCompat = WindowCompat.getInsetsController(
            window, window.getDecorView());
        controllerCompat.setAppearanceLightStatusBars(isLight);
        // 透明导航栏：controllerCompat.setAppearanceLightNavigationBars(isLight);
    }

    /**
     * 节流更新状态栏
     * 避免频繁触发更新导致闪烁、性能问题
     *
     * @param activity Activity页面
     * @param color    状态栏背景色
     */
    public static void throttleUpdateStatusBarColor(Activity activity, @ColorInt int color) {
        throttleUpdateStatusBarColor(getWindow(activity), color);
    }

    /**
     * 节流更新状态栏
     * 避免频繁触发更新导致闪烁、性能问题
     *
     * @param window 当前窗口Window
     * @param color  状态栏背景色
     */
    public static void throttleUpdateStatusBarColor(Window window, @ColorInt int color) {
        if (window == null) {
            return;
        }
        long currentTime = System.currentTimeMillis();

        // 检查是否达到最小更新间隔
        if (currentTime - lastStatusBarUpdateTime >= STATUS_BAR_UPDATE_INTERVAL) {
            window.setStatusBarColor(color);
            lastStatusBarUpdateTime = currentTime;
        }
    }

    /**
     * 适配标题栏，使其填充到状态栏（设置内边距+调整总高度）
     *
     * @param titleBar 标题栏根布局
     * @return 适配后标题栏总高度
     */
    public static int adaptTitleBar(@NonNull View titleBar) {
        int statusBarHeight = DisplayMetricsHelper.getStatusBarHeight(titleBar.getContext());
        int titleBarHeight = DisplayMetricsHelper.getTitleBarHeight(titleBar.getContext());
        titleBar.setPadding(
            titleBar.getPaddingLeft(),
            statusBarHeight,
            titleBar.getPaddingRight(),
            titleBar.getPaddingBottom()
        );
        // 设置titleBar高度为titleBarHeight + 状态栏高度
        int totalHeight = titleBarHeight + statusBarHeight;
        ViewGroup.LayoutParams toolbarParams = titleBar.getLayoutParams();
        toolbarParams.height = totalHeight;
        titleBar.setLayoutParams(toolbarParams);
        return totalHeight;
    }

    @Nullable
    private static Window getWindow(Activity activity) {
        if (activity == null) {
            Log.i(TAG, "activity should not be null.");
            return null;
        }
        return activity.getWindow();
    }
}
