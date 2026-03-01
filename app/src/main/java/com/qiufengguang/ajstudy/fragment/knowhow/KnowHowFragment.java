package com.qiufengguang.ajstudy.fragment.knowhow;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.fragment.base.BaseStaggeredFragment;
import com.qiufengguang.ajstudy.fragment.base.PageConfig;

import java.util.List;

/**
 * 知识列表页
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class KnowHowFragment extends BaseStaggeredFragment {
    private KnowHowViewModel viewModel;

    @NonNull
    @Override
    protected PageConfig getPageConfig() {
        return new PageConfig.Builder().setOverlayNaviBar(true).create();
    }

    @Override
    public void onData() {
        viewModel = new ViewModelProvider(this).get(KnowHowViewModel.class);

        // 绑定数据
        viewModel.getLiveData().observe(getViewLifecycleOwner(), list ->
            baseListAdapter.setData(list));

        setTitle("瀑布流列表");
    }

    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        List<LayoutData<?>> pageData = viewModel.getPageData(page);
        baseListAdapter.addData(pageData);
    }
}