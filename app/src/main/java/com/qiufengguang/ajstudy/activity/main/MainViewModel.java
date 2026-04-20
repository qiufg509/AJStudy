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
