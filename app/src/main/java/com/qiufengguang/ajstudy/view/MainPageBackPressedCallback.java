package com.qiufengguang.ajstudy.view;

import android.app.Activity;

import androidx.activity.OnBackPressedCallback;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;

import com.qiufengguang.ajstudy.R;

import java.lang.ref.WeakReference;

/**
 * 主页面返回操作处理
 *
 * @author qiufengguang
 * @since 2025/12/14 16:27
 */
public class MainPageBackPressedCallback extends OnBackPressedCallback {

    private final WeakReference<Activity> activityRefer;

    public MainPageBackPressedCallback(boolean enabled, Activity activity) {
        super(enabled);
        this.activityRefer = new WeakReference<>(activity);
    }

    @Override
    public void handleOnBackPressed() {
        if (this.activityRefer == null || this.activityRefer.get() == null) {
            return;
        }
        NavController navController = Navigation.findNavController(
            this.activityRefer.get(), R.id.nav_host_fragment_activity_main);

        NavDestination currentDestination = navController.getCurrentDestination();
        if (currentDestination == null) {
            return;
        }
        if (currentDestination.getId() == R.id.navigation_home) {
            this.activityRefer.get().finish();
            return;
        }
        // 非首页：直接回到首页
        NavOptions navOptions = new NavOptions.Builder()
            .setPopUpTo(R.id.navigation_home, false)
            .setLaunchSingleTop(true)
            .build();
        navController.navigate(R.id.navigation_home, null, navOptions);
    }
}
