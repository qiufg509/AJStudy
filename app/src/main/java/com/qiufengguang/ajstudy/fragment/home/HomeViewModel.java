package com.qiufengguang.ajstudy.fragment.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.remote.dto.HomeResponse;
import com.qiufengguang.ajstudy.data.repository.HomeRepository;

import java.util.List;

import retrofit2.Call;

/**
 * 首页ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class HomeViewModel extends ViewModel {
    private final MutableLiveData<List<LayoutData<?>>> liveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loadingLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();

    private final HomeRepository repository;
    private Call<HomeResponse> currentCall;

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
            public void onSuccess(List<LayoutData<?>> data) {
                loadingLiveData.postValue(false);
                liveData.postValue(data);
            }

            @Override
            public void onFailure(Throwable t) {
                loadingLiveData.postValue(false);
                errorLiveData.postValue(t.getMessage());
                // 可根据业务需求保留旧数据或清空
                liveData.postValue(null);
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

    @Override
    protected void onCleared() {
        super.onCleared();
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
        }
    }
}