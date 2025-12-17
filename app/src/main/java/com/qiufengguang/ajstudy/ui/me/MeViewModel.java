package com.qiufengguang.ajstudy.ui.me;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.utils.ThemeUtils;

/**
 * 我的页面ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class MeViewModel extends ViewModel {

    private final MutableLiveData<Integer> liveData;

    public MeViewModel() {
        liveData = new MutableLiveData<>();
        initData();
    }

    private void initData() {
        liveData.setValue(ThemeUtils.getSelectedThemeIndex());
    }

    public LiveData<Integer> getLiveData() {
        return liveData;
    }

    public void saveThemeIndex(@NonNull Integer themeIndex) {
        liveData.setValue(themeIndex);
        ThemeUtils.setSelectedThemeIndex(themeIndex);
    }
}