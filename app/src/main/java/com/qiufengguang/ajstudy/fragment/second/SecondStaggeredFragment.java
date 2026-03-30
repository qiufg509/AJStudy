package com.qiufengguang.ajstudy.fragment.second;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.fragment.base.BaseStaggeredFragment;
import com.qiufengguang.ajstudy.fragment.base.PageConfig;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.view.DynamicToolbar;

/**
 * 二级页（瀑布流样式）
 *
 * @author qiufengguang
 * @since 2026/3/4 2:17
 */
public class SecondStaggeredFragment extends BaseStaggeredFragment {
    private SecondViewModel viewModel;

    public static SecondStaggeredFragment newInstance(Bundle args) {
        SecondStaggeredFragment f = new SecondStaggeredFragment();
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
        viewModel = new ViewModelProvider(this).get(SecondViewModel.class);
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


    @Override
    public void retry() {
        Bundle arguments = getArguments();
        if (arguments == null) {
            return;
        }
        showPageState(State.LOADING);
        String uri = arguments.getString(Router.EXTRA_URI);
        String directory = arguments.getString(Router.EXTRA_DIRECTORY);
        viewModel.loadData(uri, directory);
    }
}