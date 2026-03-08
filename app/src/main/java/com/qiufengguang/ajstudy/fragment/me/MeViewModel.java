package com.qiufengguang.ajstudy.fragment.me;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.card.setting.SettingCard;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.model.SettingCardBean;
import com.qiufengguang.ajstudy.data.repository.MeRepository;
import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.utils.SpUtils;

import java.util.List;

/**
 * 我的页面ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class MeViewModel extends BaseViewModel {
    private final MutableLiveData<List<LayoutData<?>>> liveData = new MutableLiveData<>();

    private final MeRepository repository;

    public MeViewModel() {
        repository = MeRepository.getInstance();
        loadData();
    }

    private void loadData() {
        // 取消之前的请求
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
        }

        currentCall = repository.fetchMeData(new OnDataLoadedCallback<>() {
            @Override
            public void onSuccess(PageData data) {
                List<LayoutData<?>> dataList = data.getLayoutData();
                fillLocalData(dataList);
                liveData.postValue(dataList);
            }

            @Override
            public void onFailure(Throwable t) {
                List<LayoutData<?>> dataList = fetchEmptyData();
                liveData.postValue(dataList);
            }
        });
    }

    private void fillLocalData(List<LayoutData<?>> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }
        for (LayoutData<?> layoutData : dataList) {
            if (layoutData.getLayoutId() != SettingCard.LAYOUT_ID) {
                continue;
            }
            @SuppressWarnings("unchecked")
            List<SettingCardBean> cardBeans = (List<SettingCardBean>) layoutData.getData();
            if (cardBeans == null || cardBeans.isEmpty()) {
                continue;
            }
            for (SettingCardBean cardBean : cardBeans) {
                if (cardBean.getId() != 4) {
                    continue;
                }
                boolean isChecked = SpUtils.getInstance().getBoolean(Constant.Sp.KEY_TICK_SOUND, false);
                cardBean.setChecked(isChecked);
            }
        }
    }

    public LiveData<List<LayoutData<?>>> getLiveData() {
        return liveData;
    }
}