package com.qiufengguang.ajstudy.fragment.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

/**
 * 基类Fragment对应的ViewModel
 *
 * @author qiufengguang
 * @since 2025/11/26 22:19
 */
public abstract class BaseViewModel extends ViewModel {
    private final SavedStateHandle savedStateHandle;

    public BaseViewModel(@NonNull SavedStateHandle savedStateHandle) {
        this.savedStateHandle = savedStateHandle;
    }

    /**
     * 获取保存状态的 LiveData，如果不存在则创建默认值
     */
    protected <T> LiveData<T> getSavedStateLiveData(@NonNull String key, T defaultValue) {
        if (!savedStateHandle.contains(key)) {
            savedStateHandle.set(key, defaultValue);
        }
        return savedStateHandle.getLiveData(key);
    }

    /**
     * 获取保存状态的 LiveData，不设置默认值
     */
    protected <T> LiveData<T> getSavedStateLiveData(@NonNull String key) {
        return savedStateHandle.getLiveData(key);
    }

    /**
     * 设置状态值
     */
    protected <T> void setSavedState(@NonNull String key, T value) {
        savedStateHandle.set(key, value);
    }

    /**
     * 获取状态值
     */
    protected <T> T getSavedState(@NonNull String key) {
        return savedStateHandle.get(key);
    }
}