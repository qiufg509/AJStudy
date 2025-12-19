package com.qiufengguang.ajstudy.ui.home;

import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.BannerBean;
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

    private BannerWrapper bannerWrapper;

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

        bannerWrapper = new BannerWrapper(binding.recyclerBanner, binding.indicatorContainer,
            this::handleBannerClick);
        viewModel.getLiveData().observe(getViewLifecycleOwner(),
            bannerBeans -> {
                bannerWrapper.setBannerBeans(bannerBeans);
                bannerWrapper.startAutoScroll();
            });
    }

    private void handleBannerClick(int position, BannerBean item) {
        Toast.makeText(requireContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (bannerWrapper != null) {
            bannerWrapper.resumeAutoScroll();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (bannerWrapper != null) {
            bannerWrapper.pauseAutoScroll();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bannerWrapper != null) {
            bannerWrapper.release();
        }
        binding = null;
    }
}