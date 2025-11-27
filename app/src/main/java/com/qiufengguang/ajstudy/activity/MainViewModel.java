package com.qiufengguang.ajstudy.activity;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.R;

public class MainViewModel extends ViewModel {
    private final MutableLiveData<Integer> currentNavigationItem = new MutableLiveData<>();

    public MainViewModel() {
        currentNavigationItem.setValue(R.id.navigation_home);
    }

    public LiveData<Integer> getCurrentNavigationItem() {
        return currentNavigationItem;
    }

    public void setCurrentNavigationItem(int itemId) {
        currentNavigationItem.setValue(itemId);
    }
}
