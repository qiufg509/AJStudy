package com.qiufengguang.ajstudy.fragment.home;

import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.FragmentHomeBinding;
import com.qiufengguang.ajstudy.fragment.base.BaseFragment;

/**
 * 首页Fragment
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class HomeFragment extends BaseFragment {

    private FragmentHomeBinding binding;

    private HomeAdapter homeAdapter;

    @Override
    protected boolean isDarkBackgroundImage() {
        return false;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.bottom_nav_title_home);
    }

    @Override
    protected void setupContent() {
        // 使用单独的布局文件注入内容
        binding = FragmentHomeBinding.inflate(
            LayoutInflater.from(requireContext()),
            getContainer(),
            true
        );
        HomeViewModel viewModel =
            new ViewModelProvider(this).get(HomeViewModel.class);
        setPageBackground(R.drawable.home_page_bg);


        binding.recyclerHome.setLayoutManager(new LinearLayoutManager(requireContext()));

        homeAdapter = new HomeAdapter(getViewLifecycleOwner());
        binding.recyclerHome.setAdapter(homeAdapter);

        viewModel.getLiveData().observe(getViewLifecycleOwner(),
            beans -> homeAdapter.setData(beans));

        setupScrollListener();
    }

    private void setupScrollListener() {
        binding.recyclerHome.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        // 滚动停止时检查 Banner 可见性
                        homeAdapter.activeBannersIfVisible(binding.recyclerHome);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        // 滚动时暂停 Banner
                        homeAdapter.pauseAllBanners();
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 实时检查 Banner 可见性
                if (!recyclerView.isComputingLayout()) {
                    homeAdapter.activeBannersIfVisible(binding.recyclerHome);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // Activity 恢复时检查 Banner 可见性
        homeAdapter.activeBannersIfVisible(binding.recyclerHome);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Activity 暂停时停止所有 Banner
        homeAdapter.pauseAllBanners();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 释放所有资源
        homeAdapter.releaseAllResources();
        binding.recyclerHome.setAdapter(null);
        binding = null;
    }
}