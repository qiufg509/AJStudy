package com.qiufengguang.ajstudy.fragment.base;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.viewbinding.ViewBinding;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.card.base.ViewHolderFactory;
import com.qiufengguang.ajstudy.card.empty.EmptyCard;
import com.qiufengguang.ajstudy.card.state.StateCard;
import com.qiufengguang.ajstudy.data.base.BaseCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * 页面适配器（使用 ListAdapter 实现高效增量更新）
 * [稳定性优化]：引入错误隔离机制，利用 EmptyCard 实现静默降级
 *
 * @author qiufengguang
 * @since 2026/1/19 15:26
 */
public class BaseListAdapter extends ListAdapter<LayoutData<?>, BaseViewHolder<?>> {
    private static final String TAG = "BaseListAdapter";

    /**
     * 页面数据差异化更新回调
     */
    private static final DiffUtil.ItemCallback<LayoutData<?>> DIFF_CALLBACK = new DiffItemCallback();

    private final LifecycleOwner lifecycleOwner;

    private OnItemClickListener<BaseCardBean> listener;

    /**
     * 存储所有需要监听 onResume 和 onPause 方法的 ViewHolder 弱引用
     */
    private final Set<WeakReference<BaseViewHolder<?>>> lifecycleHolderRefs = new HashSet<>();

    /**
     * 存储所有ViewHolder的引用（用于资源清理）
     */
    private final Set<WeakReference<BaseViewHolder<?>>> viewHolderRefs = new HashSet<>();

    public BaseListAdapter(LifecycleOwner lifecycleOwner) {
        super(DIFF_CALLBACK);
        this.lifecycleOwner = lifecycleOwner;
    }

    /**
     * 设置数据
     *
     * @param dataList 页面数据
     */
    public void setData(List<LayoutData<?>> dataList) {
        // 暂停当前所有生命周期卡片（旧 holder 将很快被回收）
        pauseAllCards();
        // 过滤掉 null 元素，避免后续处理异常
        List<LayoutData<?>> filtered = filterNull(dataList);
        submitList(filtered);
    }

    /**
     * 加载更多数据
     *
     * @param newData 页面数据
     */
    public void addData(List<LayoutData<?>> newData) {
        if (newData == null || newData.isEmpty()) {
            return;
        }
        List<LayoutData<?>> filtered = filterNull(newData);
        List<LayoutData<?>> currentList = new ArrayList<>(getCurrentList());
        currentList.addAll(filtered);
        submitList(currentList);
    }

    /**
     * 过滤掉列表中的 null 元素
     */
    private List<LayoutData<?>> filterNull(List<LayoutData<?>> list) {
        if (list == null) {
            return null;
        }
        List<LayoutData<?>> result = new ArrayList<>(list.size());
        for (LayoutData<?> item : list) {
            if (item != null) {
                result.add(item);
            }
        }
        return result;
    }

    public void setListener(OnItemClickListener<BaseCardBean> listener) {
        this.listener = listener;
    }

    /**
     * 获取ViewType，根据position判断
     */
    @Override
    public int getItemViewType(int position) {
        LayoutData<?> layoutData = getItem(position);
        return layoutData != null ? layoutData.getLayoutId() : StateCard.LAYOUT_ID;
    }

