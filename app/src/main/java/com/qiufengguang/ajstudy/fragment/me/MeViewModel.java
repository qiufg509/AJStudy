package com.qiufengguang.ajstudy.fragment.me;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.card.setting.SettingCard;
import com.qiufengguang.ajstudy.data.SettingCardBean;
import com.qiufengguang.ajstudy.data.base.CollectionLayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.global.GlobalApp;
import com.qiufengguang.ajstudy.router.Router;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的页面ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class MeViewModel extends ViewModel {
    private static final String TAG = "MeViewModel";

    private final MutableLiveData<List<LayoutData<?>>> liveData;

    public MeViewModel() {
        liveData = new MutableLiveData<>();
        initData();
    }

    private void initData() {
        List<SettingCardBean> settingCardBeans0 = List.of(
            new SettingCardBean("我的学习", null, Router.URI.PAGE_STUDY_RECORD),
            new SettingCardBean("我的收藏", null, Router.URI.PAGE_FAVORITES)
        );
        CollectionLayoutData<SettingCardBean> settingData0 = LayoutDataFactory.createCollection(SettingCard.LAYOUT_ID, settingCardBeans0);

        List<SettingCardBean> settingCardBeans1 = List.of(
            new SettingCardBean("配色", null, Router.URI.PAGE_COLOR_SCHEME)
        );
        CollectionLayoutData<SettingCardBean> settingData1 = LayoutDataFactory.createCollection(SettingCard.LAYOUT_ID, settingCardBeans1);

        List<SettingCardBean> settingCardBeans2 = List.of(
            new SettingCardBean("音效", true),
            new SettingCardBean("触觉反馈", true),
            new SettingCardBean("实验室", "", Router.URI.PAGE_LAB)
        );
        CollectionLayoutData<SettingCardBean> settingData2 = LayoutDataFactory.createCollection(SettingCard.LAYOUT_ID, settingCardBeans2);

        List<SettingCardBean> settingCardBeans3 = List.of(
            new SettingCardBean("帮助与反馈", "", Router.URI.PAGE_HELP_FEEDBACK),
            new SettingCardBean("关于", getVersionName())
        );
        CollectionLayoutData<SettingCardBean> settingData3 = LayoutDataFactory.createCollection(SettingCard.LAYOUT_ID, settingCardBeans3);

        List<LayoutData<?>> dataList = new ArrayList<>();
        dataList.add(settingData0);
        dataList.add(settingData1);
        dataList.add(settingData2);
        dataList.add(settingData3);
        liveData.setValue(dataList);
    }

    private String getVersionName() {
        try {
            Context context = GlobalApp.getContext();
            if (context == null) {
                return "";
            }
            PackageInfo packageInfo = context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0);
            return "版本号 " + packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(TAG, "getVersionName: " + e.getMessage());
        }
        return "";
    }

    public LiveData<List<LayoutData<?>>> getLiveData() {
        return liveData;
    }
}