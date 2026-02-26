package com.qiufengguang.ajstudy.card.grid;

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
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.model.GridCardBean;
import com.qiufengguang.ajstudy.databinding.CardGridBinding;
import com.qiufengguang.ajstudy.global.Constant;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 格网卡片
 *
 * @author qiufengguang
 * @since 2025/12/28 18:50
 */
public class GridCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 2;

    private WeakReference<RecyclerView> recyclerViewRef;

    private WeakReference<TextView> titleViewRef;

    private GridCardAdapter adapter;

    private int spanCount;

    private GridDecoration.Builder spacingBuilder;

    private boolean wrapContent;

    private GridDecoration decor;

    private OnItemClickListener<GridCardBean> listener;

    private GridCard() {
    }

    public void setData(List<GridCardBean> beans, String cardTitle) {
        if (adapter == null) {
            adapter = new GridCardAdapter(beans);
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
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.width = this.wrapContent ? ViewGroup.LayoutParams.WRAP_CONTENT
            : ViewGroup.LayoutParams.MATCH_PARENT;
        recyclerView.setLayoutParams(layoutParams);

        GridLayoutManager layoutManager = new GridLayoutManager(recyclerView.getContext(), spanCount);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        if (adapter == null) {
            adapter = new GridCardAdapter(null);
        }
        adapter.setOnItemClickListener(listener);
        recyclerView.setAdapter(adapter);
        if (decor == null) {
            decor = spacingBuilder.build();
        }
        recyclerView.removeItemDecoration(decor);
        recyclerView.addItemDecoration(decor);
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardGridBinding binding = CardGridBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            GridViewHolder holder = new GridViewHolder(binding);
            setFullSpanInStaggeredPage(holder);
            return holder;
        }
    }

    public static class Builder {
        private RecyclerView recyclerView;

        private TextView titleView;

        private int spanCount;

        private GridDecoration.Builder spacingBuilder;

        private boolean wrapContent;

        private OnItemClickListener<GridCardBean> listener;

        /**
         * 设置格网卡片布局控件
         *
         * @param recyclerView RecyclerView
         * @return Builder
         */
        public GridCard.Builder setRecyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return this;
        }

        /**
         * 设置卡片标题控件
         *
         * @param titleView TextView
         * @return Builder
         */
        public GridCard.Builder setTitleView(TextView titleView) {
            this.titleView = titleView;
            return this;
        }

        /**
         * 设置格网卡片列数
         *
         * @param spanCount 列数
         * @return Builder
         */
        public GridCard.Builder setSpanCount(int spanCount) {
            this.spanCount = spanCount;
            return this;
        }

        /**
         * 设置间距Builder
         *
         * @param builder GridDecoration.Builder
         * @return Builder
         */
        public GridCard.Builder setSpacingBuilder(GridDecoration.Builder builder) {
            this.spacingBuilder = builder;
            return this;
        }

        /**
         * 设置格网卡片间距是否包含边距
         * 默认false撑满窗口宽度
         *
         * @param wrapContent 内容是否Wrap Content
         * @return Builder
         */
        public GridCard.Builder setWrapContent(boolean wrapContent) {
            this.wrapContent = wrapContent;
            return this;
        }

        /**
         * 设置格网卡片item点击事件
         *
         * @param listener {@link OnItemClickListener}
         * @return Builder
         */
        public GridCard.Builder setListener(OnItemClickListener<GridCardBean> listener) {
            this.listener = listener;
            return this;
        }

        public GridCard create() {
            if (this.recyclerView == null) {
                throw new UnsupportedOperationException(
                    "recyclerView is null, call setRecyclerView first.");
            }
            GridCard wrapper = new GridCard();
            wrapper.recyclerViewRef = new WeakReference<>(this.recyclerView);
            wrapper.titleViewRef = new WeakReference<>(this.titleView);
            if (this.spanCount > 0) {
                wrapper.spanCount = this.spanCount;
            } else {
                int column = this.recyclerView.getResources().getInteger(R.integer.ajstudy_column_count);
                wrapper.spanCount = column == Constant.Grid.COLUMN_DEFAULT ? Constant.Pln.GRID_4 :
                    (column == Constant.Grid.COLUMN_8 ? Constant.Pln.GRID_8 : Constant.Pln.GRID_12);
            }
            if (this.spacingBuilder != null) {
                wrapper.spacingBuilder = spacingBuilder;
                wrapper.spacingBuilder.setSpanCount(wrapper.spanCount);
            }
            wrapper.wrapContent = this.wrapContent;
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
