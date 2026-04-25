package com.qiufengguang.ajstudy.activity.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;

/**
 * 主页面ViewModel
 *
 * @author qiufengguang
 * @since 2025/11/27 17:42
 */
public class MainViewModel extends BaseViewModel {
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
}