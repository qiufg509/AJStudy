package com.qiufengguang.ajstudy.global;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.qiufengguang.ajstudy.data.model.LoginAction;
import com.qiufengguang.ajstudy.data.model.User;
import com.qiufengguang.ajstudy.data.repository.LoginRepository;
import com.qiufengguang.ajstudy.router.AppNavigator;
import com.qiufengguang.ajstudy.utils.AppExecutors;
import com.qiufengguang.ajstudy.utils.MarkwonHelper;
import com.qiufengguang.ajstudy.utils.SpUtils;
import com.qiufengguang.ajstudy.utils.ThemeUtils;

/**
 * 全局application
 *
 * @author qiufengguang
 * @since 2025/5/5 23:54
 */
public class GlobalApp extends Application implements ViewModelStoreOwner {
    private final ViewModelStore viewModelStore = new ViewModelStore();

    private ViewModelProvider viewModelProvider;

    private static GlobalApp sInstance;

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }

    @Override
    public void onCreate() {
        sInstance = this;
        setTheme(ThemeUtils.getSplashTheme());
        super.onCreate();
        // 初始化全局配置
        initializeApp();
    }

    /**
     * 获取全局上下文
     * [性能专家提示]：此 Context 为 Application 级别，适用于 SDK 初始化、数据持久化等非 UI 任务。
     *
     * @return Application Context
     */
    public static Context getContext() {
        return sInstance;
    }

    @Override
    public void onTerminate() {
        viewModelStore.clear();
        SpUtils.getInstance().shutdown();
        viewModelProvider = null;
        sInstance = null;
        super.onTerminate();
    }

    /**
     * 获取全局 ViewModel
     *
     * @return GlobalViewModel
     */
    public GlobalViewModel getGlobalViewModel() {
        if (viewModelProvider == null) {
            viewModelProvider = new ViewModelProvider(this);
        }
        return viewModelProvider.get(GlobalViewModel.class);
    }

    private void initializeApp() {
        // 1. 同步初始化：轻量级且必须在 UI 绘制前准备好的组件
        CardRegistrar.initialize();

        // 2. 异步初始化调度 [性能专家重构]：利用 LiveData.postValue 彻底打平线程嵌套
        AppExecutors.getInstance().diskIO().execute(() -> {
            // SDK 预热
            AppNavigator.init();
            MarkwonHelper.initialize();

            // 用户状态恢复
            LoginRepository userRepository = new LoginRepository();
            User savedUser = userRepository.getSavedUser();

            if (savedUser != null) {
                final boolean isInvalid = savedUser.isInvalid();
                if (isInvalid) {
                    userRepository.saveUserInfo(savedUser);
                }

                // [性能重构点]：直接在 diskIO 线程调用，内部自动切换 postValue，消除 mainThread 嵌套
                GlobalViewModel globalViewModel = getGlobalViewModel();
                LoginAction action = new LoginAction(true);
                if (isInvalid) {
                    action.setLoggedIn(false);
                }
                globalViewModel.setLoginAction(action);
                globalViewModel.setCurrentUser(savedUser);
            }
        });
    }
}