package com.qiufengguang.ajstudy.card.banner;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.model.BannerBean;
import com.qiufengguang.ajstudy.databinding.ItemHomeBannerBinding;

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
    private static final String TAG = "BannerAdapter";

    // 预加载配置 - 修改为较小的值
    private static final int PRELOAD_DISTANCE = 1;

    private List<BannerBean> bannerBeans;

    private OnItemClickListener<BannerBean> clickListener;

    private final int itemWidth;

    private final int itemHeight;

    private final int itemRatio;


    public BannerAdapter(int itemWidth, int itemHeight, int itemRatio) {
        this.itemWidth = itemWidth;
        this.itemHeight = itemHeight;
        this.itemRatio = itemRatio;
    }

    public void setBannerBeans(List<BannerBean> bannerBeans) {
        if (bannerBeans == null || bannerBeans.isEmpty()) {
            this.bannerBeans = bannerBeans;
            notifyItemRangeRemoved(0, getItemCount());
            return;
        }
        if (this.bannerBeans == null || this.bannerBeans.isEmpty()) {
            this.bannerBeans = bannerBeans;
            notifyItemRangeInserted(0, getItemCount());
        } else {
            this.bannerBeans = bannerBeans;
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    @NonNull
    @Override
    public BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemHomeBannerBinding binding = ItemHomeBannerBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        ViewGroup.LayoutParams layoutParams = binding.getRoot().getLayoutParams();
        layoutParams.width = itemWidth;
        layoutParams.height = itemHeight;
        binding.getRoot().setLayoutParams(layoutParams);
        binding.getRoot().setCornerRadius(itemRatio);
        return new BannerViewHolder(binding, clickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull BannerViewHolder holder, int position) {
        // 计算真实位置
        int realPosition = getRealPosition(position);
        BannerBean item = bannerBeans.get(realPosition);
        holder.bind(item);

        // 预加载相邻的图片
        preloadAdjacentImages(holder, position);
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

    private void preloadAdjacentImages(BannerViewHolder currentHolder, int position) {
        // 预加载前后图片
        for (int i = -PRELOAD_DISTANCE; i <= PRELOAD_DISTANCE; i++) {
            int preloadPosition = position + i;
            if (preloadPosition >= 0 && preloadPosition < getItemCount()) {
                int realPosition = getRealPosition(preloadPosition);
                if (realPosition >= 0 && realPosition < bannerBeans.size()) {
                    BannerBean item = bannerBeans.get(realPosition);
                    preloadImage(currentHolder, item.getImageUrl());
                }
            }
        }
    }

    private void preloadImage(BannerViewHolder holder, String imageUrl) {
        if (TextUtils.isEmpty(imageUrl)) {
            return;
        }
        try {
            Glide.with(holder.itemView.getContext())
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .preload();
        } catch (IllegalArgumentException e) {
            // 捕获异常，防止崩溃
            Log.w(TAG, "preloadImage error.");
        }
    }

    /**
     * 计算真实位置，实现无限循环
     */
    public int getRealPosition(int position) {
        return bannerBeans == null || bannerBeans.isEmpty() ? 0 : position % bannerBeans.size();
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

    public void setOnBannerClickListener(OnItemClickListener<BannerBean> listener) {
        this.clickListener = listener;
    }

    /**
     * 优化的ViewHolder实现
     */
    public static class BannerViewHolder extends RecyclerView.ViewHolder {
        private final ItemHomeBannerBinding binding;

        private BannerBean bean;

        private static final RequestOptions GLIDE_OPTIONS = new RequestOptions()
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .skipMemoryCache(false)
            .timeout(10000) // 10秒超时
            .format(DecodeFormat.PREFER_ARGB_8888);

        private static final DrawableTransitionOptions TRANSITION_OPTIONS =
            new DrawableTransitionOptions().crossFade(300); // 300ms淡入动画

        public BannerViewHolder(
            @NonNull ItemHomeBannerBinding binding,
            OnItemClickListener<BannerBean> clickListener
        ) {
            super(binding.getRoot());
            this.binding = binding;
            // 设置点击事件
            this.binding.banner.setOnClickListener(v -> {
                if (clickListener != null && bean != null) {
                    clickListener.onItemClick(v.getContext(), bean);
                }
            });
        }

        public void bind(BannerBean item) {
            this.bean = item;
            this.binding.ivBanner.setContentDescription(item.getTitle());

            // 启动图片加载
            startImageLoading();
        }

        public void startImageLoading() {
            if (bean != null && !TextUtils.isEmpty(bean.getImageUrl())) {
                Glide.with(itemView.getContext())
                    .load(bean.getImageUrl())
                    .placeholder(R.drawable.placeholder_image_16_9)
                    .apply(GLIDE_OPTIONS)
                    .transition(TRANSITION_OPTIONS)
                    .into(this.binding.ivBanner);
            } else {
                this.binding.ivBanner.setImageResource(R.drawable.placeholder_image_16_9);
            }
        }

        public void clearResources() {
            // 清理ViewHolder资源
            this.binding.ivBanner.setImageDrawable(null);
        }

        public void clearGlideRequest() {
            // 清理Glide请求，避免内存泄漏
            try {
                Context context = this.binding.ivBanner.getContext().getApplicationContext();
                Glide.with(context).clear(this.binding.ivBanner);
            } catch (IllegalArgumentException e) {
                // 忽略异常，Context可能已销毁
                Log.w(TAG, "clearGlideRequest error.");
            }
        }
    }
}