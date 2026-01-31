package com.qiufengguang.ajstudy.fragment.me;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.User;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.base.SingleLayoutData;
import com.qiufengguang.ajstudy.fragment.base.BaseListFragment;
import com.qiufengguang.ajstudy.fragment.base.PageConfig;
import com.qiufengguang.ajstudy.global.GlobalApp;
import com.qiufengguang.ajstudy.global.GlobalViewModel;

import java.util.ArrayList;

/**
 * 我的页面Fragment
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class MeFragment extends BaseListFragment {

    @NonNull
    @Override
    protected PageConfig getPageConfig() {
        return new PageConfig.Builder().create();
    }

    @Override
    public void onData() {
        setPageBackground(R.drawable.me_head_bg);
        MeViewModel viewModel =
            new ViewModelProvider(this).get(MeViewModel.class);

        Application application = requireActivity().getApplication();
        if (application instanceof GlobalApp) {
            GlobalViewModel globalViewModel = ((GlobalApp) application).getGlobalViewModel();
            LiveData<User> userLiveData = globalViewModel.getUserLiveData();
            if (userLiveData != null) {
                userLiveData.observe(getViewLifecycleOwner(), user -> {
                    if (user == null) {
                        return;
                    }
                    SingleLayoutData<User> layoutData = LayoutDataFactory.createSingle(user);
                    ArrayList<LayoutData<?>> dataList = new ArrayList<>();
                    dataList.add(layoutData);
                    baseListAdapter.addData(dataList);
                });
            }
        }
        viewModel.getLiveData().observe(getViewLifecycleOwner(),
            layoutData -> baseListAdapter.addData(layoutData));
    }

    @Override
    protected String getTitle() {
        return getString(R.string.bottom_nav_title_me);
    }

}