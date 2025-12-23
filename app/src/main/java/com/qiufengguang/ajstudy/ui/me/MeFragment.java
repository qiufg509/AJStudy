package com.qiufengguang.ajstudy.ui.me;

import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.activity.main.MainActivity;
import com.qiufengguang.ajstudy.databinding.FragmentMeBinding;
import com.qiufengguang.ajstudy.ui.base.BaseFragment;

/**
 * 我的页面Fragment
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class MeFragment extends BaseFragment {
    private static final String TAG = "MeFragment";

    private FragmentMeBinding binding;

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
        viewModel.getLiveData().observe(getViewLifecycleOwner(), this::selectedTheme);

        setPageBackground(R.drawable.me_head_bg);

        for (int index = 0, sum = binding.layoutTheme.getChildCount(); index < sum; index++) {
            View childAt = binding.layoutTheme.getChildAt(index);
            if (childAt == null) {
                continue;
            }
            childAt.setOnClickListener(v -> {
                Object tag = v.getTag();
                int themeIndex;
                try {
                    themeIndex = Integer.parseInt((String) tag);
                } catch (NumberFormatException e) {
                    themeIndex = 0;
                    Log.e(TAG, "parseInt error.");
                }
                if (viewModel.getThemeIndex() == themeIndex) {
                    return;
                }
                viewModel.saveThemeIndex(themeIndex);

                Intent intent = new Intent(requireContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.putExtra("restart_theme", true);
                startActivity(intent);
            });
        }
    }

    /**
     * 选择主题
     *
     * @param themeIndex 主题序号
     */
    private void selectedTheme(Integer themeIndex) {
        for (int index = 0, sum = binding.layoutTheme.getChildCount(); index < sum; index++) {
            View childAt = binding.layoutTheme.getChildAt(index);
            if (!(childAt instanceof ImageView)) {
                continue;
            }
            ImageView imageView = (ImageView) childAt;
            if (index == themeIndex) {
                imageView.setImageResource(R.drawable.ic_checkmark);
            } else {
                imageView.setImageDrawable(null);
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}