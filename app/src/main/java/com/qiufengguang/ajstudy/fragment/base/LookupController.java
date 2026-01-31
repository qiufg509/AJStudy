package com.qiufengguang.ajstudy.fragment.base;

import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.global.GlobalApp;

import java.util.Map;

/**
 * 格网页item占用数量控制类
 *
 * @author qiufengguang
 * @since 2026/1/30 16:33
 */
public class LookupController extends GridLayoutManager.SpanSizeLookup {
    private static final int DEFAULT_COLUMNS = 1;

    private int columnCount = Constant.Grid.COLUMN_DEFAULT;

    private final RecyclerView.Adapter<BaseViewHolder<?>> adapter;

    public LookupController(RecyclerView.Adapter<BaseViewHolder<?>> adapter) {
        Context context = GlobalApp.getContext();
        if (context != null) {
            columnCount = context.getResources().getInteger(R.integer.ajstudy_column_count);
        }
        this.adapter = adapter;
    }

    @Override
    public int getSpanSize(int position) {
        if (adapter == null) {
            return DEFAULT_COLUMNS;
        }
        int itemViewType = adapter.getItemViewType(position);
        CardCreator creator = Card.getCreator(itemViewType);
        if (creator == null) {
            return DEFAULT_COLUMNS;
        }
        Map<Integer, Integer> map = creator.getSpanSize();
        if (map == null || map.isEmpty()) {
            return DEFAULT_COLUMNS;
        }
        Integer value = map.get(columnCount);
        if (value == null || value <= 0) {
            return DEFAULT_COLUMNS;
        }
        return Math.max(1, columnCount / value);
    }
}
