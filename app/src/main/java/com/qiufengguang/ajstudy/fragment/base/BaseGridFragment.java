package com.qiufengguang.ajstudy.fragment.base;

import androidx.recyclerview.widget.GridLayoutManager;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.fragment.knowhow.KnowHowItemDecoration;
import com.qiufengguang.ajstudy.global.Constant;

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
        int perLineNumber;
        switch (columnCount) {
            case Constant.Grid.COLUMN_8:
                perLineNumber = Constant.Pln.DEF_8;
                break;
            case Constant.Grid.COLUMN_12:
                perLineNumber = Constant.Pln.DEF_12;
                break;
            default:
                perLineNumber = Constant.Pln.DEF_4;
                break;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), perLineNumber);
        KnowHowItemDecoration decoration = new KnowHowItemDecoration(requireContext(), perLineNumber);
        baseBinding.recyclerContainer.addItemDecoration(decoration);
        baseBinding.recyclerContainer.setLayoutManager(layoutManager);
        baseListAdapter = new BaseListAdapter(getViewLifecycleOwner());
        baseBinding.recyclerContainer.setAdapter(baseListAdapter);
    }
}