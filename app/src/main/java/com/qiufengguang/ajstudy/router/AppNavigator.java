package com.qiufengguang.ajstudy.router;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;

import com.qiufengguang.ajstudy.activity.detail.DetailActivity;
import com.qiufengguang.ajstudy.activity.main.MainActivity;
import com.qiufengguang.ajstudy.activity.markdown.MarkdownActivity;
import com.qiufengguang.ajstudy.activity.second.SecondActivity;

/**
 * 应用统一导航管理器
 *
 * @author qiufengguang
 * @since 2026/2/25 1:18
 */
public final class AppNavigator {
    private static final String TAG = "AppNavigator";

    private static volatile AppNavigator instance;

    private AppNavigator() {
    }

    /**
     * 初始化方法
     */
    public static void init() {
        if (instance == null) {
            synchronized (AppNavigator.class) {
                if (instance == null) {
                    instance = new AppNavigator();
                }
            }
        }
    }

    /**
     * 获取单例实例
     */
    public static AppNavigator getInstance() {
        if (instance == null) {
            throw new IllegalStateException("AppNavigator must be initialized first");
        }
        return instance;
    }

    // ==================== Activity 跳转基础方法 ====================

    /**
     * 启动 Activity（基础方法，支持结果回调）
     *
     * @param context  上下文（若使用 launcher，必须为 Activity 实例）
     * @param intent   已构建好的 Intent
     * @param launcher 如果需要返回结果，传入已注册的 ActivityResultLauncher；否则传 null
     */
    public void startActivity(
        @NonNull Context context,
        @NonNull Intent intent,
        @Nullable ActivityResultLauncher<Intent> launcher
    ) {
        if (launcher != null) {
            // 使用 launcher 启动（带结果回调）
            launcher.launch(intent);
        } else {
            // 普通启动
            if (!(context instanceof Activity)) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(intent);
        }
    }

    /**
     * 启动 Activity（无参数，无结果回调）
     *
     * @param context       上下文
     * @param activityClass 目标页面字节码
     */
    public void startActivity(@NonNull Context context, @NonNull Class<?> activityClass) {
        startActivity(context, new Intent(context, activityClass), null);
    }

