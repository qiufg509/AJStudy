package com.qiufengguang.ajstudy.ui.me;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 我的页面ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class MeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is me fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}