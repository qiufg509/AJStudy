package com.qiufengguang.ajstudy.card.banner;

import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.BannerBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardBannerBinding;
import com.qiufengguang.ajstudy.dialog.Dialog;
import com.qiufengguang.ajstudy.dialog.IDialog;
import com.qiufengguang.ajstudy.dialog.manager.DialogWrapper;
import com.qiufengguang.ajstudy.dialog.manager.DialogsManager;
import com.qiufengguang.ajstudy.global.GlobalApp;

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

    private BannerCard cardWrapper;

    private WeakReference<LifecycleOwner> lifecycleOwnerRef;


    public BannerCardHolder(@NonNull CardBannerBinding binding, @NonNull LifecycleOwner lifecycleOwner) {
        super(binding);
        this.lifecycleOwnerRef = new WeakReference<>(lifecycleOwner);
        this.isObserveResumePause = true;
        observeLifecycle();
    }

    @Override
    public void initCardWrapper() {
        if (cardWrapper != null) {
            return;
        }
        cardWrapper = new BannerCard.Builder()
            .setRecyclerView(binding.recyclerBanner)
            .setIndicatorContainer(binding.indicatorContainer)
            .setClickListener((context, position, bean) -> {
                if (!(context instanceof AppCompatActivity)) {
                    return;
                }
                handleBannerClick((AppCompatActivity) context, position, bean);
            })
            .create();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || !data.isCollection()
            || !TextUtils.equals(data.getLayoutName(), BannerBean.LAYOUT_NAME)) {
            return;
        }
        if (cardWrapper == null) {
            initCardWrapper();
        }
        @SuppressWarnings("unchecked")
        List<BannerBean> beans = (List<BannerBean>) data.getData();
        cardWrapper.setBannerBeans(beans);

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
        if (cardWrapper != null) {
            cardWrapper.startOrResumeAutoScroll();
        }
    }

    private void pauseBanner() {
        if (cardWrapper != null) {
            cardWrapper.pauseAutoScroll();
        }
    }

    private void releaseBanner() {
        if (cardWrapper != null) {
            cardWrapper.release();
            cardWrapper = null;
        }
    }

    private void handleBannerClick(@NonNull AppCompatActivity activity, int pos, BannerBean item) {
        switch (pos) {
            case 0:
                // 默认单按钮对话框
                buildDefaultDialog1(activity, item.getTitle()).show();
                break;
            case 1:
                // 默认双按钮对话框
                buildDefaultDialog2(activity, item.getTitle()).show();
                break;
            case 2:
                // 自定义对话框
                new Dialog.Builder(activity)
                    .setDialogView(R.layout.dialog_ad)
                    .setWindowBackgroundP(0.5f)
                    .setCancelable(false)
                    .setAnimStyle(R.style.DialogAnimScale)
                    .setBuildChildListener((dialog, parent, layoutRes) ->
                        parent.findViewById(R.id.iv_close)
                            .setOnClickListener(v -> dialog.dismiss()))
                    .show();
                break;
            default:
                // 多个对话框依次弹出
                Dialog.Builder builder1 = buildDefaultDialog1(activity, null);
                Dialog.Builder builder2 = buildDefaultDialog2(activity, item.getTitle());
                DialogsManager.getInstance().requestShow(new DialogWrapper(builder1));
                DialogsManager.getInstance().requestShow(new DialogWrapper(builder2));
                break;
        }
    }

    private Dialog.Builder buildDefaultDialog1(@NonNull AppCompatActivity activity, String title) {
        return new Dialog.Builder(activity)
            .setTitle(title)
            .setAnimStyle(0)
            .setContent("你可以找到所有適用於安卓系統的最佳照片背景去除應用，用於去除照片中的背景。此外，如果你想在線去除圖片背景，最好使用 Vidmore 免費線上背景去除器。")
            .setPositiveButton("按钮文案", IDialog::dismiss);
    }

    private Dialog.Builder buildDefaultDialog2(@NonNull AppCompatActivity activity, String title) {
        return new Dialog.Builder(activity)
            .setTitle(title)
            .setCancelableOutSide(true)
            .setCancelable(false)
            .setAnimStyle(R.style.DialogAnimSlideBottom)
            .setContent("這款基於網路的工具可以幫助你高效去除照片中的背景，是一款功能強大的線上照片背景去除工具。")
            .setPositiveButton(dialog -> {
                Toast.makeText(GlobalApp.getContext(), "确定", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            })
            .setNegativeButton(dialog -> {
                Toast.makeText(GlobalApp.getContext(), "取消", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            });
    }
}