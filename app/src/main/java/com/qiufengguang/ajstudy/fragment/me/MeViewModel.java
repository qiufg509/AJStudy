package com.qiufengguang.ajstudy.fragment.me;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.data.SettingCardBean;
import com.qiufengguang.ajstudy.data.base.CollectionLayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的页面ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class MeViewModel extends ViewModel {

    private final MutableLiveData<List<LayoutData<?>>> liveData;

    public MeViewModel() {
        liveData = new MutableLiveData<>();
        initData();
    }

    private void initData() {
        List<SettingCardBean> settingCardBeans1 = List.of(
            new SettingCardBean("配色", null, "color")
        );
        CollectionLayoutData<SettingCardBean> settingData1 = LayoutDataFactory.createCollection(settingCardBeans1);

        List<SettingCardBean> settingCardBeans2 = List.of(
            new SettingCardBean("音效", true),
            new SettingCardBean("触觉反馈", true)
        );
        CollectionLayoutData<SettingCardBean> settingData2 = LayoutDataFactory.createCollection(settingCardBeans2);

        List<SettingCardBean> settingCardBeans3 = List.of(
            new SettingCardBean("帮助与反馈", "", "help"),
            new SettingCardBean("关于", "版本号 1.0.0.1", "")
        );
        CollectionLayoutData<SettingCardBean> settingData3 = LayoutDataFactory.createCollection(settingCardBeans3);


        List<LayoutData<?>> dataList = new ArrayList<>();
        dataList.add(settingData1);
        dataList.add(settingData2);
        dataList.add(settingData3);
        liveData.setValue(dataList);
    }

    public LiveData<List<LayoutData<?>>> getLiveData() {
        return liveData;
    }
}