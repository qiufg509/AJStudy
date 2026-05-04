package com.qiufengguang.ajstudy.fragment.knowhow;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.State;
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
        addDisposable(repository.fetchKnowHowData()
            .subscribe(
                data -> liveData.postValue(data.getLayoutData()),
                throwable -> handleError(liveData)
            ));
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