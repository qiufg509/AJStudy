package com.qiufengguang.ajstudy.ui.dashboard;

import android.view.LayoutInflater;

import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.FragmentDashboardBinding;
import com.qiufengguang.ajstudy.ui.base.BaseFragment;

public class DashboardFragment extends BaseFragment {
    private FragmentDashboardBinding binding;

    private DashboardViewModel viewModel;

    private DashboardAdapter adapter;

    @Override
    protected void setPageBackground() {
        baseBinding.backgroundImage.setBackgroundResource(R.drawable.dashboard_page_bg);
    }

    @Override
    protected boolean isDarkBackgroundImage() {
        return false;
    }

    @Override
    protected void setupContent() {
        baseBinding.toolbar.setTitle("仪表盘");
        binding = FragmentDashboardBinding.inflate(
            LayoutInflater.from(requireContext()),
            baseBinding.contentContainer,
            true
        );
        SavedStateViewModelFactory factory = new SavedStateViewModelFactory(
            requireActivity().getApplication(), this);
        // 看起来是"新建"，但实际上获取的是同一个实例
        viewModel = new ViewModelProvider(this, factory)
            .get(DashboardViewModel.class);

        adjustColumn();

        // 绑定数据
        viewModel.getLiveData().observe(getViewLifecycleOwner(), list -> adapter.setData(list));
    }

    private void adjustColumn() {
        int columnCount = getResources().getInteger(R.integer.ajstudy_column_count);
        RecyclerView recyclerView = binding.getRoot();
        switch (columnCount) {
            case Constant.Grid.column_8:
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
                break;
            case Constant.Grid.column_12:
                recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
                break;
            default:
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                break;
        }
        adapter = new DashboardAdapter(viewModel.getLiveData().getValue());
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}