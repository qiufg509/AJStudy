package com.qiufengguang.ajstudy.fragment.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.card.state.StateCard;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.base.SingleLayoutData;
import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.data.model.StateCardBean;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 基类Fragment对应的ViewModel
 *
 * @author qiufengguang
 * @since 2025/11/26 22:19
 */
public abstract class BaseViewModel extends ViewModel {
    protected final CompositeDisposable disposables = new CompositeDisposable();

    protected void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposables.clear();
    }

    /**
     * 获取页面状态数据
     *
     * @param state 页面状态{@link State}
     * @return List<LayoutData<?>>
     */
    protected List<LayoutData<?>> fetchStateData(State state) {
        SingleLayoutData<StateCardBean> stateCardData = LayoutDataFactory.createSingle(
            StateCard.LAYOUT_ID, new StateCardBean(state));
        List<LayoutData<?>> dataList = new ArrayList<>(1);
        dataList.add(stateCardData);
        return dataList;
    }

    /**
     * 设置错误数据
     *
     * @param liveData 页面数据MutableLiveData<List<LayoutData<?>>>
     */
    protected void handleError(MutableLiveData<List<LayoutData<?>>> liveData) {
        if (liveData.getValue() == null || liveData.getValue().isEmpty()
            || (liveData.getValue().size() == 1
            && liveData.getValue().get(0).getLayoutId() == StateCard.LAYOUT_ID)) {
            List<LayoutData<?>> value = fetchStateData(State.ERROR);
            if (Thread.currentThread() == android.os.Looper.getMainLooper().getThread()) {
                liveData.setValue(value);
            } else {
                liveData.postValue(value);
            }
        }
    }
}