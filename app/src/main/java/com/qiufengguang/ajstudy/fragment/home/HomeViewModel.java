package com.qiufengguang.ajstudy.fragment.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.data.repository.HomeRepository;
import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;

import java.util.List;

/**
 * 首页ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class HomeViewModel extends BaseViewModel {
    private final MutableLiveData<List<LayoutData<?>>> liveData = new MutableLiveData<>();

    private final HomeRepository repository;

    public HomeViewModel() {
        repository = HomeRepository.getInstance();
        loadData();
    }

    private void loadData() {
        // 取消之前的请求
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
        }

        currentCall = repository.fetchHomeData(new OnDataLoadedCallback<>() {
            @Override
            public void onSuccess(PageData data) {
                liveData.postValue(data.getLayoutData());
            }

            @Override
            public void onFailure(Throwable t) {
                List<LayoutData<?>> dataList = fetchStateData(State.ERROR);
                liveData.postValue(dataList);
            }
        });
    }

    /**
     * 重试
     */
    public void retry() {
        List<LayoutData<?>> dataList = fetchStateData(State.LOADING);
        liveData.setValue(dataList);
        loadData();
    }

    public LiveData<List<LayoutData<?>>> getLiveData() {
        return liveData;
    }
}