    @NonNull
    @Override
    public BaseViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        try {
            BaseViewHolder<? extends ViewBinding> viewHolder = ViewHolderFactory.createViewHolder(parent, viewType, this.lifecycleOwner);
            if (viewHolder.isObserveResumePause) {
                lifecycleHolderRefs.add(new WeakReference<>(viewHolder));
            }
            viewHolderRefs.add(new WeakReference<>(viewHolder));
            return viewHolder;
        } catch (Throwable e) {
            Log.e(TAG, "Error creating view holder for type: " + viewType, e);
            return ViewHolderFactory.createViewHolder(parent, EmptyCard.LAYOUT_ID, this.lifecycleOwner);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<?> holder, int position) {
        try {
            LayoutData<?> layoutData = getItem(position);
            if (layoutData != null) {
                holder.bind(layoutData, lifecycleOwner, listener);
            }
        } catch (Throwable e) {
            Log.e(TAG, "Error binding view holder at position: " + position, e);
            handleBindError(holder);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<?> holder, int position, @NonNull List<Object> payloads) {
        try {
            if (payloads.isEmpty()) {
                super.onBindViewHolder(holder, position, payloads);
            } else {
                // 通常只有一个 payload
                Bundle diff = (Bundle) payloads.get(0);
                holder.update(diff);
            }
        } catch (Throwable e) {
            Log.e(TAG, "Error binding view holder (payloads) at position: " + position, e);
            handleBindError(holder);
        }
    }

    /**
     * 处理绑定异常：物理隐藏 Item
     */
    private void handleBindError(BaseViewHolder<?> holder) {
        View itemView = holder.itemView;
        itemView.setVisibility(View.GONE);
        ViewGroup.LayoutParams params = itemView.getLayoutParams();
        if (params != null) {
            params.height = 0;
            params.width = 0;
            itemView.setLayoutParams(params);
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
        // 及时从生命周期集合中移除，防止无用弱引用堆积
        removeLifecycleRef(holder);
    }

    /**
     * 激活可见范围内所有的监听生命周期的卡片，不可见则暂停
     *
     * @param recyclerView 页面列表RecyclerView
     */
    public void activeCardsIfVisible(RecyclerView recyclerView) {
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager == null) {
            return;
        }

        int firstVisible, lastVisible;
        if (manager instanceof LinearLayoutManager) {
            // GridLayoutManager 也是 LinearLayoutManager 的子类
            LinearLayoutManager llm = (LinearLayoutManager) manager;
            firstVisible = llm.findFirstVisibleItemPosition();
            lastVisible = llm.findLastVisibleItemPosition();
        } else if (manager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager sglm = (StaggeredGridLayoutManager) manager;
            int[] first = sglm.findFirstVisibleItemPositions(null);
            int[] last = sglm.findLastVisibleItemPositions(null);
            firstVisible = min(first);
            lastVisible = max(last);
        } else {
            // 不支持的 LayoutManager
            return;
        }

        // 遍历所有生命周期 holder，根据最新位置调用 onResume/onPause
        Iterator<WeakReference<BaseViewHolder<?>>> iterator = lifecycleHolderRefs.iterator();
        while (iterator.hasNext()) {
            WeakReference<BaseViewHolder<?>> ref = iterator.next();
            BaseViewHolder<?> holder = ref.get();
            if (holder == null) {
                iterator.remove();
                continue;
            }
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) {
                continue;
            }
            if (pos >= firstVisible && pos <= lastVisible) {
                holder.onResume();
            } else {
                holder.onPause();
            }
        }
    }

    /**
     * 暂停所有监听生命周期的卡片
     */
    public void pauseAllCards() {
        Iterator<WeakReference<BaseViewHolder<?>>> iterator = lifecycleHolderRefs.iterator();
        while (iterator.hasNext()) {
            WeakReference<BaseViewHolder<?>> ref = iterator.next();
            BaseViewHolder<?> holder = ref.get();
            if (holder == null) {
                iterator.remove();
            } else {
                holder.onPause();
            }
        }
    }

    /**
     * 清理指定ViewHolder
     *
     * @param holder BaseViewHolder
     */
    private void removeLifecycleRef(BaseViewHolder<?> holder) {
        Iterator<WeakReference<BaseViewHolder<?>>> it = lifecycleHolderRefs.iterator();
        while (it.hasNext()) {
            if (it.next().get() == holder) {
                it.remove();
                break;
            }
        }
    }

    /**
     * 释放所有资源
     */
    public void releaseAllResources() {
        // 暂停并清理生命周期 holder
        pauseAllCards();
        lifecycleHolderRefs.clear();

        // 清理所有 ViewHolder 资源
        for (WeakReference<BaseViewHolder<?>> ref : viewHolderRefs) {
            BaseViewHolder<?> holder = ref.get();
            if (holder != null) {
                holder.cleanup();
            }
        }
        viewHolderRefs.clear();
    }

    private int min(int[] arr) {
        int min = Integer.MAX_VALUE;
        for (int v : arr) {
            min = Math.min(min, v);
        }
        return min;
    }

    private int max(int[] arr) {
        int max = Integer.MIN_VALUE;
        for (int v : arr) {
            max = Math.max(max, v);
        }
        return max;
    }
}