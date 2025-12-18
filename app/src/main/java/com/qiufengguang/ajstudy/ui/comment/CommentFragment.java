package com.qiufengguang.ajstudy.ui.comment;

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
import com.qiufengguang.ajstudy.databinding.FragmentCommentBinding;

/**
 * 详情页-评论子页面
 *
 * @author qiufengguang
 * @since 2025/12/10 0:18
 */
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
    }


    private void setupListeners() {
        adapter.setLikeClickListener((position, comment) ->
            adapter.notifyItemChanged(position, comment));
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
