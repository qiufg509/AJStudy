package com.qiufengguang.ajstudy.fragment.base;

import android.view.ViewGroup;

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
        // 补充左右间距
        ViewGroup.LayoutParams layoutParams = baseBinding.recyclerContainer.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) layoutParams;
            int spacing = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_m);
            params.setMargins(spacing, 0, spacing, 0);
        }

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