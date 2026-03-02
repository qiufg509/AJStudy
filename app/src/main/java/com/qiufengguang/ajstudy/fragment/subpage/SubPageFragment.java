package com.qiufengguang.ajstudy.fragment.subpage;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.activity.detail.DetailViewModel;
import com.qiufengguang.ajstudy.fragment.base.BaseListFragment;
import com.qiufengguang.ajstudy.fragment.base.PageConfig;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.view.DynamicToolbar;

/**
 * 详情页子页面
 *
 * @author qiufengguang
 * @since 2026/3/1 15:44
 */
public class SubPageFragment extends BaseListFragment {
    private String detailId = "";

    public SubPageFragment() {
    }

    public static SubPageFragment newInstance() {
        return new SubPageFragment();
    }

    public static SubPageFragment newInstance(Bundle args) {
        SubPageFragment f = new SubPageFragment();
        f.setArguments(args);
        return f;
    }

    @NonNull
    @Override
    protected PageConfig getPageConfig() {
        return new PageConfig.Builder()
            .setStatusBarMode(PageConfig.StatusBarMode.NONE)
            .setHasNaviBar(false)
            // 当前作为详情页子页面，底部让出导航栏高度避免与安装按钮重叠
            .setHasNaviBar(true)
            .setOverlayNaviBar(true)
            .setTitleBarMode(DynamicToolbar.Mode.GONE)
            .setEnablePageBounce(false)
            .create();
    }

    @Override
    public void onData() {
        Bundle arguments = getArguments();
        if (arguments != null) {
            detailId = arguments.getString(Router.EXTRA_DATA, "");
        }
        DetailViewModel viewModel = new ViewModelProvider(requireActivity()).get(DetailViewModel.class);
        if (detailId.contains("comment")) {
            viewModel.getComments().observe(getViewLifecycleOwner(), layoutData ->
                baseListAdapter.setData(layoutData));
        } else if (detailId.contains("recommend")) {
            viewModel.getRecommendations().observe(getViewLifecycleOwner(), layoutData ->
                baseListAdapter.setData(layoutData));
        } else {
            viewModel.getIntroduction().observe(getViewLifecycleOwner(), layoutData ->
                baseListAdapter.setData(layoutData));
        }
    }
}