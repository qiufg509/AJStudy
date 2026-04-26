package com.qiufengguang.ajstudy.fragment.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.converter.LayoutDataConverter;
import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.data.repository.HomeRepository;
import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;
import com.qiufengguang.ajstudy.utils.JsonUtils;

import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

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

        addDisposable(repository.getLocalRawData()
            .toObservable()
            .onErrorComplete()
            .observeOn(Schedulers.io())
            .map(raw -> {
                if (isNetworkDataLoaded) {
                    return null;
                }
                lastDataJson = JsonUtils.getGson().toJson(raw);
                return LayoutDataConverter.convert(JsonUtils.getGson(), raw).getLayoutData();
            })
            .filter(Objects::nonNull)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(liveData::setValue, t -> {
            }));

        addDisposable(repository.fetchHomeRawData()
            .observeOn(Schedulers.io())
            .map(raw -> {
                isNetworkDataLoaded = true;
                if (!raw.isSuccess()) {
                    return null;
                }
                String json = JsonUtils.getGson().toJson(raw);
                if (!json.equals(lastDataJson)) {
                    lastDataJson = json;
                    repository.saveRawDataToCache(raw);
                    return LayoutDataConverter.convert(JsonUtils.getGson(), raw).getLayoutData();
                }
                return null;
            })
            .filter(Objects::nonNull)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(liveData::setValue, t -> handleError()));
    }


    private void handleError() {
        if (liveData.getValue() == null || liveData.getValue().isEmpty()) {
            liveData.postValue(fetchStateData(State.ERROR));
        }
    }

    public void retry() {
        liveData.setValue(fetchStateData(State.LOADING));
        loadData();
    }

    public LiveData<List<LayoutData<?>>> getLiveData() {
        return liveData;
    }
}