package com.qiufengguang.ajstudy.fragment.knowhow;

import android.view.LayoutInflater;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.KnowHowBean;
import com.qiufengguang.ajstudy.databinding.FragmentKnowHowBinding;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.fragment.base.BaseFragment;
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

        // 绑定数据
        viewModel.getLiveData().observe(getViewLifecycleOwner(), list ->
            adapter.setData(viewModel.getPageData(0)));
    }

    private void adjustColumn() {
        RecyclerView recyclerView = binding.getRoot();
        MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration(requireContext(),
            LinearLayoutManager.VERTICAL);
        divider.setDividerThickness(1);
        int color = ContextCompat.getColor(requireContext(), R.color.ajstudy_color_divider);
        divider.setDividerColor(color);
        divider.setLastItemDecorated(false);
        recyclerView.addItemDecoration(divider);
        int columnCount = getResources().getInteger(R.integer.ajstudy_column_count);
        LinearLayoutManager layoutManager;
        switch (columnCount) {
            case Constant.Grid.column_8:
                layoutManager = new GridLayoutManager(requireContext(), 2);
                break;
            case Constant.Grid.column_12:
                layoutManager = new GridLayoutManager(requireContext(), 3);
                break;
            default:
                layoutManager = new LinearLayoutManager(requireContext());
                break;
        }
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