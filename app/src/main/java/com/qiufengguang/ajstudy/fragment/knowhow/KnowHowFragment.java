package com.qiufengguang.ajstudy.fragment.knowhow;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;

import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.KnowHowBean;
import com.qiufengguang.ajstudy.databinding.FragmentKnowHowBinding;
import com.qiufengguang.ajstudy.fragment.base.BaseFragment;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.view.EndlessRecyclerViewScrollListener;

import java.util.List;

/**
 * 知识列表页
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class KnowHowFragment extends BaseFragment {
    private FragmentKnowHowBinding binding;

    private KnowHowViewModel viewModel;

    private KnowHowAdapter adapter;

    @Override
    protected boolean isDarkBackgroundImage() {
        return false;
    }

    @Override
    protected String getTitle() {
        return "应用列表";
    }

    @Override
    protected void setupContent() {
        binding = FragmentKnowHowBinding.inflate(
            LayoutInflater.from(requireContext()),
            baseBinding.contentContainer,
            true
        );
        SavedStateViewModelFactory factory = new SavedStateViewModelFactory(
            requireActivity().getApplication(), this);
        // 看起来是"新建"，但实际上获取的是同一个实例
        viewModel = new ViewModelProvider(this, factory)
            .get(KnowHowViewModel.class);

        adjustColumn();

        Bundle args = getArguments();
        String course = "";
        if (args != null) {
            course = args.getString("Course");
        }
        viewModel.initData(TextUtils.isEmpty(course) ? Constant.Data.DOCUMENT_STUDY_DIR : course);
        // 绑定数据
        viewModel.getLiveData().observe(getViewLifecycleOwner(), list ->
            adapter.setData(viewModel.getPageData(0)));
    }

    private void adjustColumn() {
        RecyclerView recyclerView = binding.layoutKnowHow;
        int columnCount = getResources().getInteger(R.integer.ajstudy_column_count);
        LinearLayoutManager layoutManager;
        int perLineNumber;
        switch (columnCount) {
            case Constant.Grid.COLUMN_8:
                perLineNumber = Constant.Pln.DEF_8;
                layoutManager = new GridLayoutManager(requireContext(), perLineNumber);
                break;
            case Constant.Grid.COLUMN_12:
                perLineNumber = Constant.Pln.DEF_12;
                layoutManager = new GridLayoutManager(requireContext(), perLineNumber);
                break;
            default:
                perLineNumber = Constant.Pln.DEF_4;
                layoutManager = new LinearLayoutManager(requireContext());
                break;
        }
        KnowHowItemDecoration decoration = new KnowHowItemDecoration(requireContext(), perLineNumber);
        recyclerView.addItemDecoration(decoration);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new KnowHowAdapter(viewModel.getLiveData().getValue());
        recyclerView.setAdapter(adapter);
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(layoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                List<KnowHowBean> data = viewModel.getPageData(page);
                adapter.addData(data);
            }
        };

        recyclerView.addOnScrollListener(scrollListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}