package com.qiufengguang.ajstudy.utils;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

/**
 * 系统栏高度工具类
 * 提供获取状态栏和ActionBar高度的最新兼容方法，支持至SDK 29。
 */
public class DisplayMetricsHelper {

    private DisplayMetricsHelper() {
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }

    /**
     * 获取状态栏高度（推荐方法）。
     * 此方法优先使用精确的 WindowInsets，失败时回退到资源查询。
     *
     * @param context 上下文，最好使用 Activity
     * @return 状态栏高度（像素）
     */
    public static int getStatusBarHeight(@NonNull Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            View decorView = activity.getWindow().getDecorView();

            // 使用 AndroidX 兼容库 WindowInsetsCompat，增强兼容性
            WindowInsetsCompat insets = ViewCompat.getRootWindowInsets(decorView);
            if (insets != null) {
                // 获取系统窗口插入的顶部距离，即状态栏高度
                int topInset = insets.getInsets(WindowInsetsCompat.Type.systemBars()).top;
                if (topInset > 0) {
                    return topInset;
                }
            }
        }

        // 方法2: 回退方案，通过资源名称获取（传统兼容方法）
        return getStatusBarHeightByResource(context);
    }

    /**
     * 通过系统资源标识符获取状态栏高度。
     * 这是最常用的传统方法，兼容所有API版本[citation:1]。
     *
     * @param context 上下文
     * @return 状态栏高度（像素），未找到则返回0
     */
    private static int getStatusBarHeightByResource(@NonNull Context context) {
        int result = 0;
        @SuppressLint({"DiscouragedApi", "InternalInsetResource"})
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId != 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 获取 ActionBar 的高度。
     * 注意：此方法仅在ActionBar已创建和测量后调用才有效。
     *
     * @param context 上下文，必须为 Activity 或 AppCompatActivity
     * @return ActionBar高度（像素），如果未找到或未初始化则返回0
     */
    public static int getActionBarHeight(@NonNull Context context) {
        // 方案1: 如果ActionBar已实例化，直接获取其高度（最准确）
        if (context instanceof AppCompatActivity) {
            androidx.appcompat.app.ActionBar actionBar = ((AppCompatActivity) context).getSupportActionBar();
            if (actionBar != null && actionBar.getHeight() > 0) {
                return actionBar.getHeight();
            }
        } else if (context instanceof Activity) {
            android.app.ActionBar actionBar = ((Activity) context).getActionBar();
            if (actionBar != null && actionBar.getHeight() > 0) {
                return actionBar.getHeight();
            }
        }

        // 方案2: 通过解析主题中的 actionBarSize 属性获取
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        // 先尝试解析AndroidX兼容库的属性
        if (context.getTheme().resolveAttribute(androidx.appcompat.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                context.getResources().getDisplayMetrics());
        }
        // 如果失败，尝试解析系统原生属性 (API 11+)
        else if (context.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,
                context.getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(Context context, float dpValue) {
        if (context == null) {
            return (int) (dpValue * Resources.getSystem().getDisplayMetrics().density + 0.5f);
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(Context context, float pxValue) {
        if (context == null) {
            return (int) (pxValue / Resources.getSystem().getDisplayMetrics().density + 0.5f);
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param context 上下文
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px(Context context, float spValue) {
        if (context == null) {
            return (int) (spValue * Resources.getSystem().getDisplayMetrics().scaledDensity + 0.5f);
        }
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param context 上下文
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp(Context context, float pxValue) {
        if (context == null) {
            return (int) (pxValue / Resources.getSystem().getDisplayMetrics().scaledDensity + 0.5f);
        }
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    /**
     * 使用TypedValue进行dp转px（更精确）
     *
     * @param context 上下文
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2pxByTypedValue(Context context, float dpValue) {
        return (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dpValue,
            context.getResources().getDisplayMetrics()
        );
    }

    /**
     * 使用TypedValue进行sp转px（更精确）
     *
     * @param context 上下文
     * @param spValue sp值
     * @return px值
     */
    public static int sp2pxByTypedValue(Context context, float spValue) {
        return (int) TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            spValue,
            context.getResources().getDisplayMetrics()
        );
    }

    /**
     * 获取屏幕宽度（px）
     *
     * @param context 上下文
     * @return 屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels;
    }

    /**
     * 获取屏幕高度（px）
     *
     * @param context 上下文
     * @return 屏幕高度
     */
    public static int getScreenHeight(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels;
    }

    /**
     * 获取屏幕宽度（dp）
     *
     * @param context 上下文
     * @return 屏幕宽度（dp）
     */
    public static float getScreenWidthDp(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels / dm.density;
    }

    /**
     * 获取屏幕高度（dp）
     *
     * @param context 上下文
     * @return 屏幕高度（dp）
     */
    public static float getScreenHeightDp(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.heightPixels / dm.density;
    }
}