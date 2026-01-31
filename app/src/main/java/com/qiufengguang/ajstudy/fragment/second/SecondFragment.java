package com.qiufengguang.ajstudy.fragment.second;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.fragment.base.BaseListFragment;
import com.qiufengguang.ajstudy.fragment.base.PageConfig;

public class SecondFragment extends BaseListFragment {
    public static final String ARG_TITLE = "secondPageTitle";

    public static SecondFragment newInstance(Bundle args) {
        SecondFragment f = new SecondFragment();
        f.setArguments(args);
        return f;
    }

    @NonNull
    @Override
    protected PageConfig getPageConfig() {
        return new PageConfig.Builder().setHasNaviBar(false).create();
    }

    @Override
    public void onData() {
        SecondViewModel viewModel = new ViewModelProvider(this).get(SecondViewModel.class);
        viewModel.getLiveData().observe(getViewLifecycleOwner(), layoutData ->
            baseListAdapter.setData(layoutData));
    }

    @Override
    protected String getTitle() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return "";
        }
        return arguments.getString(ARG_TITLE);
    }
}