package com.qiufengguang.ajstudy.fragment.home;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.card.banner.BannerViewHolder;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.grid.GridViewHolder;
import com.qiufengguang.ajstudy.card.largegraphic.LargeGraphicViewHolder;
import com.qiufengguang.ajstudy.card.series.SeriesCardViewHolder;
import com.qiufengguang.ajstudy.data.BannerBean;
import com.qiufengguang.ajstudy.data.GridCardBean;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.data.LayoutData;
import com.qiufengguang.ajstudy.data.SeriesCardBean;
import com.qiufengguang.ajstudy.databinding.CardBannerBinding;
import com.qiufengguang.ajstudy.databinding.CardGridBinding;
import com.qiufengguang.ajstudy.databinding.CardLargeGraphicBinding;
import com.qiufengguang.ajstudy.databinding.CardSeriesBinding;

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
public class HomeAdapter extends RecyclerView.Adapter<BaseViewHolder<?>> {

    /**
     * banner卡片
     */
    private static final int VIEW_TYPE_BANNER = 0;
    /**
     * 格网卡片
     */
    private static final int VIEW_TYPE_GRID_CARD = 1;
    /**
     * 系列卡片
     */
    private static final int VIEW_TYPE_SERIES_CARD = 3;
    /**
     * 图文大卡
     */
    private static final int VIEW_TYPE_L_G_CARD = 4;

    private final LifecycleOwner lifecycleOwner;

    private List<LayoutData<?>> dataList;

    /**
     * 存储所有banner的位置
     */
    private final List<Integer> bannerPositions = new ArrayList<>();

    /**
     * 存储所有BannerViewHolder的引用
     */
    private final Set<WeakReference<BannerViewHolder>> bannerViewHolderRefs = new HashSet<>();

    /**
     * 存储所有ViewHolder的引用（用于资源清理）
     */
    private final Set<WeakReference<BaseViewHolder<?>>> viewHolderRefs = new HashSet<>();

    public HomeAdapter(LifecycleOwner lifecycleOwner) {
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
            pauseAllBanners();
            notifyItemRangeRemoved(0, getItemCount());
            return;
        }
        if (this.dataList == null || this.dataList.isEmpty()) {
            this.dataList = dataList;
            findAllBannerPositions();
            notifyItemRangeInserted(0, getItemCount());
        } else {
            this.dataList = dataList;
            findAllBannerPositions();
            notifyItemRangeChanged(0, getItemCount());
        }
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
        switch (layoutData.getLayoutName()) {
            case BannerBean.LAYOUT_NAME:
                return VIEW_TYPE_BANNER;
            case GridCardBean.LAYOUT_NAME:
                return VIEW_TYPE_GRID_CARD;
            case LargeGraphicCardBean.LAYOUT_NAME:
                return VIEW_TYPE_L_G_CARD;
            case SeriesCardBean.LAYOUT_NAME:
                return VIEW_TYPE_SERIES_CARD;
            default:
                return -1;
        }
    }

    @Override
    public int getItemCount() {
        if (dataList == null || dataList.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (int index = 0, sum = dataList.size(); index < sum; index++) {
            LayoutData<?> layoutData = dataList.get(index);
            if (layoutData == null || layoutData.getBeans() == null) {
                continue;
            }
            count++;
        }
        return count;
    }

    @NonNull
    @Override
    public BaseViewHolder<?> onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_BANNER:
                CardBannerBinding bannerBinding = CardBannerBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                BannerViewHolder bannerViewHolder = new BannerViewHolder(
                    bannerBinding, this.lifecycleOwner);
                // 添加到专门的banner引用集合
                bannerViewHolderRefs.add(new WeakReference<>(bannerViewHolder));
                viewHolderRefs.add(new WeakReference<>(bannerViewHolder));
                return bannerViewHolder;
            case VIEW_TYPE_GRID_CARD:
                CardGridBinding gridBinding = CardGridBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                GridViewHolder gridViewHolder = new GridViewHolder(gridBinding);
                viewHolderRefs.add(new WeakReference<>(gridViewHolder));
                return gridViewHolder;
            case VIEW_TYPE_SERIES_CARD:
                CardSeriesBinding seriesBinding = CardSeriesBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                SeriesCardViewHolder seriesViewHolder = new SeriesCardViewHolder(seriesBinding);
                viewHolderRefs.add(new WeakReference<>(seriesViewHolder));
                return seriesViewHolder;
            default:
                CardLargeGraphicBinding largeGraphicBinding = CardLargeGraphicBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                LargeGraphicViewHolder lgcViewHolder = new LargeGraphicViewHolder(largeGraphicBinding);
                viewHolderRefs.add(new WeakReference<>(lgcViewHolder));
                return lgcViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseViewHolder<?> holder, int position) {
        LayoutData<?> layoutData = dataList.get(position);
        if (layoutData == null) {
            return;
        }
        Object data = layoutData.getBeans();
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
     * 查找所有banner的位置
     */
    private void findAllBannerPositions() {
        bannerPositions.clear();
        for (int index = 0; index < dataList.size(); index++) {
            LayoutData<?> layoutData = dataList.get(index);
            if (layoutData == null || layoutData.getBeans() == null) {
                continue;
            }
            if (TextUtils.equals(layoutData.getLayoutName(), BannerBean.LAYOUT_NAME)) {
                bannerPositions.add(index);
            }
        }
    }

    /**
     * 激活所有范围内的轮播banner，不可见则停止轮播
     *
     * @param recyclerView 页面列表RecyclerView
     */
    public void activeBannersIfVisible(RecyclerView recyclerView) {
        if (recyclerView.getLayoutManager() == null) {
            return;
        }

        // 获取当前可见位置范围
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        int firstVisible = layoutManager.findFirstVisibleItemPosition();
        int lastVisible = layoutManager.findLastVisibleItemPosition();

        // 遍历所有banner位置，恢复可见的banner
        for (int bannerPos : bannerPositions) {
            if (bannerPos < firstVisible || bannerPos > lastVisible) {
                continue;
            }
            for (WeakReference<BannerViewHolder> ref : bannerViewHolderRefs) {
                BannerViewHolder holder = ref.get();
                if (holder == null) {
                    continue;
                }
                holder.setBannerActive(holder.getBindingAdapterPosition() == bannerPos);
            }
        }
    }

    /**
     * 暂停所有banner的轮播
     */
    public void pauseAllBanners() {
        for (WeakReference<BannerViewHolder> ref : bannerViewHolderRefs) {
            BannerViewHolder holder = ref.get();
            if (holder != null) {
                holder.setBannerActive(false);
            }
        }
    }

    /**
     * 清理所有 ViewHolder 的资源
     */
    public void releaseAllResources() {
        // 清理所有BannerViewHolder引用
        bannerViewHolderRefs.clear();
        bannerPositions.clear();

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