package com.qiufengguang.ajstudy.ui.notifications;

import android.view.LayoutInflater;
import android.view.View;

import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.databinding.FragmentNotificationsBinding;
import com.qiufengguang.ajstudy.ui.base.BaseFragment;

public class NotificationsFragment extends BaseFragment {
    private FragmentNotificationsBinding binding;

    @Override
    protected void setPageBackground() {
        baseBinding.backgroundImage.setVisibility(View.GONE);
    }

    @Override
    protected boolean isDarkBackgroundImage() {
        return false;
    }

    @Override
    protected void setupContent() {
        baseBinding.toolbar.setTitle("我的");
        // 使用单独的布局文件注入内容
        binding = FragmentNotificationsBinding.inflate(
            LayoutInflater.from(requireContext()),
            baseBinding.contentContainer,
            true
        );

        NotificationsViewModel viewModel =
            new ViewModelProvider(this).get(NotificationsViewModel.class);
        viewModel.getText().observe(getViewLifecycleOwner(), title -> {
            baseBinding.toolbar.setTitle(title);
            binding.textNotifications.setText("这是内容");
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}