package com.qiufengguang.ajstudy.activity.main;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.R;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<Integer> liveData = new MutableLiveData<>();

    public MainViewModel() {
        liveData.setValue(R.id.navigation_home);
    }

    public LiveData<Integer> getLiveData() {
        return liveData;
    }

    public void setLiveData(int itemId) {
        liveData.setValue(itemId);
    }
}
