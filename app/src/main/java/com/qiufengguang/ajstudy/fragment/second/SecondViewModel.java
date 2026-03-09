package com.qiufengguang.ajstudy.fragment.second;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.card.serverip.ServerIpCard;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.base.SingleLayoutData;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.repository.SecondaryRepository;
import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;
import com.qiufengguang.ajstudy.router.Router;

import java.util.ArrayList;
import java.util.List;

/**
 * 二级页ViewModel
 *
 * @author qiufengguang
 * @since 2026/1/31 21:53
 */
public class SecondViewModel extends BaseViewModel {
    private final MutableLiveData<List<LayoutData<?>>> liveData = new MutableLiveData<>();
    private final MutableLiveData<Boolean> titleData = new MutableLiveData<>();

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
            liveData.setValue(fetchLabData());
            return;
        }
        if (TextUtils.equals(uri, Router.URI.PAGE_COLOR_SCHEME)) {
            List<LayoutData<?>> dataList = fetchEmptyData();
            liveData.setValue(dataList);
            return;
        }
        currentCall = repository.fetchData(uri, directory, new OnDataLoadedCallback<>() {
            @Override
            public void onSuccess(PageData data) {
                liveData.postValue(data.getLayoutData());
                // 可扩展更多标题样式配置
                if (TextUtils.equals(data.getTitleType(), "back_title_gradient")) {
                    titleData.postValue(true);
                }
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

    public LiveData<Boolean> getTitleData() {
        return titleData;
    }

    private List<LayoutData<?>> fetchLabData() {
        SingleLayoutData<?> serverIpCardData = LayoutDataFactory.createSingle(ServerIpCard.LAYOUT_ID, null);
        List<LayoutData<?>> dataList = new ArrayList<>();
        dataList.add(serverIpCardData);
        return dataList;
    }
}