package com.qiufengguang.ajstudy.card.screenshot;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.data.model.ScreenshotCardBean;
import com.qiufengguang.ajstudy.databinding.CardScreenshotBinding;

import java.lang.ref.WeakReference;

/**
 * 截图卡
 *
 * @author qiufengguang
 * @since 2026/3/1 16:48
 */
public class ScreenshotCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 14;

    private WeakReference<RecyclerView> recyclerViewRef;

    private ScreenshotAdapter adapter;

    private ScreenshotDecoration decor;

    private ScreenshotCard() {
    }

    public void setData(ScreenshotCardBean bean) {
        if (adapter == null) {
            adapter = new ScreenshotAdapter(bean.getImages());
        } else {
            adapter.updateData(bean.getImages());
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
        LinearLayoutManager layoutManager = new LinearLayoutManager(
            recyclerView.getContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        );
        recyclerView.setLayoutManager(layoutManager);

        if (decor == null) {
            int spacing = recyclerView.getResources().getDimensionPixelSize(
                R.dimen.activity_horizontal_margin_s);
            decor = new ScreenshotDecoration(spacing);
        }
        recyclerView.removeItemDecoration(decor);
        recyclerView.addItemDecoration(decor);

        // 优化RecyclerView性能
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false); // 避免嵌套滚动冲突

        // 创建横向Adapter
        adapter = new ScreenshotAdapter(null);
        recyclerView.setAdapter(adapter);
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardScreenshotBinding binding = CardScreenshotBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            ScreenshotHolder holder = new ScreenshotHolder(binding);
            setFullSpanInStaggeredPage(holder);
            return holder;
        }
    }

    public static class Builder {
        private RecyclerView recyclerView;

        /**
         * 设置卡片布局控件
         *
         * @param recyclerView RecyclerView
         * @return Builder
         */
        public ScreenshotCard.Builder setRecyclerView(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
            return this;
        }

        public ScreenshotCard create() {
            if (this.recyclerView == null) {
                throw new UnsupportedOperationException(
                    "recyclerView is null, call setRecyclerView first.");
            }
            ScreenshotCard wrapper = new ScreenshotCard();
            wrapper.recyclerViewRef = new WeakReference<>(this.recyclerView);
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

                recyclerViewRef.clear();
            }
            recyclerViewRef = null;
        }
        decor = null;
        if (adapter != null) {
            adapter = null;
        }
    }
}
