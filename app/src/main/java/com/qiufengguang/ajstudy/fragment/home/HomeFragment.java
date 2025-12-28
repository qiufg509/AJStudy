package com.qiufengguang.ajstudy.fragment.home;

import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.BannerBean;
import com.qiufengguang.ajstudy.databinding.FragmentHomeBinding;
import com.qiufengguang.ajstudy.dialog.Dialog;
import com.qiufengguang.ajstudy.dialog.IDialog;
import com.qiufengguang.ajstudy.dialog.manager.DialogWrapper;
import com.qiufengguang.ajstudy.dialog.manager.DialogsManager;
import com.qiufengguang.ajstudy.fragment.base.BaseFragment;
import com.qiufengguang.ajstudy.global.GlobalApp;

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

        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 5);
        binding.recyclerGrid.setLayoutManager(layoutManager);
        binding.recyclerGrid.setAdapter(new GridCardAdapter());
        binding.recyclerGrid.addItemDecoration(new GridSpacingItemDecoration(5,
            getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_s)));
    }

    private void handleBannerClick(int position, BannerBean item) {
        switch (position) {
            case 0:
                // 默认单按钮对话框
                buildDefaultDialog1(item.getTitle()).show();
                break;
            case 1:
                // 默认双按钮对话框
                buildDefaultDialog2(item.getTitle()).show();
                break;
            case 2:
                // 自定义对话框
                new Dialog.Builder(requireActivity())
                    .setDialogView(R.layout.dialog_ad)
                    .setWindowBackgroundP(0.5f)
                    .setCancelable(false)
                    .setAnimStyle(R.style.DialogAnimScale)
                    .setBuildChildListener((dialog, parent, layoutRes) ->
                        parent.findViewById(R.id.iv_close)
                            .setOnClickListener(v -> dialog.dismiss()))
                    .show();
                break;
            default:
                // 多个对话框依次弹出
                Dialog.Builder builder1 = buildDefaultDialog1(null);
                Dialog.Builder builder2 = buildDefaultDialog2(item.getTitle());
                DialogsManager.getInstance().requestShow(new DialogWrapper(builder1));
                DialogsManager.getInstance().requestShow(new DialogWrapper(builder2));
                break;
        }
    }

    private Dialog.Builder buildDefaultDialog1(String title) {
        return new Dialog.Builder(requireActivity())
            .setTitle(title)
            .setAnimStyle(0)
            .setContent("你可以找到所有適用於安卓系統的最佳照片背景去除應用，用於去除照片中的背景。此外，如果你想在線去除圖片背景，最好使用 Vidmore 免費線上背景去除器。")
            .setPositiveButton("按钮文案", IDialog::dismiss);
    }

    private Dialog.Builder buildDefaultDialog2(String title) {
        return new Dialog.Builder(requireActivity())
            .setTitle(title)
            .setCancelableOutSide(true)
            .setCancelable(false)
            .setAnimStyle(R.style.DialogAnimSlideBottom)
            .setContent("這款基於網路的工具可以幫助你高效去除照片中的背景，是一款功能強大的線上照片背景去除工具。")
            .setPositiveButton(dialog -> {
                Toast.makeText(GlobalApp.getContext(), "确定", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            })
            .setNegativeButton(dialog -> {
                Toast.makeText(GlobalApp.getContext(), "取消", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
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