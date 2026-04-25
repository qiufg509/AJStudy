package com.qiufengguang.ajstudy.fragment.home;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.fragment.base.BaseGridFragment;
import com.qiufengguang.ajstudy.fragment.base.PageConfig;
import com.qiufengguang.ajstudy.view.DynamicToolbar;

/**
 * 首页Fragment
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class HomeFragment extends BaseGridFragment {
    private HomeViewModel viewModel;

    @NonNull
    @Override
    protected PageConfig getPageConfig() {
        return new PageConfig.Builder()
            .setTitleBarMode(DynamicToolbar.Mode.TITLE_AI)
            .create();
    }

    @Override
    public void onData() {
        setPageBackground(R.drawable.home_page_bg);

        viewModel.getLiveData().observe(getViewLifecycleOwner(),
            beans -> baseListAdapter.setData(beans));
        setTitle(getString(R.string.bottom_nav_title_home));
    }

    @Override
    public void showPageState(State state) {
        if (viewModel == null) {
            viewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        }
        if (viewModel.getLiveData().getValue() == null
            || viewModel.getLiveData().getValue().isEmpty()) {
            super.showPageState(state);
        }
    }

    @Override
    public void retry() {
        viewModel.retry();
    }
}