package com.qiufengguang.ajstudy.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

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