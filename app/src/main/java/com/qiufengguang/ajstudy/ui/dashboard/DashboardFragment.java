package com.qiufengguang.ajstudy.ui.dashboard;

import android.view.LayoutInflater;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.divider.MaterialDividerItemDecoration;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.FragmentDashboardBinding;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.ui.base.BaseFragment;

/**
 * 列表页
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
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
                recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 2));
                break;
            case Constant.Grid.column_12:
                recyclerView.setLayoutManager(new GridLayoutManager(requireContext(), 3));
                break;
            default:
                recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
                break;
        }
        adapter = new DashboardAdapter(viewModel.getLiveData().getValue());
        recyclerView.setAdapter(adapter);
        MaterialDividerItemDecoration divider = new MaterialDividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL);
        divider.setDividerThickness(1);
        int color = ContextCompat.getColor(requireContext(), R.color.ajstudy_color_divider_horizontal);
        divider.setDividerColor(color);
        divider.setLastItemDecorated(false);
        recyclerView.addItemDecoration(divider);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}