package com.qiufengguang.ajstudy.fragment.home;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.card.banner.BannerViewHolder;
import com.qiufengguang.ajstudy.card.grid.GridViewHolder;
import com.qiufengguang.ajstudy.data.BannerBean;
import com.qiufengguang.ajstudy.data.BaseCardBean;
import com.qiufengguang.ajstudy.data.GridCardBean;
import com.qiufengguang.ajstudy.databinding.CardBannerBinding;
import com.qiufengguang.ajstudy.databinding.CardGridBinding;

import java.lang.ref.WeakReference;
import java.util.List;

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
     * 列表卡
     */
    private static final int VIEW_TYPE_LIST = 4;

    private final LifecycleOwner lifecycleOwner;

    private List<List<? extends BaseCardBean>> dataList;

    private int bannerPosition = -1;
    private WeakReference<BannerViewHolder> bannerViewHolderRef;

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
            default:
                return VIEW_TYPE_LIST;
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
                    bannerBinding.getRoot(), this.lifecycleOwner);
                bannerViewHolderRef = new WeakReference<>(bannerViewHolder);
                return bannerViewHolder;
            case VIEW_TYPE_GRID_CARD:
            default:
                CardGridBinding gridBinding = CardGridBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                return new GridViewHolder(gridBinding.getRoot(), this.lifecycleOwner);
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
        }
    }


    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        if (holder instanceof BannerViewHolder) {
            ((BannerViewHolder) holder).onViewAttachedToWindow();
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        if (holder instanceof BannerViewHolder) {
            ((BannerViewHolder) holder).onViewDetachedFromWindow();
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
        if (isBannerVisible(recyclerView)) {
            BannerViewHolder holder = bannerViewHolderRef != null ? bannerViewHolderRef.get() : null;
            if (holder != null) {
                holder.setBannerActive(true);
            }
        }
    }

    public void pauseBanner() {
        BannerViewHolder holder = bannerViewHolderRef != null ? bannerViewHolderRef.get() : null;
        if (holder != null) {
            holder.setBannerActive(false);
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

    public void releaseAllResources() {
        BannerViewHolder holder = bannerViewHolderRef != null ? bannerViewHolderRef.get() : null;
        if (holder != null) {
            holder.cleanup();
        }
        bannerViewHolderRef = null;
        dataList = null;
    }
}