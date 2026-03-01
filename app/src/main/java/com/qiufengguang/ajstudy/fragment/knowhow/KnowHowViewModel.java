package com.qiufengguang.ajstudy.fragment.knowhow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.repository.KnowHowRepository;
import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 知识列表页ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class KnowHowViewModel extends BaseViewModel {
    private final MutableLiveData<List<LayoutData<?>>> liveData = new MutableLiveData<>();

    private final KnowHowRepository repository;

    /**
     * 每一页加载总数量
     */
    private static final int PAGE_SIZE = 10;


    public KnowHowViewModel() {
        repository = KnowHowRepository.getInstance();
        loadData();
    }

    public void loadData() {
        // 取消之前的请求
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
        }

        currentCall = repository.fetchKnowHowData(new OnDataLoadedCallback<>() {
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
    }

    public List<LayoutData<?>> getPageData(int page) {
        List<LayoutData<?>> value = liveData.getValue();
        if (value == null || page < 0) {
            return null;
        }

        int fromIndex = page * PAGE_SIZE;
        int toIndex = Math.min((page + 1) * PAGE_SIZE, value.size());
        if (fromIndex >= toIndex) {
            return null;
        }
        return new ArrayList<>(value.subList(fromIndex, toIndex));
    }

    public LiveData<List<LayoutData<?>>> getLiveData() {
        return liveData;
    }
}