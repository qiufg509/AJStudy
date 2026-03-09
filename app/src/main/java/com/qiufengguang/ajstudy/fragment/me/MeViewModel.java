package com.qiufengguang.ajstudy.fragment.me;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.card.setting.SettingCard;
import com.qiufengguang.ajstudy.card.user.SimpleUserCard;
import com.qiufengguang.ajstudy.data.base.CollectionLayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.base.SingleLayoutData;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.model.SettingCardBean;
import com.qiufengguang.ajstudy.data.model.User;
import com.qiufengguang.ajstudy.data.repository.MeRepository;
import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.global.GlobalApp;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.utils.SpUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的页面ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class MeViewModel extends BaseViewModel {
    private static final String TAG = "MeViewModel";
    /**
     * 本地数据源
     */
    private final MutableLiveData<List<LayoutData<?>>> localData = new MutableLiveData<>();

    /**
     * 服务器数据源
     */
    private final MutableLiveData<List<LayoutData<?>>> serverData = new MutableLiveData<>();
    /**
     * 用户数据源
     */
    private LiveData<User> userLiveData;

    /**
     * 合并后的列表数据
     */
    private final MediatorLiveData<List<LayoutData<?>>> mergedListLiveData = new MediatorLiveData<>();

    private final MeRepository repository;

    public MeViewModel() {
        repository = MeRepository.getInstance();
        mergedListLiveData.addSource(serverData, server -> combine());
        mergedListLiveData.addSource(localData, local -> combine());
        loadServerData();
        loadLocalData();
    }

    public void setUserLiveData(LiveData<User> userLiveData) {
        // 如果之前已添加过旧源，先移除
        if (this.userLiveData != null) {
            mergedListLiveData.removeSource(this.userLiveData);
        }
        this.userLiveData = userLiveData;
        if (userLiveData != null) {
            mergedListLiveData.addSource(userLiveData, user -> combine());
        }
    }

    private void loadServerData() {
        // 取消之前的请求
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
        }
        currentCall = repository.fetchMeData(new OnDataLoadedCallback<>() {
            @Override
            public void onSuccess(PageData data) {
                List<LayoutData<?>> dataList = data.getLayoutData();
                serverData.postValue(dataList);
            }

            @Override
            public void onFailure(Throwable t) {
                Log.w(TAG, "loadServerData: ", t);
            }
        });
    }

    private void loadLocalData() {
        List<SettingCardBean> colorSchemeList = List.of(
            new SettingCardBean(3, "配色", Router.PAGE_ID.SECOND_GRID + "/" + Router.URI.PAGE_COLOR_SCHEME)
        );
        CollectionLayoutData<SettingCardBean> colorSchemeData = LayoutDataFactory.createCollection(SettingCard.LAYOUT_ID, colorSchemeList);

        boolean enableTickSound = SpUtils.getInstance().getBoolean(Constant.Sp.KEY_TICK_SOUND, false);
        boolean enableHaptic = SpUtils.getInstance().getBoolean(Constant.Sp.KEY_HAPTIC, false);
        List<SettingCardBean> localConfigList = List.of(
            new SettingCardBean(4, "音效", true, enableTickSound),
            new SettingCardBean(5, "触觉反馈", true, enableHaptic),
            new SettingCardBean(6, "实验室", Router.PAGE_ID.SECOND_GRID + "/" + Router.URI.PAGE_LAB)
        );
        CollectionLayoutData<SettingCardBean> localConfigData = LayoutDataFactory.createCollection(SettingCard.LAYOUT_ID, localConfigList);

        List<SettingCardBean> localInfoList = List.of(
            new SettingCardBean(7, "帮助与反馈", Router.PAGE_ID.SECOND_GRID + "/" + Router.URI.PAGE_HELP_FEEDBACK),
            new SettingCardBean(8, "关于", getVersionName(), "")
        );
        CollectionLayoutData<SettingCardBean> localInfoData = LayoutDataFactory.createCollection(SettingCard.LAYOUT_ID, localInfoList);

        List<LayoutData<?>> dataList = new ArrayList<>();
        dataList.add(colorSchemeData);
        dataList.add(localConfigData);
        dataList.add(localInfoData);
        localData.setValue(dataList);
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

    private void combine() {
        List<LayoutData<?>> combined = new ArrayList<>();
        if (userLiveData != null) {
            User user = userLiveData.getValue();
            if (user != null) {
                SingleLayoutData<User> userCard = LayoutDataFactory.createSingle(
                    SimpleUserCard.LAYOUT_ID, user);
                combined.add(userCard);
            }
        }
        List<LayoutData<?>> serverList = serverData.getValue();
        if (serverList != null && !serverList.isEmpty()) {
            combined.addAll(serverList);
        }

        List<LayoutData<?>> localList = localData.getValue();
        if (localList != null && !localList.isEmpty()) {
            combined.addAll(localList);
        }
        if (!combined.isEmpty()) {
            mergedListLiveData.setValue(combined);
        } else {
            List<LayoutData<?>> dataList = fetchEmptyData();
            mergedListLiveData.postValue(dataList);
        }
    }

    public LiveData<List<LayoutData<?>>> getLiveData() {
        return mergedListLiveData;
    }
}