package com.qiufengguang.ajstudy.fragment.me;

import android.content.Intent;
import android.view.LayoutInflater;

import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.activity.main.MainActivity;
import com.qiufengguang.ajstudy.card.grid.GridCardWrapper;
import com.qiufengguang.ajstudy.databinding.FragmentMeBinding;
import com.qiufengguang.ajstudy.fragment.base.BaseFragment;

/**
 * 我的页面Fragment
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class MeFragment extends BaseFragment {
    private static final String TAG = "MeFragment";

    private FragmentMeBinding binding;

    private GridCardWrapper gridCardWrapper;

    @Override
    protected boolean isDarkBackgroundImage() {
        return true;
    }

    @Override
    protected String getTitle() {
        return getString(R.string.bottom_nav_title_me);
    }

    @Override
    protected void setupContent() {
        // 使用单独的布局文件注入内容
        binding = FragmentMeBinding.inflate(
            LayoutInflater.from(requireContext()),
            baseBinding.contentContainer,
            true
        );

        MeViewModel viewModel =
            new ViewModelProvider(this).get(MeViewModel.class);

        setPageBackground(R.drawable.me_head_bg);

        gridCardWrapper = new GridCardWrapper.Builder()
            .setRecyclerView(binding.recyclerGrid)
            .setItemType(GridCardWrapper.TYPE_IMAGE)
            .setListener((context, bean) -> {
                viewModel.saveThemeIndex(bean);

                Intent intent = new Intent(requireContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("restart_theme", true);
                startActivity(intent);
            })
            .create();
        gridCardWrapper.show();

        viewModel.getThemeLiveData().observe(getViewLifecycleOwner(),
            gridCardBeans -> gridCardWrapper.setData(gridCardBeans));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (gridCardWrapper != null) {
            gridCardWrapper.release();
        }
        binding = null;
    }
}