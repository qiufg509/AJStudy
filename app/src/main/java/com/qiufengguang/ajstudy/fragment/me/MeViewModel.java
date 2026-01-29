package com.qiufengguang.ajstudy.fragment.me;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.GridCardBean;
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
        List<GridCardBean> gridCardBeans = new ArrayList<>();
        gridCardBeans.add(new GridCardBean(R.color.ajstudy_primary_red));
        gridCardBeans.add(new GridCardBean(R.color.ajstudy_primary_blue));
        gridCardBeans.add(new GridCardBean(R.color.ajstudy_primary_green));
        gridCardBeans.add(new GridCardBean(R.color.ajstudy_primary_orange));
        gridCardBeans.add(new GridCardBean(R.color.ajstudy_primary_pink));
        gridCardBeans.add(new GridCardBean(R.color.ajstudy_primary_black));
        gridCardBeans.add(new GridCardBean(R.color.ajstudy_primary_amber));
        gridCardBeans.add(new GridCardBean(R.color.ajstudy_primary_indigo));
        gridCardBeans.add(new GridCardBean(R.color.ajstudy_primary_lime));
        gridCardBeans.add(new GridCardBean(R.color.ajstudy_primary_purple));
        int themeIndex = ThemeUtils.getSelectedThemeIndex();
        GridCardBean bean = gridCardBeans.get(themeIndex);
        bean.setIcon(R.drawable.ic_checkmark);

        LayoutData<List<GridCardBean>> gridCardData = LayoutDataFactory.createCollection(gridCardBeans);

        List<LayoutData<?>> dataList = new ArrayList<>();
        dataList.add(gridCardData);
        liveData.setValue(dataList);
    }


    public LiveData<List<LayoutData<?>>> getLiveData() {
        return liveData;
    }


    public void saveThemeIndex(GridCardBean bean) {
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
                if (bean == cardBean) {
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