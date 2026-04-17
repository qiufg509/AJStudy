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
import java.util.Objects;

/**
 * 格网卡片
 * [性能专家重构]：消除布局震荡，优化重绘效率
 */
public class GridCard extends Card {
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

        if (titleViewRef != null) {
            TextView textView = titleViewRef.get();
            if (textView != null) {
                if (TextUtils.isEmpty(cardTitle)) {
                    if (textView.getVisibility() != View.GONE) textView.setVisibility(View.GONE);
                } else {
                    if (textView.getVisibility() != View.VISIBLE)
                        textView.setVisibility(View.VISIBLE);
                    if (!Objects.equals(textView.getText(), cardTitle)) textView.setText(cardTitle);
                }
            }
        }
    }

    public void show() {
        if (recyclerViewRef == null) return;
        RecyclerView recyclerView = recyclerViewRef.get();
        if (recyclerView == null) return;

        // [性能专家重构点]：检查是否需要初始化，避免重复 setLayoutManager 导致的布局震荡
        if (recyclerView.getLayoutManager() == null) {
            ViewGroup.LayoutParams lp = recyclerView.getLayoutParams();
            lp.width = this.wrapContent ? ViewGroup.LayoutParams.WRAP_CONTENT : ViewGroup.LayoutParams.MATCH_PARENT;
            recyclerView.setLayoutParams(lp);

            GridLayoutManager lm = new GridLayoutManager(recyclerView.getContext(), spanCount);
            recyclerView.setLayoutManager(lm);
            recyclerView.setHasFixedSize(true);

            if (adapter == null) adapter = new GridCardAdapter(null);
            adapter.setOnItemClickListener(listener);
            recyclerView.setAdapter(adapter);

            if (decor == null && spacingBuilder != null) {
                decor = spacingBuilder.build();
            }
            if (decor != null) {
                recyclerView.addItemDecoration(decor);
            }
        } else {
            // 已初始化，仅同步点击监听
            if (adapter != null) adapter.setOnItemClickListener(listener);
        }
    }

    // ... Creator 和 Builder 保持原有逻辑，已在前期优化
    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardGridBinding binding = CardGridBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new GridCardHolder(binding);
        }

        @Override
        public boolean isFullSpanInStaggeredPage() {
            return true;
        }
    }

    public static class Builder {
        private RecyclerView recyclerView;
        private TextView titleView;
        private int spanCount;
        private GridDecoration.Builder spacingBuilder;
        private boolean wrapContent;
        private OnItemClickListener<GridCardBean> listener;

        public GridCard.Builder setRecyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return this;
        }

        public GridCard.Builder setTitleView(TextView titleView) {
            this.titleView = titleView;
            return this;
        }

        public GridCard.Builder setSpanCount(int spanCount) {
            this.spanCount = spanCount;
            return this;
        }

        public GridCard.Builder setSpacingBuilder(GridDecoration.Builder builder) {
            this.spacingBuilder = builder;
            return this;
        }

        public GridCard.Builder setWrapContent(boolean wrapContent) {
            this.wrapContent = wrapContent;
            return this;
        }

        public GridCard.Builder setListener(OnItemClickListener<GridCardBean> listener) {
            this.listener = listener;
            return this;
        }

        public GridCard create() {
            if (this.recyclerView == null)
                throw new UnsupportedOperationException("recyclerView is null");
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

    public void release() {
        if (recyclerViewRef != null) {
            RecyclerView recyclerView = recyclerViewRef.get();
            if (recyclerView != null) {
                recyclerView.setAdapter(null);
                recyclerView.setLayoutManager(null);
                if (decor != null) recyclerView.removeItemDecoration(decor);
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