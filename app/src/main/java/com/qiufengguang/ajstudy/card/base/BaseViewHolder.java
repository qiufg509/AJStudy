package com.qiufengguang.ajstudy.card.base;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

import com.qiufengguang.ajstudy.data.base.LayoutData;

/**
 * 卡片基类ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/24 16:23
 */
public abstract class BaseViewHolder<B extends ViewBinding> extends RecyclerView.ViewHolder {

    protected B binding;

    /**
     * 是否监听 onResume 和 onPause
     */
    public boolean isObserveResumePause = false;

    public BaseViewHolder(@NonNull B binding) {
        super(binding.getRoot());
        this.binding = binding;
        initCard();
    }

    /**
     * 初始化卡片
     */
    public abstract void initCard();

    /**
     * 绑定数据
     */
    public abstract void bind(LayoutData<?> data);

    /**
     * 绑定数据
     */
    public void bind(LayoutData<?> data, LifecycleOwner lifecycleOwner) {
        Object object = data.getData();
        if (object == null) {
            return;
        }
        this.bind(data);
    }

    /**
     * 清理资源，防止内存泄漏
     * 只清理自己创建的资源，不要清理系统管理的资源（如 binding）
     */
    public void cleanup() {
        // 重要：不要清空 binding！binding 的生命周期应该由 RecyclerView 和 Android 系统管理，而不是由 ViewHolder 手动清理。ViewHolder 只是使用者，不是所有者
    }

    /**
     * 当 ViewHolder 进入屏幕时调用
     */
    public void onViewAttachedToWindow() {
    }

    /**
     * 当 ViewHolder 离开屏幕时调用
     */
    public void onViewDetachedFromWindow() {
    }

    /**
     * Called when the card is no longer resumed
     */
    public void onResume() {
    }

    /**
     * Called when the card is visible to the user and actively running.
     */
    public void onPause() {
    }

    /**
     * 更新卡片
     *
     * @param data 卡片数据
     */
    public void update(LayoutData<?> data) {
    }
}