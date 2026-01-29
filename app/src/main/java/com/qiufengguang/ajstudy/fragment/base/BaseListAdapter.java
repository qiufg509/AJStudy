package com.qiufengguang.ajstudy.fragment.base;

import android.text.TextUtils;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.qiufengguang.ajstudy.card.banner.BannerViewHolder;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.ViewHolderFactory;
import com.qiufengguang.ajstudy.data.BannerBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 首页适配器
 *
 * @author qiufengguang
 * @since 2026/1/19 15:26
 */
public class BaseListAdapter extends RecyclerView.Adapter<BaseViewHolder<?>> {

    private final LifecycleOwner lifecycleOwner;

    private List<LayoutData<?>> dataList;

    /**
     * 存储所有监听生命周期的卡片的位置
     */
    private final List<Integer> lifecycleCardPos = new ArrayList<>();

    /**
     * 存储所有需要监听 onResume 和 onPause 方法的 ViewHolder 引用
     */
    private final Set<WeakReference<BaseViewHolder<?>>> lifecycleHolderRefs = new HashSet<>();

    /**
     * 存储所有ViewHolder的引用（用于资源清理）
     */
    private final Set<WeakReference<BaseViewHolder<?>>> viewHolderRefs = new HashSet<>();

    public BaseListAdapter(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    /**
     * 设置数据
     *
     * @param dataList 页面数据
     */
    public void setData(List<LayoutData<?>> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            this.dataList = dataList;
            pauseAllCards();
            notifyItemRangeRemoved(0, getItemCount());
            return;
        }
        if (this.dataList == null || this.dataList.isEmpty()) {
            this.dataList = dataList;
            findAllLifecycleCardPositions();
            notifyItemRangeInserted(0, getItemCount());
        } else {
            this.dataList = dataList;
            findAllLifecycleCardPositions();
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    /**
     * 加载更多数据
     *
     * @param dataList 页面数据
     */
    public void addData(List<LayoutData<?>> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }
        int positionStart;
        int itemCount = dataList.size();
        if (this.dataList == null) {
            positionStart = 0;
            this.dataList = dataList;
        } else {
            positionStart = this.dataList.size();
            this.dataList.addAll(dataList);
        }
        notifyItemRangeInserted(positionStart, itemCount);
    }

    /**
     * 获取ViewType，根据position判断
     */
    @Override
    public int getItemViewType(int position) {
        LayoutData<?> layoutData = dataList.get(position);
        if (layoutData == null) {
            return -1;
        }
        return layoutData.getLayoutId();
    }

    @Override
    public int getItemCount() {
        if (dataList == null || dataList.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (int index = 0, sum = dataList.size(); index < sum; index++) {
            LayoutData<?> layoutData = dataList.get(index);
            if (layoutData == null || layoutData.getData() == null) {
                continue;
            }
            count++;
        }
        return count;
    }

    @NonNull
    @Override
    public BaseViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BaseViewHolder<? extends ViewBinding> viewHolder = ViewHolderFactory.createViewHolder(parent, viewType, this.lifecycleOwner);
        if (viewHolder.isObserveResumePause) {
            lifecycleHolderRefs.add(new WeakReference<>(viewHolder));
        }
        viewHolderRefs.add(new WeakReference<>(viewHolder));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<?> holder, int position) {
        LayoutData<?> layoutData = dataList.get(position);
        if (layoutData == null) {
            return;
        }
        Object data = layoutData.getData();
        if (data == null) {
            return;
        }
        String layoutName = layoutData.getLayoutName();
        if (holder instanceof BannerViewHolder
            && TextUtils.equals(layoutName, BannerBean.LAYOUT_NAME)) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            bannerViewHolder.bind(layoutData, this.lifecycleOwner);
        } else {
            holder.bind(layoutData);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<?> holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            // 如果没有payload，执行完整的绑定
            super.onBindViewHolder(holder, position, payloads);
        } else {
            // 根据payload的内容，只更新特定的视图
            for (Object payload : payloads) {
                if (payload instanceof LayoutData) {
                    holder.update((LayoutData<?>) payload);
                }
            }
        }
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BaseViewHolder<?> holder) {
        super.onViewAttachedToWindow(holder);
        holder.onViewAttachedToWindow();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BaseViewHolder<?> holder) {
        super.onViewDetachedFromWindow(holder);
        holder.onViewDetachedFromWindow();
    }

    @Override
    public void onViewRecycled(@NonNull BaseViewHolder<?> holder) {
        super.onViewRecycled(holder);
        // 当 ViewHolder 被回收时，清理其资源
        holder.cleanup();
    }

    /**
     * 查找所有监听生命周期的卡片的位置
     */
    private void findAllLifecycleCardPositions() {
        lifecycleCardPos.clear();
        for (int index = 0; index < dataList.size(); index++) {
            LayoutData<?> layoutData = dataList.get(index);
            if (layoutData == null || layoutData.getData() == null) {
                continue;
            }
            if (TextUtils.equals(layoutData.getLayoutName(), BannerBean.LAYOUT_NAME)) {
                lifecycleCardPos.add(index);
            }
        }
    }

    /**
     * 激活可见范围内所有的监听生命周期的卡片，不可见则暂停
     *
     * @param recyclerView 页面列表RecyclerView
     */
    public void activeCardsIfVisible(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() == null) {
            return;
        }

        // 获取当前可见位置范围
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisible = layoutManager.findFirstVisibleItemPosition();
        int lastVisible = layoutManager.findLastVisibleItemPosition();

        // 遍历所有lifecycleCard位置，恢复可见的lifecycleCard
        for (int cardIndex : lifecycleCardPos) {
            if (cardIndex < firstVisible || cardIndex > lastVisible) {
                continue;
            }
            for (WeakReference<BaseViewHolder<?>> ref : lifecycleHolderRefs) {
                BaseViewHolder<?> holder = ref.get();
                if (holder == null) {
                    continue;
                }
                if (holder.getBindingAdapterPosition() == cardIndex) {
                    holder.onResume();
                } else {
                    holder.onPause();
                }
            }
        }
    }

    /**
     * 暂停所有监听生命周期的卡片
     */
    public void pauseAllCards() {
        for (WeakReference<BaseViewHolder<?>> ref : lifecycleHolderRefs) {
            BaseViewHolder<?> holder = ref.get();
            if (holder != null) {
                holder.onPause();
            }
        }
    }

    /**
     * 清理所有 ViewHolder 的资源
     */
    public void releaseAllResources() {
        // 清理所有BannerViewHolder引用
        lifecycleHolderRefs.clear();
        lifecycleCardPos.clear();

        // 清理所有 BaseViewHolder
        for (WeakReference<BaseViewHolder<?>> ref : viewHolderRefs) {
            BaseViewHolder<?> holder = ref.get();
            if (holder != null) {
                holder.cleanup();
            }
        }
        viewHolderRefs.clear();

        // 清理其他资源
        dataList = null;
    }
}