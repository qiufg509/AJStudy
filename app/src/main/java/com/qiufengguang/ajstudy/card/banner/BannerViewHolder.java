package com.qiufengguang.ajstudy.card.banner;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.data.BannerBean;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 轮播banner卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/19 14:03
 */
public class BannerViewHolder extends RecyclerView.ViewHolder {

    private BannerWrapper bannerWrapper;
    private boolean isBannerActive = false;
    private WeakReference<LifecycleOwner> lifecycleOwnerRef;
    private WeakReference<View> bannerViewRef;

    public BannerViewHolder(@NonNull View itemView, @NonNull LifecycleOwner lifecycleOwner) {
        super(itemView);
        this.lifecycleOwnerRef = new WeakReference<>(lifecycleOwner);
        this.bannerViewRef = new WeakReference<>(itemView);

        // 初始化 BannerWrapper
        bannerWrapper = new BannerWrapper.Builder()
            .setBannerLayout(itemView)
            .create();
        observeLifecycle();
    }

    public void bind(List<BannerBean> bannerBeans) {
        if (bannerBeans != null && bannerWrapper != null) {
            bannerWrapper.setBannerBeans(bannerBeans);
        }

        checkAndResumeBanner();
    }

    private void observeLifecycle() {
        LifecycleOwner lifecycleOwner = lifecycleOwnerRef.get();
        if (lifecycleOwner == null) {
            return;
        }
        lifecycleOwner.getLifecycle().addObserver(new DefaultLifecycleObserver() {
            @Override
            public void onResume(@NonNull LifecycleOwner owner) {
                checkAndResumeBanner();
            }

            @Override
            public void onPause(@NonNull LifecycleOwner owner) {
                pauseBanner();
            }

            @Override
            public void onDestroy(@NonNull LifecycleOwner owner) {
                releaseBanner();
                lifecycleOwner.getLifecycle().removeObserver(this);
            }
        });
    }

    public void onViewAttachedToWindow() {
        checkAndResumeBanner();
    }

    public void onViewDetachedFromWindow() {
        pauseBanner();
    }

    /**
     * 检查是否需要恢复轮播
     */
    private void checkAndResumeBanner() {
        if (bannerWrapper != null && isBannerActive) {
            // 只有当 Banner 完全可见时才恢复轮播
            if (isBannerFullyVisible()) {
                bannerWrapper.resumeAutoScroll();
            }
        }
    }

    private boolean isBannerFullyVisible() {
        View bannerView = bannerViewRef.get();
        if (bannerView == null) return false;

        Rect rect = new Rect();
        boolean isVisible = bannerView.getGlobalVisibleRect(rect);
        if (!isVisible) return false;

        int screenHeight = bannerView.getResources().getDisplayMetrics().heightPixels;
        return rect.top >= 0 && rect.bottom <= screenHeight;
    }

    private void pauseBanner() {
        if (bannerWrapper != null) {
            bannerWrapper.pauseAutoScroll();
        }
        isBannerActive = false;
    }

    private void releaseBanner() {
        if (bannerWrapper != null) {
            bannerWrapper.release();
            bannerWrapper = null;
        }
        isBannerActive = false;
    }

    public void setBannerActive(boolean active) {
        this.isBannerActive = active;
        if (active) {
            checkAndResumeBanner();
        } else {
            pauseBanner();
        }
    }

    public void cleanup() {
        releaseBanner();
        lifecycleOwnerRef = null;
        bannerViewRef = null;
    }
}