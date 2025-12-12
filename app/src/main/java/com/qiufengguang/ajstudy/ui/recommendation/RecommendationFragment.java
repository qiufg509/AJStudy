package com.qiufengguang.ajstudy.ui.recommendation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.activity.detail.DetailViewModel;
import com.qiufengguang.ajstudy.databinding.FragmentRecommendationBinding;

/**
 * 详情页-推荐子页面
 *
 * @author qiufengguang
 * @since 2025/12/10 0:18
 */
public class RecommendationFragment extends Fragment {

    private FragmentRecommendationBinding binding;
    private DetailViewModel viewModel;
    private RecommendationAdapter adapter;

    public RecommendationFragment() {
    }

    public static RecommendationFragment newInstance() {
        return new RecommendationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        binding = FragmentRecommendationBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // 初始化ViewModel
        viewModel = new ViewModelProvider(requireActivity()).get(DetailViewModel.class);

        // 初始化RecyclerView
        setupRecyclerView();

        // 设置监听器
        setupListeners();

        // 观察数据变化
        observeData();
    }

    private void setupRecyclerView() {
        adapter = new RecommendationAdapter();
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(RecyclerView.VERTICAL);

        binding.recyclerView.setLayoutManager(layoutManager);
        binding.recyclerView.setAdapter(adapter);

        // 设置滚动监听，避免与父布局滚动冲突
        binding.recyclerView.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });

        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                // 当RecyclerView滚动到顶部时，允许父布局拦截触摸事件
                if (!recyclerView.canScrollVertically(-1)) {
                    recyclerView.getParent().requestDisallowInterceptTouchEvent(false);
                }
            }
        });
    }

    private void setupListeners() {
        // Item点击事件
        adapter.setOnItemClickListener((position, recommendation) ->
            Toast.makeText(getContext(), "点击应用: " + recommendation.getName(), Toast.LENGTH_SHORT).show()
        );

        // 安装按钮点击事件
        adapter.setOnInstallClickListener((position, recommendation) ->
            Toast.makeText(getContext(), "安装: " + recommendation.getName(), Toast.LENGTH_SHORT).show()
        );
    }

    private void observeData() {
        viewModel.getRecommendations().observe(getViewLifecycleOwner(), recommendations -> {
            if (recommendations != null) {
                adapter.setRecommendations(recommendations);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}