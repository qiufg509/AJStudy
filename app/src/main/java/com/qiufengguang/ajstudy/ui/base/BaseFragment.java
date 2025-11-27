package com.qiufengguang.ajstudy.ui.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.qiufengguang.ajstudy.databinding.FragmentBaseBinding;
import com.qiufengguang.ajstudy.utils.StatusBarUtil;

public abstract class BaseFragment extends Fragment {

    protected FragmentBaseBinding baseBinding;

    public View onCreateView(@NonNull LayoutInflater inflater,
        ViewGroup container, Bundle savedInstanceState) {
        baseBinding = FragmentBaseBinding.inflate(inflater, container, false);
        return baseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        StatusBarUtil.ensureTransparentStatusBar(requireActivity(), isDarkBackgroundImage());
        setPageBackground();
        setupToolbar();
        setupContent();
    }


    private void setupToolbar() {
        baseBinding.toolbar.setNavigationIcon(null);
        // 设置Toolbar样式
        FragmentActivity activity = getActivity();
        if (!(activity instanceof AppCompatActivity)) {
            return;
        }
        // 设置 Toolbar 为 ActionBar
        AppCompatActivity appCompatActivity = (AppCompatActivity) activity;
        appCompatActivity.setSupportActionBar(baseBinding.toolbar);

        // 确保没有默认的 ActionBar 行为
        ActionBar actionBar = appCompatActivity.getSupportActionBar();
        if (actionBar == null) {
            return;
        }
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowHomeEnabled(false);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(null);
    }

    @Override
    public void onResume() {
        super.onResume();
        // 每次Fragment可见时确保状态栏设置
        StatusBarUtil.ensureTransparentStatusBar(requireActivity(), isDarkBackgroundImage());
    }

    protected abstract void setPageBackground();

    protected abstract boolean isDarkBackgroundImage();

    protected abstract void setupContent();
}