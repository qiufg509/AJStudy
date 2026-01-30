package com.qiufengguang.ajstudy.fragment.base;

import androidx.recyclerview.widget.GridLayoutManager;

import com.qiufengguang.ajstudy.R;

/**
 * 格网页基类Fragment
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public abstract class BaseGridFragment extends BaseListFragment {

    @Override
    protected void setupContent() {
        int columnCount = getResources().getInteger(R.integer.ajstudy_column_count);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), columnCount);
//        KnowHowItemDecoration decoration = new KnowHowItemDecoration(requireContext(), columnCount);
//        baseBinding.recyclerContainer.addItemDecoration(decoration);
        baseListAdapter = new BaseListAdapter(getViewLifecycleOwner());
        baseBinding.recyclerContainer.setLayoutManager(layoutManager);
        layoutManager.setSpanSizeLookup(new LookupController(baseListAdapter));
        baseBinding.recyclerContainer.setAdapter(baseListAdapter);
    }
}