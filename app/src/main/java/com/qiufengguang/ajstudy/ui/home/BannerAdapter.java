package com.qiufengguang.ajstudy.ui.home;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.BannerBean;
import com.qiufengguang.ajstudy.databinding.ItemHomeBannerBinding;
import com.qiufengguang.ajstudy.global.GlobalApp;

import java.util.ArrayList;
import java.util.List;

/**
 * 优化后的Banner适配器
 * 特点：
 * 1. 高效的图片加载和缓存策略
 * 2. 现代化的ViewHolder实现
 * 3. 智能预加载机制
 * 4. 平滑的动画效果
 * 5. 完善的错误处理
 *
 * @author qiufengguang
 * @since 2025/12/19 15:36
 */
public class BannerAdapter extends RecyclerView.Adapter<BannerAdapter.BannerViewHolder> {
    // 预加载配置
    private static final int PRELOAD_DISTANCE = 2;

    private final List<BannerBean> bannerBeans = new ArrayList<>();

    private OnBannerClickListener clickListener;

    public void setBannerBeans(List<BannerBean> bannerBeans) {
        if (bannerBeans == null || bannerBeans.isEmpty()) {
            return;
        }
        this.bannerBeans.addAll(bannerBeans);
        notifyItemRangeInserted(0, this.bannerBeans.size());
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeBannerBinding binding = ItemHomeBannerBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new BannerViewHolder(binding, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        // 计算真实位置
        int realPosition = getRealPosition(position);
        BannerBean item = bannerBeans.get(realPosition);

        holder.bind(item, realPosition);

        // 预加载相邻的图片
        preloadAdjacentImages(position);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull BannerViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        // 视图附加时开始图片加载
        holder.startImageLoading();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull BannerViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        // 视图分离时清理资源
        holder.clearResources();
    }

    @Override
    public void onViewRecycled(@NonNull BannerViewHolder holder) {
        super.onViewRecycled(holder);
        // 视图回收时清理Glide请求
        holder.clearGlideRequest();
    }

    private void preloadAdjacentImages(int position) {
        // 预加载前后图片
        for (int i = -PRELOAD_DISTANCE; i <= PRELOAD_DISTANCE; i++) {
            int preloadPosition = position + i;
            if (preloadPosition >= 0 && preloadPosition < getItemCount()) {
                int realPosition = getRealPosition(preloadPosition);
                if (realPosition >= 0 && realPosition < bannerBeans.size()) {
                    BannerBean item = bannerBeans.get(realPosition);
                    preloadImage(item.getImageUrl());
                }
            }
        }
    }

    private void preloadImage(String imageUrl) {
        if (GlobalApp.getContext() == null) {
            return;
        }
        Glide.with(GlobalApp.getContext())
            .load(imageUrl)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .preload();
    }

    /**
     * 计算真实位置，实现无限循环
     */
    public int getRealPosition(int position) {
        return bannerBeans.isEmpty() ? 0 : position % bannerBeans.size();
    }

    /**
     * 获取真实数量
     */
    public int getRealItemCount() {
        return bannerBeans.size();
    }

    @Override
    public int getItemCount() {
        // 返回Integer.MAX_VALUE实现真正的无限滚动
        return bannerBeans.isEmpty() ? 0 : Integer.MAX_VALUE;
    }

    public void setOnBannerClickListener(OnBannerClickListener listener) {
        this.clickListener = listener;
    }

    /**
     * 优化的ViewHolder实现
     */
    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        private final ItemHomeBannerBinding binding;

        private BannerBean bean;

        private int currentRealPosition;

        // Glide配置选项
        private static final RequestOptions GLIDE_OPTIONS = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .skipMemoryCache(false)
            .timeout(10000) // 10秒超时
            .format(com.bumptech.glide.load.DecodeFormat.PREFER_ARGB_8888);

        private static final DrawableTransitionOptions TRANSITION_OPTIONS =
            new DrawableTransitionOptions().crossFade(300); // 300ms淡入动画

        public BannerViewHolder(@NonNull ItemHomeBannerBinding binding, OnBannerClickListener clickListener) {
            super(binding.getRoot());
            this.binding = binding;
            // 设置点击事件
            this.binding.banner.setOnClickListener(v -> {
                if (clickListener != null && bean != null) {
                    clickListener.onBannerClick(currentRealPosition, bean);
                }
            });
        }

        public void bind(BannerBean item, int realPosition) {
            this.bean = item;
            this.currentRealPosition = realPosition;
            this.binding.ivBanner.setContentDescription(item.getTitle());

            // 启动图片加载
            startImageLoading();
        }

        public void startImageLoading() {
            if (!TextUtils.isEmpty(bean.getImageUrl())) {
                Glide.with(itemView.getContext())
                    .load(bean.getImageUrl())
                    .placeholder(R.drawable.placeholder_image)
                    .apply(GLIDE_OPTIONS)
                    .transition(TRANSITION_OPTIONS)
                    .into(this.binding.ivBanner);
            } else {
                this.binding.ivBanner.setImageResource(R.drawable.placeholder_image);
            }
        }

        public void clearResources() {
            // 清理ViewHolder资源
            this.binding.ivBanner.setImageDrawable(null);
        }

        public void clearGlideRequest() {
            // 清理Glide请求，避免内存泄漏
            Glide.with(itemView.getContext()).clear(this.binding.ivBanner);
        }
    }

    /**
     * Banner点击监听接口
     */
    public interface OnBannerClickListener {
        void onBannerClick(int position, BannerBean bean);
    }
}