package com.qiufengguang.ajstudy.fragment.second;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.fragment.base.BaseGridFragment;
import com.qiufengguang.ajstudy.fragment.base.PageConfig;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.view.DynamicToolbar;

public class SecondFragment extends BaseGridFragment {
    public static SecondFragment newInstance(Bundle args) {
        SecondFragment f = new SecondFragment();
        f.setArguments(args);
        return f;
    }

    @NonNull
    @Override
    protected PageConfig getPageConfig() {
        return new PageConfig.Builder()
            .setHasNaviBar(false)
            .setTitleBarMode(DynamicToolbar.Mode.BACK_TITLE)
            .create();
    }

    @Override
    public void onData() {
        SecondViewModel viewModel = new ViewModelProvider(this).get(SecondViewModel.class);
        viewModel.getLiveData().observe(getViewLifecycleOwner(), layoutData ->
            baseListAdapter.setData(layoutData));
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        setTitle(arguments.getString(Router.EXTRA_TITLE));
        String uri = arguments.getString(Router.EXTRA_URI);
        String directory = arguments.getString(Router.EXTRA_DIRECTORY);
        viewModel.loadData(uri, directory);
    }
}