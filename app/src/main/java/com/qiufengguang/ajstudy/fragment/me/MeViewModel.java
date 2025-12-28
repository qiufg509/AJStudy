package com.qiufengguang.ajstudy.fragment.me;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.GridCardBean;
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

    private final MutableLiveData<List<GridCardBean>> themeLiveData;

    public MeViewModel() {
        themeLiveData = new MutableLiveData<>();
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
        themeLiveData.setValue(gridCardBeans);
    }

    public LiveData<List<GridCardBean>> getThemeLiveData() {
        return themeLiveData;
    }

    public void saveThemeIndex(GridCardBean bean) {
        List<GridCardBean> cardBeans = themeLiveData.getValue();
        if (cardBeans == null) {
            return;
        }
        for (int index = 0, sum = cardBeans.size(); index < sum; index++) {
            GridCardBean cardBean = cardBeans.get(index);
            if (bean == cardBean) {
                cardBean.setIcon(R.drawable.ic_checkmark);
                ThemeUtils.setSelectedThemeIndex(index);
                continue;
            }
            cardBean.setIcon(0);
        }
        themeLiveData.setValue(cardBeans);
    }
}