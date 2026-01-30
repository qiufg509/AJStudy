package com.qiufengguang.ajstudy.card.largegraphic;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.card.base.GridDecoration;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.databinding.CardLargeGraphicBinding;
import com.qiufengguang.ajstudy.global.Constant;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Map;

/**
 * 大图文卡
 *
 * @author qiufengguang
 * @since 2026/1/24 23:31
 */
public class LargeGraphicCard extends Card {
    private WeakReference<RecyclerView> recyclerViewRef;

    private WeakReference<TextView> titleViewRef;

    private LargeGraphicCardAdapter adapter;

    private int spanCount;

    private int horizontalSpacing;

    private int verticalSpacing;

    private boolean includeEdge;

    private LargeGraphicCardAdapter.OnItemClickListener listener;

    private GridDecoration decor;

    private LargeGraphicCard() {
    }

    public void setData(List<LargeGraphicCardBean> beans, String cardTitle) {
        if (adapter == null) {
            adapter = new LargeGraphicCardAdapter(beans);
        } else {
            adapter.setData(beans);
        }
        if (titleViewRef == null) {
            return;
        }
        TextView textView = titleViewRef.get();
        if (textView == null) {
            return;
        }
        if (TextUtils.isEmpty(cardTitle)) {
            textView.setVisibility(View.GONE);
        } else {
            textView.setVisibility(View.VISIBLE);
            textView.setText(cardTitle);
        }
    }

    public void show() {
        if (recyclerViewRef == null) {
            return;
        }
        RecyclerView recyclerView = recyclerViewRef.get();
        if (recyclerView == null) {
            return;
        }
        GridLayoutManager layoutManager = new GridLayoutManager(recyclerView.getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        if (adapter == null) {
            adapter = new LargeGraphicCardAdapter(null);
        }
        adapter.setOnItemClickListener(listener);
        recyclerView.setAdapter(adapter);
        if (decor == null) {
            decor = new GridDecoration(spanCount,
                horizontalSpacing, verticalSpacing, includeEdge);
        }
        recyclerView.removeItemDecoration(decor);
        recyclerView.addItemDecoration(decor);
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardLargeGraphicBinding binding = CardLargeGraphicBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new LargeGraphicCardHolder(binding);
        }

        @Override
        public Map<Integer, Integer> getSpanSize() {
            return getSpanSizeMap(Constant.Pln.DEF_4);
        }
    }

    public static class Builder {
        private RecyclerView recyclerView;

        private TextView titleView;

        private int spanCount;

        private int horizontalSpacing;

        private int verticalSpacing;

        private int spacing;

        private boolean includeEdge;

        private LargeGraphicCardAdapter.OnItemClickListener listener;

        /**
         * 设置格网卡片内容布局控件
         *
         * @param recyclerView RecyclerView
         * @return Builder
         */
        public LargeGraphicCard.Builder setRecyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return this;
        }

        /**
         * 设置格网卡片标题控件
         *
         * @param titleView TextView
         * @return Builder
         */
        public LargeGraphicCard.Builder setTitleView(TextView titleView) {
            this.titleView = titleView;
            return this;
        }

        /**
         * 设置格网卡片列数
         *
         * @param spanCount 列数
         * @return Builder
         */
        public LargeGraphicCard.Builder setSpanCount(int spanCount) {
            this.spanCount = spanCount;
            return this;
        }

        /**
         * 设置格网卡片水平间距
         *
         * @param horizontalSpacing 水平间距大小
         * @return Builder
         */
        public LargeGraphicCard.Builder setHorizontalSpacing(int horizontalSpacing) {
            this.horizontalSpacing = horizontalSpacing;
            return this;
        }

        /**
         * 设置格网卡片垂直间距
         *
         * @param verticalSpacing 垂直间距大小
         * @return Builder
         */
        public LargeGraphicCard.Builder setVerticalSpacing(int verticalSpacing) {
            this.verticalSpacing = verticalSpacing;
            return this;
        }

        /**
         * 设置格网卡片间距
         *
         * @param spacing 间距大小
         * @return Builder
         */
        public LargeGraphicCard.Builder setSpacing(int spacing) {
            this.spacing = spacing;
            return this;
        }

        /**
         * 设置格网卡片间距是否包含边距
         *
         * @param includeEdge 默认false不包含
         * @return Builder
         */
        public LargeGraphicCard.Builder setIncludeEdge(boolean includeEdge) {
            this.includeEdge = includeEdge;
            return this;
        }

        /**
         * 设置格网卡片item点击事件
         *
         * @param listener GridCardAdapter.OnItemClickListener
         * @return Builder
         */
        public LargeGraphicCard.Builder setListener(
            LargeGraphicCardAdapter.OnItemClickListener listener) {
            this.listener = listener;
            return this;
        }

        public LargeGraphicCard create() {
            if (this.recyclerView == null) {
                throw new UnsupportedOperationException(
                    "recyclerView is null, call setRecyclerView first.");
            }
            LargeGraphicCard wrapper = new LargeGraphicCard();
            wrapper.recyclerViewRef = new WeakReference<>(this.recyclerView);
            wrapper.titleViewRef = new WeakReference<>(this.titleView);
            if (this.spanCount > 0) {
                wrapper.spanCount = this.spanCount;
            } else {
                int column = this.recyclerView.getResources().getInteger(R.integer.ajstudy_column_count);
                wrapper.spanCount = column == Constant.Grid.COLUMN_DEFAULT ? Constant.Pln.DEF_4 :
                    (column == Constant.Grid.COLUMN_8 ? Constant.Pln.DEF_8 : Constant.Pln.DEF_12);
            }
            wrapper.horizontalSpacing = this.horizontalSpacing == 0 && this.spacing != 0
                ? this.spacing : this.horizontalSpacing;
            wrapper.verticalSpacing = this.verticalSpacing == 0 && this.spacing != 0
                ? this.spacing : this.verticalSpacing;
            wrapper.includeEdge = this.includeEdge;
            wrapper.listener = this.listener;
            return wrapper;
        }
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        if (recyclerViewRef != null) {
            RecyclerView recyclerView = recyclerViewRef.get();
            if (recyclerView != null) {
                recyclerView.setAdapter(null);
                recyclerView.setLayoutManager(null);
                recyclerView.removeItemDecoration(decor);
                recyclerViewRef.clear();
            }
            recyclerViewRef = null;
        }
        decor = null;
        if (adapter != null) {
            adapter.setOnItemClickListener(null);
            adapter = null;
        }
    }
}
