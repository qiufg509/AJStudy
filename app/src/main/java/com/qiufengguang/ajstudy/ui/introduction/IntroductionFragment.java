package com.qiufengguang.ajstudy.ui.introduction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.activity.detail.DetailViewModel;
import com.qiufengguang.ajstudy.databinding.FragmentIntroductionBinding;

/**
 * 详情页-介绍子页面
 *
 * @author qiufengguang
 * @since 2025/12/10 0:18
 */
public class IntroductionFragment extends Fragment {

    private FragmentIntroductionBinding binding;
    private DetailViewModel viewModel;
    private IntroductionAdapter adapter;

    public IntroductionFragment() {
    }

    public static IntroductionFragment newInstance() {
        return new IntroductionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentIntroductionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(DetailViewModel.class);

        // 初始化RecyclerView
        setupRecyclerView();

        // 观察数据变化
        observeData();
    }

    private void setupRecyclerView() {
        adapter = new IntroductionAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);
    }

    private void observeData() {
        viewModel.getIntroduction().observe(getViewLifecycleOwner(), introductionList -> {
            if (introductionList != null) {
                adapter.setIntroduction(introductionList);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}