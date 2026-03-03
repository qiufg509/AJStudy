package com.qiufengguang.ajstudy.fragment.second;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.fragment.base.BaseGridFragment;
import com.qiufengguang.ajstudy.fragment.base.PageConfig;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.view.DynamicToolbar;

/**
 * 二级页（格网样式）
 *
 * @author qiufengguang
 * @since 2026/3/4 2:17
 */
public class SecondGridFragment extends BaseGridFragment {
    public static SecondGridFragment newInstance(Bundle args) {
        SecondGridFragment f = new SecondGridFragment();
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