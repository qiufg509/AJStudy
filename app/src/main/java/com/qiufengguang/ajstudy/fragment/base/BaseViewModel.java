package com.qiufengguang.ajstudy.fragment.base;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.data.base.LayoutData;

import java.util.List;

/**
 * 基类Fragment对应的ViewModel
 *
 * @author qiufengguang
 * @since 2025/11/26 22:19
 */
public abstract class BaseViewModel extends ViewModel {
    private final MutableLiveData<List<LayoutData<?>>> liveData;

    public BaseViewModel() {
        liveData = new MutableLiveData<>();
        initData();
    }

    /**
     * 初始化数据
     */
    private void initData() {
    }


    public LiveData<List<LayoutData<?>>> getLiveData() {
        return liveData;
    }
}