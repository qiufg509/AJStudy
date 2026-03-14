package com.qiufengguang.ajstudy.fragment.second;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.selecttheme.SelectThemeCard;
import com.qiufengguang.ajstudy.card.serverip.ServerIpCard;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.base.PageData;
import com.qiufengguang.ajstudy.data.base.SingleLayoutData;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.model.SelectThemeCardBean;
import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.data.repository.SecondaryRepository;
import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.utils.ThemeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
            List<LayoutData<?>> dataList = fetchStateData(State.EMPTY);
            liveData.setValue(dataList);
            return;
        }
        if (TextUtils.equals(uri, Router.URI.PAGE_LAB)) {
            liveData.setValue(fetchLabData());
            return;
        }
        if (TextUtils.equals(uri, Router.URI.PAGE_COLOR_SCHEME)) {
            List<LayoutData<?>> dataList = fetchColorSchemeData();
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
                List<LayoutData<?>> dataList = fetchStateData(State.ERROR);
                liveData.postValue(dataList);
            }
        });
        if (currentCall == null) {
            List<LayoutData<?>> dataList = fetchStateData(State.EMPTY);
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

    private List<LayoutData<?>> fetchColorSchemeData() {
        int themeIndex = ThemeUtils.getSelectedThemeIndex();
        List<SelectThemeCardBean> cardBeans = List.of(
            new SelectThemeCardBean(0, "番茄红", R.color.ajstudy_primary_red),
            new SelectThemeCardBean(1, "科技蓝", R.color.ajstudy_primary_blue),
            new SelectThemeCardBean(2, "成功绿", R.color.ajstudy_primary_green),
            new SelectThemeCardBean(3, "暖橙", R.color.ajstudy_primary_orange),
            new SelectThemeCardBean(4, "亮粉", R.color.ajstudy_primary_pink),
            new SelectThemeCardBean(5, "近黑", R.color.ajstudy_primary_black),
            new SelectThemeCardBean(6, "琥珀黄", R.color.ajstudy_primary_amber),
            new SelectThemeCardBean(7, "深蓝紫", R.color.ajstudy_primary_indigo),
            new SelectThemeCardBean(8, "浅黄绿", R.color.ajstudy_primary_lime),
            new SelectThemeCardBean(9, "主紫", R.color.ajstudy_primary_purple)
        );
        return Optional.of(cardBeans)
            .orElse(Collections.emptyList())
            .stream()
            .map(bean -> {
                if (bean.getThemeIndex() == themeIndex) {
                    bean.setSelected(true);
                }
                return LayoutDataFactory.createSingle(SelectThemeCard.LAYOUT_ID, bean);
            })
            .collect(Collectors.toList());
    }
}