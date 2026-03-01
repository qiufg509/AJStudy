package com.qiufengguang.ajstudy.card.banner;

import androidx.annotation.NonNull;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.BannerBean;
import com.qiufengguang.ajstudy.databinding.CardBannerBinding;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 轮播banner卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/19 14:03
 */
public class BannerCardHolder extends BaseViewHolder<CardBannerBinding>
    implements DefaultLifecycleObserver {

    private BannerCard card;

    private WeakReference<LifecycleOwner> lifecycleOwnerRef;


    public BannerCardHolder(@NonNull CardBannerBinding binding, @NonNull LifecycleOwner lifecycleOwner) {
        super(binding);
        this.lifecycleOwnerRef = new WeakReference<>(lifecycleOwner);
        this.isObserveResumePause = true;
        observeLifecycle();
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new BannerCard.Builder()
            .setRecyclerView(binding.recyclerBanner)
            .setIndicatorContainer(binding.indicatorContainer)
            .setClickListener(this::onCommonClickListener)
            .create();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || !data.isCollection()
            || data.getLayoutId() != BannerCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        @SuppressWarnings("unchecked")
        List<BannerBean> beans = (List<BannerBean>) data.getData();
        card.setBannerBeans(beans);

        checkAndResumeBanner();
    }

    public void bind(LayoutData<?> data, LifecycleOwner lifecycleOwner) {
        if (this.lifecycleOwnerRef == null || this.lifecycleOwnerRef.isEnqueued()) {
            this.lifecycleOwnerRef = new WeakReference<>(lifecycleOwner);
        }
        this.bind(data);
    }

    private void observeLifecycle() {
        LifecycleOwner lifecycleOwner = lifecycleOwnerRef.get();
        if (lifecycleOwner == null) {
            return;
        }
        lifecycleOwner.getLifecycle().addObserver(this);
    }

    @Override
    public void onResume(@NonNull LifecycleOwner owner) {
        checkAndResumeBanner();
    }

    @Override
    public void onPause(@NonNull LifecycleOwner owner) {
        pauseBanner();
    }

    @Override
    public void onResume() {
        checkAndResumeBanner();
    }

    @Override
    public void onPause() {
        pauseBanner();
    }

    @Override
    public void onDestroy(@NonNull LifecycleOwner owner) {
        releaseBanner();
        owner.getLifecycle().removeObserver(this);
    }

    @Override
    public void onViewAttachedToWindow() {
        super.onViewAttachedToWindow();
        checkAndResumeBanner();
    }

    @Override
    public void onViewDetachedFromWindow() {
        super.onViewDetachedFromWindow();
        pauseBanner();
    }

    @Override
    public void cleanup() {
        super.cleanup();
        releaseBanner();
        if (lifecycleOwnerRef != null) {
            LifecycleOwner lifecycleOwner = lifecycleOwnerRef.get();
            if (lifecycleOwner != null) {
                lifecycleOwner.getLifecycle().removeObserver(this);
                lifecycleOwnerRef.clear();
            }
            lifecycleOwnerRef = null;
        }
    }

    /**
     * 检查是否需要恢复轮播
     */
    private void checkAndResumeBanner() {
        if (card != null) {
            card.startOrResumeAutoScroll();
        }
    }

    private void pauseBanner() {
        if (card != null) {
            card.pauseAutoScroll();
        }
    }

    private void releaseBanner() {
        if (card != null) {
            card.release();
            card = null;
        }
    }
}