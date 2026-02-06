package com.qiufengguang.ajstudy.card.largegraphicgrid;

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
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.databinding.CardLargeGraphicBinding;
import com.qiufengguang.ajstudy.global.Constant;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 大图文格网卡
 * 包含多个大图文卡，内部处理栅格适配
 *
 * @author qiufengguang
 * @since 2026/1/24 23:31
 */
public class LargeGraphicGridCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 4;

    private WeakReference<RecyclerView> recyclerViewRef;

    private WeakReference<TextView> titleViewRef;

    private LargeGraphicGridCardAdapter adapter;

    private int spanCount;

    private GridDecoration.Builder spacingBuilder;

    private OnItemClickListener<LargeGraphicCardBean> listener;

    private GridDecoration decor;

    private LargeGraphicGridCard() {
    }

    public void setData(List<LargeGraphicCardBean> beans, String cardTitle) {
        if (adapter == null) {
            adapter = new LargeGraphicGridCardAdapter(beans);
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
            adapter = new LargeGraphicGridCardAdapter(null);
        }
        adapter.setOnItemClickListener(listener);
        recyclerView.setAdapter(adapter);
        if (spacingBuilder != null) {
            if (decor == null) {
                decor = spacingBuilder.build();
            }
            recyclerView.removeItemDecoration(decor);
            recyclerView.addItemDecoration(decor);
        }
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardLargeGraphicBinding binding = CardLargeGraphicBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new LargeGraphicGridCardHolder(binding);
        }
    }

    public static class Builder {
        private RecyclerView recyclerView;

        private TextView titleView;

        private int spanCount;

        private GridDecoration.Builder spacingBuilder;

        private OnItemClickListener<LargeGraphicCardBean> listener;

        /**
         * 设置卡片内容布局控件
         *
         * @param recyclerView RecyclerView
         * @return Builder
         */
        public LargeGraphicGridCard.Builder setRecyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return this;
        }

        /**
         * 设置卡片标题控件
         *
         * @param titleView TextView
         * @return Builder
         */
        public LargeGraphicGridCard.Builder setTitleView(TextView titleView) {
            this.titleView = titleView;
            return this;
        }

        /**
         * 设置卡片列数
         *
         * @param spanCount 列数
         * @return Builder
         */
        public LargeGraphicGridCard.Builder setSpanCount(int spanCount) {
            this.spanCount = spanCount;
            return this;
        }

        /**
         * 设置间距Builder
         *
         * @param spacingBuilder GridDecoration.Builder
         * @return Builder
         */
        public LargeGraphicGridCard.Builder setSpacingBuilder(GridDecoration.Builder spacingBuilder) {
            this.spacingBuilder = spacingBuilder;
            return this;
        }

        /**
         * 设置卡片item点击事件
         *
         * @param listener {@link OnItemClickListener}
         * @return Builder
         */
        public LargeGraphicGridCard.Builder setListener(
            OnItemClickListener<LargeGraphicCardBean> listener) {
            this.listener = listener;
            return this;
        }

        public LargeGraphicGridCard create() {
            if (this.recyclerView == null) {
                throw new UnsupportedOperationException(
                    "recyclerView is null, call setRecyclerView first.");
            }
            LargeGraphicGridCard wrapper = new LargeGraphicGridCard();
            wrapper.recyclerViewRef = new WeakReference<>(this.recyclerView);
            wrapper.titleViewRef = new WeakReference<>(this.titleView);
            if (this.spanCount > 0) {
                wrapper.spanCount = this.spanCount;
            } else {
                int column = this.recyclerView.getResources().getInteger(R.integer.ajstudy_column_count);
                wrapper.spanCount = column == Constant.Grid.COLUMN_DEFAULT ? Constant.Pln.DEF_4 :
                    (column == Constant.Grid.COLUMN_8 ? Constant.Pln.DEF_8 : Constant.Pln.DEF_12);
            }
            if (this.spacingBuilder != null) {
                wrapper.spacingBuilder = spacingBuilder;
                wrapper.spacingBuilder.setSpanCount(wrapper.spanCount);
            }
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
