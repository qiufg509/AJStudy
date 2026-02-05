package com.qiufengguang.ajstudy.card.setting;

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
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.SettingCardBean;
import com.qiufengguang.ajstudy.databinding.CardSettingBinding;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.card.base.GridDecoration;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 设置卡片
 *
 * @author qiufengguang
 * @since 2026/1/31 12:50
 */
public class SettingCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 9;

    private WeakReference<RecyclerView> recyclerViewRef;

    private WeakReference<TextView> titleViewRef;

    private SettingCardAdapter adapter;

    private int spanCount;

    private int horizontalSpacing;

    private int verticalSpacing;

    private int startSpacing;

    private int endSpacing;

    private OnItemClickListener<SettingCardBean> listener;

    private GridDecoration decor;

    private SettingCard() {
    }

    public void setData(List<SettingCardBean> beans, String cardTitle) {
        if (adapter == null) {
            adapter = new SettingCardAdapter(beans);
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
            adapter = new SettingCardAdapter(null);
        }
        adapter.setSpanCount(spanCount);
        adapter.setOnItemClickListener(listener);
        recyclerView.setAdapter(adapter);
        if (decor == null) {
            decor = new GridDecoration.Builder(spanCount)
                .startSpacing(startSpacing)
                .endSpacing(endSpacing)
                .horizontalSpacing(horizontalSpacing)
                .verticalSpacing(verticalSpacing)
                .build();
        }
        recyclerView.removeItemDecoration(decor);
        recyclerView.addItemDecoration(decor);
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardSettingBinding binding = CardSettingBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new SettingCardHolder(binding);
        }
    }

    public static class Builder {
        private RecyclerView recyclerView;

        private TextView titleView;

        private int spanCount;

        private int horizontalSpacing;

        private int verticalSpacing;

        private int startSpacing;

        private int endSpacing;

        private int spacing;

        private OnItemClickListener<SettingCardBean> listener;

        /**
         * 设置卡片内容布局控件
         *
         * @param recyclerView RecyclerView
         * @return Builder
         */
        public SettingCard.Builder setRecyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return this;
        }

        /**
         * 设置卡片标题控件
         *
         * @param titleView TextView
         * @return Builder
         */
        public SettingCard.Builder setTitleView(TextView titleView) {
            this.titleView = titleView;
            return this;
        }

        /**
         * 设置卡片列数
         *
         * @param spanCount 列数
         * @return Builder
         */
        public SettingCard.Builder setSpanCount(int spanCount) {
            this.spanCount = spanCount;
            return this;
        }

        /**
         * 设置卡片水平间距
         *
         * @param horizontalSpacing 水平间距大小
         * @return Builder
         */
        public SettingCard.Builder setHorizontalSpacing(int horizontalSpacing) {
            this.horizontalSpacing = horizontalSpacing;
            return this;
        }

        /**
         * 设置格片垂直间距
         *
         * @param verticalSpacing 垂直间距大小
         * @return Builder
         */
        public SettingCard.Builder setVerticalSpacing(int verticalSpacing) {
            this.verticalSpacing = verticalSpacing;
            return this;
        }

        /**
         * 设置左侧间距
         *
         * @param startSpacing 左侧间距
         * @return Builder
         */
        public SettingCard.Builder setStartSpacing(int startSpacing) {
            this.startSpacing = startSpacing;
            return this;
        }

        /**
         * 设置右侧间距
         *
         * @param endSpacing 右侧间距
         * @return Builder
         */
        public SettingCard.Builder setEndSpacing(int endSpacing) {
            this.endSpacing = endSpacing;
            return this;
        }

        /**
         * 设置卡片间距
         *
         * @param spacing 间距大小
         * @return Builder
         */
        public SettingCard.Builder setSpacing(int spacing) {
            this.spacing = spacing;
            return this;
        }

        /**
         * 设置卡片item点击事件
         *
         * @param listener {@link OnItemClickListener}
         * @return Builder
         */
        public SettingCard.Builder setListener(
            OnItemClickListener<SettingCardBean> listener) {
            this.listener = listener;
            return this;
        }

        public SettingCard create() {
            if (this.recyclerView == null) {
                throw new UnsupportedOperationException(
                    "recyclerView is null, call setRecyclerView first.");
            }
            SettingCard wrapper = new SettingCard();
            wrapper.recyclerViewRef = new WeakReference<>(this.recyclerView);
            wrapper.titleViewRef = new WeakReference<>(this.titleView);
            if (this.spanCount > 0) {
                wrapper.spanCount = this.spanCount;
            } else {
                int column = this.recyclerView.getResources().getInteger(R.integer.ajstudy_column_count);
                wrapper.spanCount = column == Constant.Grid.COLUMN_DEFAULT
                    ? Constant.Pln.DEF_4 : Constant.Pln.DEF_8;
            }
            wrapper.horizontalSpacing = this.horizontalSpacing == 0 && this.spacing != 0
                ? this.spacing : this.horizontalSpacing;
            wrapper.verticalSpacing = this.verticalSpacing == 0 && this.spacing != 0
                ? this.spacing : this.verticalSpacing;
            wrapper.startSpacing = this.startSpacing;
            wrapper.endSpacing = this.endSpacing;
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
