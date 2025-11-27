package com.qiufengguang.ajstudy.ui.home;

import android.view.LayoutInflater;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.appbar.MaterialToolbar;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.FragmentHomeBinding;
import com.qiufengguang.ajstudy.ui.base.BaseFragment;

public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;

    @Override
    protected void setPageBackground() {
        baseBinding.backgroundImage.setImageResource(R.mipmap.home_page_bg);
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