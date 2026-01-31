package com.qiufengguang.ajstudy.fragment.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.FragmentBaseBinding;
import com.qiufengguang.ajstudy.utils.DisplayMetricsHelper;
import com.qiufengguang.ajstudy.utils.StatusBarUtil;
import com.qiufengguang.ajstudy.view.EndlessRecyclerViewScrollListener;

/**
 * 列表页基类Fragment
 *
 * @author qiufengguang
 * @since 2025/11/27 17:42
 */
public abstract class BaseListFragment extends Fragment {

    protected FragmentBaseBinding baseBinding;

    protected BaseListAdapter baseListAdapter;

    public View onCreateView(
        @NonNull LayoutInflater inflater,
        ViewGroup container,
        Bundle savedInstanceState
    ) {
        baseBinding = FragmentBaseBinding.inflate(inflater, container, false);
        return baseBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        PageConfig config = getPageConfig();
        StatusBarUtil.setLightStatusBar(requireActivity(), !config.isDarkBackground);

        setupLayout(config);
        setupContent();
        onData();
        setupScrollListener();
        setTitle();
    }

    @Override
    public void onResume() {
        super.onResume();
        // Activity 恢复时检查卡片可见性
        baseListAdapter.activeCardsIfVisible(baseBinding.recyclerContainer);
    }

    @Override
    public void onPause() {
        super.onPause();
        // Activity 暂停时停止所有卡片
        baseListAdapter.pauseAllCards();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // 释放所有资源
        baseListAdapter.releaseAllResources();
        baseListAdapter = null;
        baseBinding.recyclerContainer.setAdapter(null);
        baseBinding.recyclerContainer.setLayoutManager(null);
        baseBinding.recyclerContainer.clearOnScrollListeners();
        baseBinding = null;
    }

    private void setupLayout(PageConfig config) {
        int statusBarHeight = DisplayMetricsHelper.getStatusBarHeight(requireActivity());
        int titleBarHeight = DisplayMetricsHelper.getTitleBarHeight(requireActivity());

        FrameLayout titleBarRoot = baseBinding.titleBar.getRoot();
        titleBarRoot.setPadding(
            titleBarRoot.getPaddingLeft(),
            statusBarHeight,
            titleBarRoot.getPaddingRight(),
            titleBarRoot.getPaddingBottom()
        );
        // 设置titleBar高度为titleBarHeight + 状态栏高度
        int totalHeight = titleBarHeight + statusBarHeight;
        ViewGroup.LayoutParams toolbarParams = titleBarRoot.getLayoutParams();
        toolbarParams.height = totalHeight;
        titleBarRoot.setLayoutParams(toolbarParams);

        int naviBarHeight = DisplayMetricsHelper.getNavigationBarHeight(requireActivity());

        // 设置内容在titleBar之下
        ViewGroup.LayoutParams containerLayoutParams = baseBinding.bounceContainer.getLayoutParams();
        if (containerLayoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) containerLayoutParams;
            layoutParams.topMargin = config.overlayTitleBar ? 0 : totalHeight;
            layoutParams.bottomMargin = (config.hasNaviBar && !config.overlayNaviBar) ? naviBarHeight : 0;
            baseBinding.bounceContainer.setLayoutParams(layoutParams);
        }
        baseBinding.recyclerContainer.setPaddingRelative(
            baseBinding.recyclerContainer.getPaddingStart(),
            baseBinding.recyclerContainer.getTop(),
            baseBinding.recyclerContainer.getPaddingEnd(),
            (config.hasNaviBar && config.overlayNaviBar) ? naviBarHeight : 0
        );
        baseBinding.recyclerContainer.setClipToPadding(!config.hasNaviBar || !config.overlayNaviBar);
    }

    /**
     * 设置页面内容
     */
    protected void setupContent() {
        baseBinding.recyclerContainer.setLayoutManager(new LinearLayoutManager(requireContext()));
        baseListAdapter = new BaseListAdapter(getViewLifecycleOwner());
        baseBinding.recyclerContainer.setAdapter(baseListAdapter);
    }

    private void setTitle() {
        setTitle(getTitle());
    }

    public void setTitle(String title) {
        baseBinding.titleBar.barTitle.setText(title);
    }

    private void setupScrollListener() {
        baseBinding.recyclerContainer.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE:
                        // 滚动停止时检查 Banner 可见性
                        baseListAdapter.activeCardsIfVisible(baseBinding.recyclerContainer);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING:
                    case RecyclerView.SCROLL_STATE_SETTLING:
                        // 滚动时暂停 Banner
                        baseListAdapter.pauseAllCards();
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                // 实时检查 Banner 可见性
                if (!recyclerView.isComputingLayout()) {
                    baseListAdapter.activeCardsIfVisible(baseBinding.recyclerContainer);
                }
            }
        });

        RecyclerView.LayoutManager layoutManager = baseBinding.recyclerContainer.getLayoutManager();
        if (!(layoutManager instanceof LinearLayoutManager)) {
            return;
        }
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(
            (LinearLayoutManager) layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount) {
                BaseListFragment.this.onLoadMore(page, totalItemsCount);
            }
        };

        baseBinding.recyclerContainer.addOnScrollListener(scrollListener);
    }

    /**
     * 设置页面背景图，不设置或者入参0则默认背景色R.color.ajstudy_window_background
     *
     * @param resId 图片资源
     */
    protected void setPageBackground(@DrawableRes int resId) {
        if (resId == 0) {
            baseBinding.getRoot().setBackgroundResource(R.color.ajstudy_window_background);
            baseBinding.backgroundImage.setVisibility(View.GONE);
            baseBinding.backgroundImage.setBackground(null);
        } else {
            baseBinding.getRoot().setBackground(null);
            baseBinding.backgroundImage.setImageResource(resId);
            baseBinding.backgroundImage.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 获取页面配置项
     *
     * @return PageConfig
     */
    @NonNull
    protected abstract PageConfig getPageConfig();

    /**
     * 处理页面数据
     */
    public abstract void onData();

    /**
     * 加载更多数据
     * 页面滑动到倒数第三条数据时触发
     *
     * @param page            当前页数
     * @param totalItemsCount item数量
     */
    public void onLoadMore(int page, int totalItemsCount) {
    }


    /**
     * 可返回null，待需要的时候再调用setTitle设置标题
     *
     * @return 标题
     */
    protected abstract String getTitle();
}