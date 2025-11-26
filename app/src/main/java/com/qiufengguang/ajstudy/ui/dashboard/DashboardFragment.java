package com.qiufengguang.ajstudy.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.Constant;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.FragmentDashboardBinding;

public class DashboardFragment extends Fragment {
    private static final String TAG = "DashboardFragment";

    private FragmentDashboardBinding binding;

    private DashboardViewModel dashboardVm;

    private DashboardAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
        Bundle savedInstanceState) {
        if (isAdded() && getActivity() != null) {
            SavedStateViewModelFactory factory = new SavedStateViewModelFactory(
                getActivity().getApplication(), this);
            // 看起来是"新建"，但实际上获取的是同一个实例
            dashboardVm = new ViewModelProvider(this, factory).get(DashboardViewModel.class);
        }

        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (dashboardVm == null) {
            // 初始化ViewModel，使用SavedStateViewModelFactory
            SavedStateViewModelFactory factory = new SavedStateViewModelFactory(
                requireActivity().getApplication(), this);
            dashboardVm = new ViewModelProvider(this, factory).get(DashboardViewModel.class);
        }
        RecyclerView recyclerView = binding.getRoot();
        int columnCount = getResources().getInteger(R.integer.ajstudy_column_count);
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
        adapter = new DashboardAdapter(dashboardVm.getLiveData().getValue());
        recyclerView.setAdapter(adapter);

        // 绑定数据
        dashboardVm.getLiveData().observe(getViewLifecycleOwner(), list -> {
            adapter.setData(list);
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}