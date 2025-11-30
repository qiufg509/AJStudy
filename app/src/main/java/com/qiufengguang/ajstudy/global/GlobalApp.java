package com.qiufengguang.ajstudy.global;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;

import com.qiufengguang.ajstudy.data.LoginAction;
import com.qiufengguang.ajstudy.data.User;
import com.qiufengguang.ajstudy.network.LoginRepository;

import java.lang.ref.WeakReference;
import java.util.Objects;

public class GlobalApp extends Application implements ViewModelStoreOwner {
    private final ViewModelStore viewModelStore = new ViewModelStore();

    private ViewModelProvider viewModelProvider;

    private static WeakReference<Context> contextReference;

    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        return viewModelStore;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        contextReference = new WeakReference<>(this.getApplicationContext());
        // 初始化全局配置
        initializeApp();
    }

    public static Context getContext() {
        return Objects.isNull(contextReference) ? null : contextReference.get();
    }

    @Override
    public void onTerminate() {
        viewModelStore.clear();
        contextReference.clear();
        viewModelProvider = null;
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
        // 从 SharedPreferences 或其他存储检查登录状态
        LoginRepository userRepository = new LoginRepository();
        User savedUser = userRepository.getSavedUser();
        if (savedUser == null) {
            return;
        }
        GlobalViewModel globalViewModel = getGlobalViewModel();
        LoginAction action = new LoginAction(true);
        if (savedUser.isInvalid()) {
            userRepository.saveUserInfo(savedUser);
            action.setLoggedIn(false);
        }
        globalViewModel.setLoginAction(action);
        globalViewModel.setCurrentUser(savedUser);
    }
}