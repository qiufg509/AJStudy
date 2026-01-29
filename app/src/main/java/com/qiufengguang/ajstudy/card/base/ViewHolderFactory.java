package com.qiufengguang.ajstudy.card.base;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.viewbinding.ViewBinding;

import com.qiufengguang.ajstudy.card.banner.BannerViewHolder;
import com.qiufengguang.ajstudy.card.grid.GridViewHolder;
import com.qiufengguang.ajstudy.card.largegraphic.LargeGraphicViewHolder;
import com.qiufengguang.ajstudy.card.normal.NormalCardViewHolder;
import com.qiufengguang.ajstudy.card.series.SeriesCardViewHolder;
import com.qiufengguang.ajstudy.data.BannerBean;
import com.qiufengguang.ajstudy.data.GridCardBean;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.data.SeriesCardBean;
import com.qiufengguang.ajstudy.databinding.CardBannerBinding;
import com.qiufengguang.ajstudy.databinding.CardGridBinding;
import com.qiufengguang.ajstudy.databinding.CardLargeGraphicBinding;
import com.qiufengguang.ajstudy.databinding.CardNormalBinding;
import com.qiufengguang.ajstudy.databinding.CardSeriesBinding;

/**
 * 卡片ViewHolder工厂方法
 *
 * @author qiufengguang
 * @since 2026/1/29 13:00
 */
public class ViewHolderFactory {
    public static BaseViewHolder<? extends ViewBinding> createViewHolder(
        @NonNull ViewGroup parent, int viewType, LifecycleOwner lifecycleOwner) {
        switch (viewType) {
            case BannerBean.LAYOUT_ID:
                CardBannerBinding bannerBinding = CardBannerBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                return new BannerViewHolder(
                    bannerBinding, lifecycleOwner);
            case GridCardBean.LAYOUT_ID:
                CardGridBinding gridBinding = CardGridBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                return new GridViewHolder(gridBinding);
            case SeriesCardBean.LAYOUT_ID:
                CardSeriesBinding seriesBinding = CardSeriesBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                return new SeriesCardViewHolder(seriesBinding);
            case LargeGraphicCardBean.LAYOUT_ID:
                CardLargeGraphicBinding largeGraphicBinding = CardLargeGraphicBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                return new LargeGraphicViewHolder(largeGraphicBinding);
            default:
                CardNormalBinding normalBinding = CardNormalBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                return new NormalCardViewHolder(normalBinding);
        }
    }
}
