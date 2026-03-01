package com.qiufengguang.ajstudy.fragment.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
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
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

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

        loadingLiveData.setValue(true);
        errorLiveData.setValue(null);

        currentCall = repository.fetchHomeData(new OnDataLoadedCallback<>() {
            @Override
            public void onSuccess(PageData data) {
                loadingLiveData.postValue(false);
                liveData.postValue(data.getLayoutData());
            }

            @Override
            public void onFailure(Throwable t) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue(t.getMessage());
                List<LayoutData<?>> dataList = fetchEmptyData();
                liveData.postValue(dataList);
            }
        });
    }

    public LiveData<List<LayoutData<?>>> getLiveData() {
        return liveData;
    }

    public LiveData<Boolean> getLoading() {
        return loadingLiveData;
    }

    public LiveData<String> getError() {
        return errorLiveData;
    }

    public void retry() {
        loadData();
    }
}