    /**
     * 启动 Activity（无参数，无结果回调）
     *
     * @param context       上下文
     * @param activityClass 目标页面字节码
     * @param extras        Bundle参数
     */
    public void startActivity(
        @NonNull Context context,
        @NonNull Class<?> activityClass,
        @Nullable Bundle extras
    ) {
        Intent intent = new Intent(context, activityClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        startActivity(context, intent, null);
    }

    /**
     * 启动 Activity（携带参数，支持结果回调）
     *
     * @param context       上下文
     * @param activityClass 目标页面字节码
     * @param extras        Bundle参数
     * @param launcher      已注册的 ActivityResultLauncher
     */
    public void startActivity(
        @NonNull Context context,
        @NonNull Class<?> activityClass,
        @Nullable Bundle extras,
        @Nullable ActivityResultLauncher<Intent> launcher
    ) {
        Intent intent = new Intent(context, activityClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        startActivity(context, intent, launcher);
    }

    // ==================== 业务页面专用跳转方法 ====================

    /**
     * 跳转详情页
     *
     * @param context 上下文
     * @param uri     页面uri
     * @param title   标题
     */
    public void startDetailActivity(@NonNull Context context, @NonNull String uri, String title) {
        startDetailActivity(context, uri, title, null);
    }

    /**
     * 跳转详情页
     *
     * @param context  上下文
     * @param uri      页面uri
     * @param title    标题
     * @param launcher ActivityResultLauncher<Intent>
     */
    public void startDetailActivity(
        @NonNull Context context,
        @NonNull String uri,
        String title,
        @Nullable ActivityResultLauncher<Intent> launcher
    ) {
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(Router.EXTRA_URI, uri);
        intent.putExtra(Router.EXTRA_TITLE, title);
        startActivity(context, intent, launcher);
    }

    /**
     * 跳转文章页
     *
     * @param context 上下文
     * @param uri     页面uri
     * @param title   标题
     */
    public void startArticleActivity(@NonNull Context context, @NonNull String uri, String title) {
        startArticleActivity(context, uri, title, null);
    }

    /**
     * 跳转文章页
     *
     * @param context  上下文
     * @param uri      页面uri
     * @param title    标题
     * @param launcher ActivityResultLauncher<Intent>
     */
    public void startArticleActivity(
        @NonNull Context context,
        @NonNull String uri,
        String title,
        @Nullable ActivityResultLauncher<Intent> launcher
    ) {
        Intent intent = new Intent(context, MarkdownActivity.class);
        intent.putExtra(Router.EXTRA_URI, uri);
        intent.putExtra(Router.EXTRA_TITLE, title);
        startActivity(context, intent, launcher);
    }

    /**
     * 跳转二级页
     *
     * @param context 上下文
     * @param uri     页面uri
     * @param title   标题
     */
    public void startSecondActivity(
        @NonNull Context context,
        @NonNull String uri,
        String title
    ) {
        startSecondActivity(context, uri, title, null);
    }

    /**
     * 跳转二级页
     *
     * @param context 上下文
     * @param args    页面跳转参数
     */
    public void startSecondActivity(@NonNull Context context, Bundle args) {
        Intent intent = new Intent(context, SecondActivity.class);
        intent.putExtra(Router.EXTRA_SECOND_PAGE, args);
        startActivity(context, intent, null);
    }

    /**
     * 跳转二级页
     *
     * @param context  上下文
     * @param uri      页面uri
     * @param title    标题
     * @param launcher ActivityResultLauncher<Intent>
     */
    public void startSecondActivity(
        @NonNull Context context,
        @NonNull String uri,
        String title,
        @Nullable ActivityResultLauncher<Intent> launcher
    ) {
        Intent intent = new Intent(context, SecondActivity.class);
        Bundle args = new Bundle();
        args.putString(Router.EXTRA_URI, uri);
        args.putString(Router.EXTRA_TITLE, title);
        intent.putExtra(Router.EXTRA_SECOND_PAGE, args);
        startActivity(context, intent, launcher);
    }


    // ==================== 重启 ====================

    public void restart(@NonNull Context context, @NonNull String uri) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra(uri, true);
        startActivity(context, intent, null);
    }


    // ==================== 浏览器跳转 ====================

    /**
     * 打开系统浏览器（不支持结果回调）
     *
     * @param context 上下文
     * @param url     目标网址（会自动校验格式）
     */
    public void openBrowser(@NonNull Context context, @NonNull String url) {
        try {
            Uri uri = Uri.parse(url);
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            // 浏览器跳转一般不需要结果回调
            startActivity(context, intent, null);
        } catch (ActivityNotFoundException e) {
            Log.w(TAG, "openBrowser: " + e.getMessage());
        }
    }

    // ==================== Navigation 组件跳转 ====================

    /**
     * 通过 Navigation 导航到目标 Fragment
     *
     * @param navController 当前页面的 NavController
     * @param destId        目标 Fragment 的 ID
     */
    public void navigateTo(@NonNull NavController navController, @IdRes int destId) {
        navController.navigate(destId);
    }

    /**
     * 通过 Navigation 导航到目标 Fragment（携带参数）
     */
    public void navigateTo(
        @NonNull NavController navController,
        @IdRes int destId,
        @Nullable Bundle args
    ) {
        navController.navigate(destId, args);
    }

    /**
     * 通过 Navigation 导航到目标 Fragment（携带参数）
     */
    public void navigateTo(
        @NonNull NavController navController,
        @IdRes int destId,
        @Nullable Bundle args,
        @Nullable NavOptions navOptions
    ) {
        navController.navigate(destId, args, navOptions);
    }

    // ==================== 传统 Fragment 切换（备用） ====================

    /**
     * 使用 FragmentManager 替换容器中的 Fragment（不支持结果回调，如需结果请使用 Activity 间跳转）
     *
     * @param fragmentManager FragmentManager
     * @param containerId     容器ID
     * @param fragment        目标 Fragment（需通过 newInstance 创建）
     * @param args            参数 Bundle
     * @param addToBackStack  是否加入返回栈
     */
    public void replaceFragment(
        @NonNull FragmentManager fragmentManager,
        @IdRes int containerId,
        @NonNull Fragment fragment,
        @Nullable Bundle args,
        boolean addToBackStack
    ) {
        if (args != null) {
            fragment.setArguments(args);
        }
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(containerId, fragment);
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }
}