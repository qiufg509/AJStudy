package com.qiufengguang.ajstudy.fragment.second;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.repository.SecondaryRepository;
import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;
import com.qiufengguang.ajstudy.router.Router;

import java.util.List;

/**
 * 二级页ViewModel
 *
 * @author qiufengguang
 * @since 2026/1/31 21:53
 */
public class SecondViewModel extends BaseViewModel {
    private final MutableLiveData<List<LayoutData<?>>> liveData = new MutableLiveData<>();

    private final SecondaryRepository repository;

    public SecondViewModel() {
        repository = SecondaryRepository.getInstance();
    }

    public void loadData(String uri, String directory) {
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
        }
        if (TextUtils.isEmpty(uri)) {
            List<LayoutData<?>> dataList = fetchEmptyData();
            liveData.setValue(dataList);
            return;
        }
        if (TextUtils.equals(uri, Router.URI.PAGE_LAB)) {
            fetchEmptyData();
            return;
        }
        if (TextUtils.equals(uri, Router.URI.PAGE_COLOR_SCHEME)) {
            fetchEmptyData();
            return;
        }
        currentCall = repository.fetchData(uri, directory, new OnDataLoadedCallback<>() {
            @Override
            public void onSuccess(PageData data) {
                liveData.postValue(data.getLayoutData());
            }

            @Override
            public void onFailure(Throwable t) {
                List<LayoutData<?>> dataList = fetchEmptyData();
                liveData.postValue(dataList);
            }
        });
        if (currentCall == null) {
            List<LayoutData<?>> dataList = fetchEmptyData();
            liveData.setValue(dataList);
        }
    }

    public LiveData<List<LayoutData<?>>> getLiveData() {
        return liveData;
    }
}