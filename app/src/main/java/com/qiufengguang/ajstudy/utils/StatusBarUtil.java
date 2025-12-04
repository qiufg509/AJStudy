package com.qiufengguang.ajstudy.utils;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.WindowManager;

import androidx.annotation.Nullable;

public class StatusBarUtil {
    private static final String TAG = "StatusBarUtil";

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

        // 3. 设置为完全透明
        window.setStatusBarColor(Color.TRANSPARENT);

        // Android 11+ 的边到边显示
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            // 内容延伸到状态栏
            window.setDecorFitsSystemWindows(false);
            WindowInsetsController controller = getWindowInsetsController(activity);
            if (controller != null) {
                controller.hide(WindowInsets.Type.navigationBars());
            }
        } else {
            View decorView = window.getDecorView();
            decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            );
        }
    }

    /**
     * 设置状态栏文字icon深浅色
     *
     * @param activity Activity页面
     * @param light    true浅色 false深色
     */
    public static void setLightStatusBar(Activity activity, boolean light) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            WindowInsetsController controller = getWindowInsetsController(activity);
            if (controller != null) {
                controller.setSystemBarsAppearance(
                    light ? WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS : 0,
                    WindowInsetsController.APPEARANCE_LIGHT_STATUS_BARS
                );
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window window = getWindow(activity);
            if (window == null) {
                return;
            }
            View decorView = window.getDecorView();
            int flags = decorView.getSystemUiVisibility();
            if (light) {
                flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            } else {
                flags &= ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            }
            decorView.setSystemUiVisibility(flags);
        }
    }

    @Nullable
    private static WindowInsetsController getWindowInsetsController(Activity activity) {
        Window window = getWindow(activity);
        if (window == null) {
            Log.i(TAG, "The timing to obtain the window is incorrect.");
            return null;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return window.getInsetsController();
        } else if (Build.VERSION.SDK_INT == Build.VERSION_CODES.R) {
            return window.getDecorView().getWindowInsetsController();
        } else {
            Log.i(TAG, "Version does not support.");
            return null;
        }
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
