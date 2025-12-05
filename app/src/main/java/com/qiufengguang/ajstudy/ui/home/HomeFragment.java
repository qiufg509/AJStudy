package com.qiufengguang.ajstudy.ui.home;

import android.view.LayoutInflater;

import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.FragmentHomeBinding;
import com.qiufengguang.ajstudy.ui.base.BaseFragment;

/**
 * 首页Fragment
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;

    @Override
    protected void setPageBackground() {
        baseBinding.backgroundImage.setImageResource(R.drawable.home_page_bg);
    }

    @Override
    protected boolean isDarkBackgroundImage() {
        return true;
    }

    @Override
    protected void setupContent() {
        baseBinding.toolbar.setTitle("首页");
        // 使用单独的布局文件注入内容
        binding = FragmentHomeBinding.inflate(
            LayoutInflater.from(requireContext()),
            baseBinding.contentContainer,
            true
        );
        HomeViewModel viewModel =
            new ViewModelProvider(this).get(HomeViewModel.class);
        viewModel.getLiveData().observe(getViewLifecycleOwner(),
            title -> baseBinding.toolbar.setTitle(title));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}