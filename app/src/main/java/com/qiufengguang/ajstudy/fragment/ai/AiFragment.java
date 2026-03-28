package com.qiufengguang.ajstudy.fragment.ai;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.activity.ai.AiActivity;
import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.fragment.base.BaseListFragment;
import com.qiufengguang.ajstudy.fragment.base.PageConfig;
import com.qiufengguang.ajstudy.view.DynamicToolbar;

/**
 * 二级页（格网样式）
 *
 * @author qiufengguang
 * @since 2026/3/4 2:17
 */
public class AiFragment extends BaseListFragment {

    public static AiFragment newInstance(Bundle args) {
        AiFragment f = new AiFragment();
        f.setArguments(args);
        return f;
    }

    @NonNull
    @Override
    protected PageConfig getPageConfig() {
        return new PageConfig.Builder()
            .setHasNaviBar(false)
            .setStatusBarMode(PageConfig.StatusBarMode.NONE)
            .setTitleBarMode(DynamicToolbar.Mode.GONE)
            .setEnablePageBounce(false)
            .create();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void setupLayout(PageConfig config) {
        super.setupLayout(config);
        baseBinding.bounceContainer.setOnTouchEventListener(event -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (getActivity() instanceof AiActivity) {
                    ((AiActivity) getActivity()).hideKeyboard();
                }
            }
        });
    }

    @Override
    public void showPageState(State state) {
    }

    @Override
    public void onData() {
        AiViewModel viewModel = new ViewModelProvider(this).get(AiViewModel.class);
        viewModel.getLiveData().observe(getViewLifecycleOwner(), layoutData ->
            baseListAdapter.setData(layoutData));
    }
}