package com.qiufengguang.ajstudy.ui.dashboard;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.data.DashboardBean;
import com.qiufengguang.ajstudy.provider.PageTempProvider;

public class DashboardViewModel extends ViewModel {

    private static final String TAG = "DashboardViewModel";
    private final MutableLiveData<DashboardBean> mText;

    public DashboardViewModel() {
        mText = new MutableLiveData<>();
        DashboardBean bean = new DashboardBean();
        bean.setName("测试111");
        mText.setValue(bean);
    }

    public DashboardViewModel(SavedStateHandle handle) {
        mText = handle.getLiveData(DashboardBean.class.getName());
        handle.setSavedStateProvider(DashboardBean.class.getName(), new PageTempProvider<>(DashboardBean.class));
        Log.i(TAG, "NotificationsViewModel: " + mText.getValue());
    }

    public LiveData<DashboardBean> getText() {
        return mText;
    }
}