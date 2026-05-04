package com.qiufengguang.ajstudy.fragment.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.card.state.StateCard;
import com.qiufengguang.ajstudy.data.base.BaseCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.base.SingleLayoutData;
import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.data.model.StateCardBean;
import com.qiufengguang.ajstudy.databinding.FragmentBaseBinding;
import com.qiufengguang.ajstudy.utils.DisplayMetricsHelper;
import com.qiufengguang.ajstudy.utils.StatusBarUtil;
import com.qiufengguang.ajstudy.view.DynamicToolbar;
import com.qiufengguang.ajstudy.view.EndlessRecyclerViewScrollListener;
import com.qiufengguang.ajstudy.view.OnToolBarListener;

import java.util.ArrayList;
import java.util.List;

/**
 * 列表页基类Fragment
 *
 * @author qiufengguang
 * @since 2025/11/27 17:42
 */
public abstract class BaseListFragment extends Fragment {

    protected FragmentBaseBinding baseBinding;

    protected BaseListAdapter baseListAdapter;

    private PageSpacingDecoration decor;

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
        if (config.statusBarMode == PageConfig.StatusBarMode.LIGHT) {
            StatusBarUtil.setLightStatusBar(requireActivity(), false);
        } else if (config.statusBarMode == PageConfig.StatusBarMode.DARK) {
            StatusBarUtil.setLightStatusBar(requireActivity(), true);
        }

        setupLayout(config);
        setupContent();
        showPageState(State.LOADING);
        onData();
        setupScrollListener();
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
        baseListAdapter.setListener(null);
        baseListAdapter = null;
        baseBinding.recyclerContainer.setAdapter(null);
        baseBinding.recyclerContainer.setLayoutManager(null);
        baseBinding.recyclerContainer.setItemAnimator(null);
        baseBinding.recyclerContainer.clearOnScrollListeners();
        baseBinding.recyclerContainer.removeItemDecoration(decor);
        decor = null;
        baseBinding = null;
    }

    protected void setupLayout(PageConfig config) {
        int totalHeight = StatusBarUtil.adaptTitleBar(baseBinding.titleBar);

        int naviBarHeight = DisplayMetricsHelper.getNavigationBarHeight(requireActivity());

        // 设置内容在titleBar之下
        ViewGroup.LayoutParams containerLayoutParams = baseBinding.bounceContainer.getLayoutParams();
        if (containerLayoutParams instanceof ViewGroup.MarginLayoutParams layoutParams) {
            layoutParams.topMargin =
                (config.titleBarMode == DynamicToolbar.Mode.GONE || config.overlayTitleBar)
                    ? 0 : totalHeight;
            layoutParams.bottomMargin =
                (config.hasNaviBar && !config.overlayNaviBar)
                    ? naviBarHeight : 0;
            baseBinding.bounceContainer.setLayoutParams(layoutParams);
        }
        baseBinding.recyclerContainer.setPaddingRelative(
            baseBinding.recyclerContainer.getPaddingStart(),
            baseBinding.recyclerContainer.getTop(),
            baseBinding.recyclerContainer.getPaddingEnd(),
            (config.hasNaviBar && config.overlayNaviBar) ? naviBarHeight : 0
        );
        baseBinding.recyclerContainer.setClipToPadding(!config.hasNaviBar || !config.overlayNaviBar);

        if (decor == null) {
            decor = new PageSpacingDecoration(requireContext());
        }
        baseBinding.recyclerContainer.removeItemDecoration(decor);
        baseBinding.recyclerContainer.addItemDecoration(decor);

        if (config.titleBarMode != DynamicToolbar.Mode.GONE) {
            baseBinding.titleBar.setVisibility(View.VISIBLE);
            setupToolbar(config.titleBarMode);
        } else {
            baseBinding.titleBar.setVisibility(View.GONE);
        }

        if (!config.enableBounce) {
            baseBinding.bounceContainer.setEnablePullDownBounce(false);
            baseBinding.bounceContainer.setEnablePullUpBounce(false);
        }
        if (config.titleBarMode != DynamicToolbar.Mode.GONE && config.overlayTitleBar) {
            int endThreshold = getResources().getDimensionPixelSize(R.dimen.card_height_image_200);
            TitleBarScrollListener listener = new TitleBarScrollListener(
                baseBinding.titleBar, endThreshold - totalHeight);
            baseBinding.recyclerContainer.addOnScrollListener(listener);
        }
    }

    private void setupToolbar(DynamicToolbar.Mode mode) {
        baseBinding.titleBar.setMode(mode);
        if (mode == DynamicToolbar.Mode.BACK_TITLE ||
            mode == DynamicToolbar.Mode.BACK_TITLE_SHARE) {
            baseBinding.titleBar.setListener(new OnToolBarListener() {
                @Override
                public void onBackClick() {
                    requireActivity().getOnBackPressedDispatcher().onBackPressed();
                }
            });
        }
    }

    /**
     * 设置页面内容
     */
    protected void setupContent() {
        baseBinding.recyclerContainer.setLayoutManager(new LinearLayoutManager(requireContext()));
        baseListAdapter = new BaseListAdapter(getViewLifecycleOwner());
        baseBinding.recyclerContainer.setAdapter(baseListAdapter);
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public void setTitle(String title) {
        baseBinding.titleBar.setTitle(title);
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

        onLoadMoreListener();
        baseListAdapter.setListener(getListener());
    }

    /**
     * 加载更多监听
     */
    private void onLoadMoreListener() {
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
     * 设置页面背景图，入参0则默认背景色R.color.ajstudy_window_background
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
     * 设置页面状态
     *
     * @param state 状态{@link State}
     */
    public void showPageState(State state) {
        if (baseListAdapter != null && state != null) {
            SingleLayoutData<StateCardBean> stateCardData = LayoutDataFactory.createSingle(
                StateCard.LAYOUT_ID, new StateCardBean(state));
            List<LayoutData<?>> dataList = new ArrayList<>(1);
            dataList.add(stateCardData);
            baseListAdapter.setData(dataList);
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
     * 重试
     */
    public void retry() {
    }

    /**
     * 卡片点击回调监听（默认点击重试，可复写扩展功能）
     * 卡片CardHolder复写bind(LayoutData<?> data, OnItemClickListener<BaseCardBean> listener)
     *
     * @return OnItemClickListener
     */
    public OnItemClickListener<BaseCardBean> getListener() {
        return (context, data) -> retry();
    }
}