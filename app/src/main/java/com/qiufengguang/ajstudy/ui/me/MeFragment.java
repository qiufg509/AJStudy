package com.qiufengguang.ajstudy.ui.me;

import android.view.LayoutInflater;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.databinding.FragmentMeBinding;
import com.qiufengguang.ajstudy.ui.base.BaseFragment;

/**
 * 我的页面Fragment
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class MeFragment extends BaseFragment {
    private FragmentMeBinding binding;

    @Override
    protected void setPageBackground() {
        baseBinding.backgroundImage.setVisibility(View.GONE);
    }

    @Override
    protected boolean isDarkBackgroundImage() {
        return true;
    }

    @Override
    protected String getTitle() {
        return "我的";
    }

    @Override
    protected void setupContent() {
        // 使用单独的布局文件注入内容
        binding = FragmentMeBinding.inflate(
            LayoutInflater.from(requireContext()),
            baseBinding.contentContainer,
            true
        );

        MeViewModel viewModel =
            new ViewModelProvider(this).get(MeViewModel.class);
        viewModel.getText().observe(getViewLifecycleOwner(), title -> {
            setTitle(title);
            binding.tvContent.setText("这是内容");
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}