package com.qiufengguang.ajstudy.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 首页ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> liveData;

    public HomeViewModel() {
        liveData = new MutableLiveData<>();
        liveData.setValue("This is home fragment");
    }

    public LiveData<String> getLiveData() {
        return liveData;
    }
}