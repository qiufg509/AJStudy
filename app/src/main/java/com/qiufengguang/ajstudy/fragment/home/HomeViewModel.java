package com.qiufengguang.ajstudy.fragment.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.data.repository.HomeRepository;
import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;
import com.qiufengguang.ajstudy.utils.JsonUtils;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;

/**
 * 首页ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class HomeViewModel extends BaseViewModel {
    private final MutableLiveData<List<LayoutData<?>>> liveData = new MutableLiveData<>();

    private final HomeRepository repository;

    /**
     * 用于对比数据是否一致
     */
    private String lastDataJson = "";

    private volatile boolean isNetworkDataLoaded = false;

    public HomeViewModel() {
        repository = HomeRepository.getInstance();
        loadData();
    }

    private void loadData() {
        isNetworkDataLoaded = false;
        // 优先读取并展示本地缓存
        addDisposable(repository.getLocalHomeData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(pageData -> {
                if (!isNetworkDataLoaded) {
                    lastDataJson = JsonUtils.getGson().toJson(pageData);
                    liveData.postValue(pageData.getLayoutData());
                }
            }, throwable -> {
                // 忽略缓存读取错误
            }));

        // 请求服务器获取最新数据
        addDisposable(repository.fetchHomeData()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(pageData -> {
                isNetworkDataLoaded = true;
                String newDataJson = JsonUtils.getGson().toJson(pageData);
                // 对比数据，如果不一致则更新 UI 并存入数据库
                if (!newDataJson.equals(lastDataJson)) {
                    lastDataJson = newDataJson;
                    liveData.postValue(pageData.getLayoutData());
                    repository.saveHomeDataToCache(pageData);
                }
            }, throwable -> {
                // 网络失败时不标记 isNetworkDataLoaded = true，
                // 这样如果此时慢悠悠的本地缓存请求才返回，依然能展示出旧数据，比直接显示错误页体验更好。
                if (liveData.getValue() == null || liveData.getValue().isEmpty()) {
                    liveData.postValue(fetchStateData(State.ERROR));
                }
            }));
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