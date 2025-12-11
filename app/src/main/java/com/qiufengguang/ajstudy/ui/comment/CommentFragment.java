package com.qiufengguang.ajstudy.ui.comment;

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
import com.qiufengguang.ajstudy.databinding.FragmentCommentBinding;

public class CommentFragment extends Fragment {

    private FragmentCommentBinding binding;
    private DetailViewModel viewModel;
    private CommentAdapter adapter;

    public CommentFragment() {
    }

    public static CommentFragment newInstance() {
        return new CommentFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        binding = FragmentCommentBinding.inflate(inflater, container, false);
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
        adapter = new CommentAdapter();
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
        // 发表评价点击
        binding.tvPostReview.setOnClickListener(v ->
            Toast.makeText(getContext(), "发表评论", Toast.LENGTH_SHORT).show()
        );

        // 排序点击
        binding.tvLatest.setOnClickListener(v ->
            Toast.makeText(getContext(), "按最新排序", Toast.LENGTH_SHORT).show()
        );

        // 筛选点击
        binding.tvAllReviews.setOnClickListener(v ->
            Toast.makeText(getContext(), "全部评论筛选", Toast.LENGTH_SHORT).show()
        );
    }

    private void observeData() {
        viewModel.getComments().observe(getViewLifecycleOwner(), detailComments -> {
            if (detailComments != null) {
                adapter.setReviews(detailComments);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
