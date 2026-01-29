package com.qiufengguang.ajstudy.fragment.me;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.fragment.base.BaseListFragment;
import com.qiufengguang.ajstudy.fragment.base.PageConfig;

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
        viewModel.getLiveData().observe(getViewLifecycleOwner(),
            layoutData -> baseListAdapter.setData(layoutData));
    }

    @Override
    protected String getTitle() {
        return getString(R.string.bottom_nav_title_me);
    }

}