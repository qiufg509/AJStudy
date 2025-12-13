package com.qiufengguang.ajstudy.ui.base;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.FragmentBaseBinding;
import com.qiufengguang.ajstudy.utils.DisplayMetricsHelper;
import com.qiufengguang.ajstudy.utils.StatusBarUtil;

/**
 * 基类Fragment
 *
 * @author qiufengguang
 * @since 2025/11/27 17:42
 */
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
        StatusBarUtil.setLightStatusBar(requireActivity(), isDarkBackgroundImage());

        setPageBackground();
        setupBar();
        setupContent();
        setTitle();
    }

    private void setTitle() {
        setTitle(getTitle());
    }

    public void setTitle(String title) {
        baseBinding.titleBar.barTitle.setText(title);
    }

    private void setupBar() {
        int statusBarHeight = DisplayMetricsHelper.getStatusBarHeight(requireActivity());
        int actionBarHeight = DisplayMetricsHelper.getActionBarHeight(requireActivity());

        FrameLayout titleBarRoot = baseBinding.titleBar.getRoot();
        titleBarRoot.setPadding(
            titleBarRoot.getPaddingLeft(),
            statusBarHeight,
            titleBarRoot.getPaddingRight(),
            titleBarRoot.getPaddingBottom()
        );
        // 设置titleBar最小高度为ActionBar高度 + 状态栏高度
        int totalHeight = actionBarHeight + statusBarHeight;
        ViewGroup.LayoutParams toolbarParams = titleBarRoot.getLayoutParams();
        toolbarParams.height = totalHeight;
        titleBarRoot.setLayoutParams(toolbarParams);

        // 设置内容在titleBar之下
        ViewGroup.LayoutParams containerLayoutParams = baseBinding.contentContainer.getLayoutParams();
        if (containerLayoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) containerLayoutParams;
            layoutParams.topMargin = totalHeight;
            baseBinding.contentContainer.setLayoutParams(layoutParams);
        }

        // 设置背景图在导航栏之上，避免过度绘制
        ViewGroup.LayoutParams bgLayoutParams = baseBinding.backgroundImage.getLayoutParams();
        if (bgLayoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) bgLayoutParams;
            layoutParams.bottomMargin = getResources().getDimensionPixelSize(R.dimen.ajstudy_bottom_navigation_height);
            baseBinding.backgroundImage.setLayoutParams(layoutParams);
        }
    }

    /**
     * 页面内容容器
     * 子页面内容要attach到该容器中inflate(LayoutInflater.from(requireContext()), getContainer(), true);
     *
     * @return FrameLayout
     */
    public FrameLayout getContainer() {
        return baseBinding.contentContainer;
    }

    /**
     * 设置背景图，对背景ImageView控件操作
     * eg：baseBinding.backgroundImage.setImageResource(R.drawable.home_page_bg);
     */
    protected abstract void setPageBackground();

    /**
     * 是否为深色背景
     * 深色背景则设置白色状态栏文字、icon
     * 浅色背景则设置灰色状态栏文字、icon
     *
     * @return true深色 false浅色
     */
    protected abstract boolean isDarkBackgroundImage();

    /**
     * 可返回null，待需要的时候再调用setTitle设置标题
     *
     * @return 标题
     */
    protected abstract String getTitle();

    /**
     * 设置页面内容
     */
    protected abstract void setupContent();
}