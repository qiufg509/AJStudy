package com.qiufengguang.ajstudy.fragment.home;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.card.banner.BannerViewHolder;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.grid.GridViewHolder;
import com.qiufengguang.ajstudy.card.largegraphic.LargeGraphicViewHolder;
import com.qiufengguang.ajstudy.data.BannerBean;
import com.qiufengguang.ajstudy.data.BaseCardBean;
import com.qiufengguang.ajstudy.data.GridCardBean;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.databinding.CardBannerBinding;
import com.qiufengguang.ajstudy.databinding.CardGridBinding;
import com.qiufengguang.ajstudy.databinding.CardLargeGraphicBinding;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 首页适配器
 *
 * @author qiufengguang
 * @since 2026/1/19 15:26
 */
public class HomeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * banner卡片
     */
    private static final int VIEW_TYPE_BANNER = 0;
    /**
     * 格网卡片
     */
    private static final int VIEW_TYPE_GRID_CARD = 1;
    /**
     * 二分卡片
     */
    private static final int VIEW_TYPE_TWO_PART_CARD = 3;
    /**
     * 图文大卡
     */
    private static final int VIEW_TYPE_L_G_CARD = 4;

    private final LifecycleOwner lifecycleOwner;

    private List<List<? extends BaseCardBean>> dataList;

    private int bannerPosition = -1;
    private final Set<WeakReference<BaseViewHolder<?, ?>>> viewHolderRefs = new HashSet<>();

    public HomeAdapter(LifecycleOwner lifecycleOwner) {
        this.lifecycleOwner = lifecycleOwner;
    }

    /**
     * 设置数据
     *
     * @param dataList 页面数据
     */
    public void setData(List<List<? extends BaseCardBean>> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            this.dataList = dataList;
            pauseBanner();
            notifyItemRangeRemoved(0, getItemCount());
            return;
        }
        if (this.dataList == null || this.dataList.isEmpty()) {
            this.dataList = dataList;
            findBannerPosition();
            notifyItemRangeInserted(0, getItemCount());
        } else {
            this.dataList = dataList;
            findBannerPosition();
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    /**
     * 获取ViewType，根据position判断
     */
    @Override
    public int getItemViewType(int position) {
        List<? extends BaseCardBean> baseCardBeans = dataList.get(position);
        if (baseCardBeans == null || baseCardBeans.isEmpty()) {
            return -1;
        }
        BaseCardBean baseCardBean = baseCardBeans.get(0);
        switch (baseCardBean.getLayoutName()) {
            case BannerBean.LAYOUT_NAME:
                return VIEW_TYPE_BANNER;
            case GridCardBean.LAYOUT_NAME:
                return VIEW_TYPE_GRID_CARD;
            case LargeGraphicCardBean.LAYOUT_NAME:
                return VIEW_TYPE_L_G_CARD;
            default:
                return VIEW_TYPE_TWO_PART_CARD;
        }
    }

    @Override
    public int getItemCount() {
        if (dataList == null || dataList.isEmpty()) {
            return 0;
        }
        int count = 0;
        for (int index = 0, sum = dataList.size(); index < sum; index++) {
            List<? extends BaseCardBean> baseCardBeans = dataList.get(index);
            if (baseCardBeans == null || baseCardBeans.isEmpty()) {
                continue;
            }
            count++;
        }
        return count;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_BANNER:
                CardBannerBinding bannerBinding = CardBannerBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                BannerViewHolder bannerViewHolder = new BannerViewHolder(
                    bannerBinding, this.lifecycleOwner);
                viewHolderRefs.add(new WeakReference<>(bannerViewHolder));
                return bannerViewHolder;
            case VIEW_TYPE_GRID_CARD:
                CardGridBinding gridBinding = CardGridBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                GridViewHolder gridViewHolder = new GridViewHolder(gridBinding);
                viewHolderRefs.add(new WeakReference<>(gridViewHolder));
                return gridViewHolder;
            default:
                CardLargeGraphicBinding largeGraphicBinding = CardLargeGraphicBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                LargeGraphicViewHolder lgcViewHolder = new LargeGraphicViewHolder(largeGraphicBinding);
                viewHolderRefs.add(new WeakReference<>(lgcViewHolder));
                return lgcViewHolder;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        List<? extends BaseCardBean> baseCardBeans = dataList.get(position);
        if (baseCardBeans == null || baseCardBeans.isEmpty()) {
            return;
        }
        String layoutName = baseCardBeans.get(0).getLayoutName();
        if (holder instanceof BannerViewHolder
            && TextUtils.equals(layoutName, BannerBean.LAYOUT_NAME)) {
            BannerViewHolder bannerViewHolder = (BannerViewHolder) holder;
            @SuppressWarnings("unchecked")
            List<BannerBean> bannerBeans = (List<BannerBean>) baseCardBeans;
            bannerViewHolder.bind(bannerBeans);
            bannerViewHolder.setBannerActive(true);
        } else if (holder instanceof GridViewHolder
            && TextUtils.equals(layoutName, GridCardBean.LAYOUT_NAME)) {
            @SuppressWarnings("unchecked")
            List<GridCardBean> gridCardBeans = (List<GridCardBean>) baseCardBeans;
            ((GridViewHolder) holder).bind(gridCardBeans);
        } else if (holder instanceof LargeGraphicViewHolder
            && TextUtils.equals(layoutName, LargeGraphicCardBean.LAYOUT_NAME)) {
            @SuppressWarnings("unchecked")
            List<LargeGraphicCardBean> lgcBeans = (List<LargeGraphicCardBean>) baseCardBeans;
            ((LargeGraphicViewHolder) holder).bind(lgcBeans);
        }
    }


    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof BaseViewHolder) {
            ((BaseViewHolder<?, ?>) holder).onViewAttachedToWindow();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof BaseViewHolder) {
            ((BaseViewHolder<?, ?>) holder).onViewDetachedFromWindow();
        }
    }

    @Override
    public void onViewRecycled(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewRecycled(holder);
        // 当 ViewHolder 被回收时，清理其资源
        if (holder instanceof BaseViewHolder) {
            ((BaseViewHolder<?, ?>) holder).cleanup();
        }
    }


    private void findBannerPosition() {
        for (int index = 0; index < dataList.size(); index++) {
            List<? extends BaseCardBean> baseCardBeans = dataList.get(index);
            if (baseCardBeans == null || baseCardBeans.isEmpty()) {
                continue;
            }
            BaseCardBean baseCardBean = baseCardBeans.get(0);
            if (baseCardBean != null
                && TextUtils.equals(baseCardBean.getLayoutName(), BannerBean.LAYOUT_NAME)) {
                bannerPosition = index;
                break;
            }
        }
    }

    public void resumeBannerIfVisible(RecyclerView recyclerView) {
        if (!isBannerVisible(recyclerView)) {
            return;
        }
        for (WeakReference<BaseViewHolder<?, ?>> ref : viewHolderRefs) {
            BaseViewHolder<?, ?> holder = ref.get();
            if (holder instanceof BannerViewHolder) {
                ((BannerViewHolder) holder).setBannerActive(true);
            }
        }
    }

    public void pauseBanner() {
        for (WeakReference<BaseViewHolder<?, ?>> ref : viewHolderRefs) {
            BaseViewHolder<?, ?> holder = ref.get();
            if (holder instanceof BannerViewHolder) {
                ((BannerViewHolder) holder).setBannerActive(false);
            }
        }
    }

    public boolean isBannerVisible(RecyclerView recyclerView) {
        if (bannerPosition == -1 || recyclerView.getLayoutManager() == null) {
            return false;
        }

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof androidx.recyclerview.widget.LinearLayoutManager) {
            androidx.recyclerview.widget.LinearLayoutManager linearLayoutManager =
                (androidx.recyclerview.widget.LinearLayoutManager) layoutManager;
            int firstVisible = linearLayoutManager.findFirstVisibleItemPosition();
            int lastVisible = linearLayoutManager.findLastVisibleItemPosition();

            return bannerPosition >= firstVisible && bannerPosition <= lastVisible;
        }
        return false;
    }

    /**
     * 清理所有 ViewHolder 的资源
     */
    public void releaseAllResources() {
        // 清理所有 BaseViewHolder
        for (WeakReference<BaseViewHolder<?, ?>> ref : viewHolderRefs) {
            BaseViewHolder<?, ?> holder = ref.get();
            if (holder != null) {
                holder.cleanup();
            }
        }
        viewHolderRefs.clear();

        // 清理其他资源
        dataList = null;
        bannerPosition = -1;
    }
}