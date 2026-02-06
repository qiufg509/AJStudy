package com.qiufengguang.ajstudy.fragment.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.fragment.base.BaseGridFragment;
import com.qiufengguang.ajstudy.fragment.base.PageConfig;

/**
 * 首页Fragment
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class HomeFragment extends BaseGridFragment {
    @NonNull
    @Override
    protected PageConfig getPageConfig() {
        return new PageConfig.Builder().create();
    }

    @Override
    public void onData() {
        HomeViewModel viewModel =
            new ViewModelProvider(this).get(HomeViewModel.class);
        setPageBackground(R.drawable.home_page_bg);

        viewModel.getLiveData().observe(getViewLifecycleOwner(),
            beans -> baseListAdapter.setData(beans));
    }

    @Override
    protected String getTitle() {
        return getString(R.string.bottom_nav_title_home);
    }
}