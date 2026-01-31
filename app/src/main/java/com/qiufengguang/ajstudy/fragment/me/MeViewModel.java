package com.qiufengguang.ajstudy.fragment.me;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.GridCardBean;
import com.qiufengguang.ajstudy.data.SettingCardBean;
import com.qiufengguang.ajstudy.data.base.CollectionLayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.utils.ThemeUtils;

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
        List<GridCardBean> gridCardBeans = List.of(
            new GridCardBean(R.color.ajstudy_primary_red),
            new GridCardBean(R.color.ajstudy_primary_blue),
            new GridCardBean(R.color.ajstudy_primary_green),
            new GridCardBean(R.color.ajstudy_primary_orange),
            new GridCardBean(R.color.ajstudy_primary_pink),
            new GridCardBean(R.color.ajstudy_primary_black),
            new GridCardBean(R.color.ajstudy_primary_amber),
            new GridCardBean(R.color.ajstudy_primary_indigo),
            new GridCardBean(R.color.ajstudy_primary_lime),
            new GridCardBean(R.color.ajstudy_primary_purple)
        );
        int themeIndex = ThemeUtils.getSelectedThemeIndex();
        GridCardBean bean = gridCardBeans.get(themeIndex);
        bean.setIcon(R.drawable.ic_checkmark);
        LayoutData<List<GridCardBean>> gridCardData = LayoutDataFactory.createCollection(gridCardBeans, "主题色");


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
        dataList.add(gridCardData);
        dataList.add(settingData1);
        dataList.add(settingData2);
        dataList.add(settingData3);
        liveData.setValue(dataList);
    }


    public LiveData<List<LayoutData<?>>> getLiveData() {
        return liveData;
    }


    public void saveThemeIndex(GridCardBean bean) {
        if (bean.getItemType() != GridCardBean.TYPE_IMAGE) {
            return;
        }
        List<LayoutData<?>> value = liveData.getValue();
        if (value == null) {
            return;
        }
        for (int index = 0, sum = value.size(); index < sum; index++) {
            LayoutData<?> layoutData = value.get(index);
            if (!TextUtils.equals(layoutData.getLayoutName(), GridCardBean.LAYOUT_NAME)) {
                continue;
            }
            @SuppressWarnings("unchecked")
            List<GridCardBean> beans = (List<GridCardBean>) layoutData.getData();
            for (int pos = 0, size = beans.size(); pos < size; pos++) {
                GridCardBean cardBean = beans.get(pos);
                if (bean.getBackgroundTint() == cardBean.getBackgroundTint()
                    && bean.getItemType() == cardBean.getItemType()) {
                    cardBean.setIcon(R.drawable.ic_checkmark);
                    ThemeUtils.setSelectedThemeIndex(pos);
                    continue;
                }
                cardBean.setIcon(0);
            }
        }

        liveData.setValue(value);
    }
}