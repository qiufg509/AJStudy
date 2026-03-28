package com.qiufengguang.ajstudy.fragment.ai;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.activity.ai.AiActivity;
import com.qiufengguang.ajstudy.activity.ai.AiViewModel;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.base.BaseCardBean;
import com.qiufengguang.ajstudy.data.model.ChatMessage;
import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.fragment.base.BaseListFragment;
import com.qiufengguang.ajstudy.fragment.base.PageConfig;
import com.qiufengguang.ajstudy.view.DynamicToolbar;

/**
 * Ai对话列表页面
 *
 * @author qiufengguang
 * @since 2026/3/28 18:20
 */
public class AiFragment extends BaseListFragment {
    private AiViewModel viewModel;

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
    public OnItemClickListener<BaseCardBean> getListener() {
        return (context, data) -> {
            if (!(data instanceof ChatMessage)) {
                return;
            }
            ChatMessage message = (ChatMessage) data;
            viewModel.sendMessage(message.getContent());
        };
    }

    @Override
    public void onData() {
        setTitle("新对话");
        viewModel = new ViewModelProvider(requireActivity()).get(AiViewModel.class);
        viewModel.getLiveData().observe(getViewLifecycleOwner(), layoutData ->
            baseListAdapter.setData(layoutData));
    }
}