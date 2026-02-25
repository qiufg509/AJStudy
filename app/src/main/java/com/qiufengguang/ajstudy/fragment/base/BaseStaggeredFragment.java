package com.qiufengguang.ajstudy.fragment.base;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.global.Constant;

/**
 * 瀑布流页基类Fragment
 *
 * @author qiufengguang
 * @since 2026/2/25 23:12
 */
public abstract class BaseStaggeredFragment extends BaseListFragment {

    @Override
    protected void setupContent() {
        int columnCount = getResources().getInteger(R.integer.ajstudy_column_count);
        int spanCount;
        switch (columnCount) {
            case Constant.Grid.COLUMN_8:
                spanCount = Constant.Pln.GRAPHIC_M_8;
                break;
            case Constant.Grid.COLUMN_12:
                spanCount = Constant.Pln.GRAPHIC_M_12;
                break;
            default:
                spanCount = Constant.Pln.GRAPHIC_M_4;
                break;
        }
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL);
        baseListAdapter = new BaseListAdapter(getViewLifecycleOwner());
        baseBinding.recyclerContainer.setLayoutManager(layoutManager);
        baseBinding.recyclerContainer.setItemAnimator(new DefaultItemAnimator());
        baseBinding.recyclerContainer.setAdapter(baseListAdapter);
    }
}