package com.qiufengguang.ajstudy.global;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GlobalViewModel extends ViewModel {
    private final MutableLiveData<String> liveData = new MutableLiveData<>();

    public MutableLiveData<String> getLiveData() {
        return liveData;
    }

    public void updateData(String newData) {
        // 注意：在主线程调用
        liveData.setValue(newData);
    }
}
