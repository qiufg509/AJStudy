package com.qiufengguang.ajstudy.global;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class GlobalViewModel extends ViewModel {
    private final MutableLiveData<String> data = new MutableLiveData<>();

    public MutableLiveData<String> getData() {
        return data;
    }

    public void updateData(String newData) {
        data.setValue(newData); // 注意：在主线程调用
    }

    @Override
    protected void onCleared() {
        // 清理资源
        super.onCleared();
    }
}
