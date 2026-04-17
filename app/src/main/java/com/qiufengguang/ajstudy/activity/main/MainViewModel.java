package com.qiufengguang.ajstudy.activity.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 主页面ViewModel
 *
 * @author qiufengguang
 * @since 2025/11/27 17:42
 */
public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";

    private final MutableLiveData<Integer> liveData = new MutableLiveData<>();

    public MainViewModel() {
        // [性能重构]：移除此处不稳定的初始化逻辑，已迁移至 GlobalApp 统一调度
    }

    public LiveData<Integer> getLiveData() {
        return liveData;
    }

    public void setLiveData(int itemId) {
        liveData.setValue(itemId);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}
