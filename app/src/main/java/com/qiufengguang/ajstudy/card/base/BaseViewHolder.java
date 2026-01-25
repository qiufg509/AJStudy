package com.qiufengguang.ajstudy.card.base;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;

/**
 * 卡片基类ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/24 16:23
 */
public abstract class BaseViewHolder<B extends ViewBinding, T> extends RecyclerView.ViewHolder {

    protected B binding;

    public BaseViewHolder(@NonNull B binding) {
        super(binding.getRoot());
        this.binding = binding;
        initCardWrapper();
    }

    /**
     * 初始化卡片Wrapper
     */
    public abstract void initCardWrapper();

    /**
     * 绑定数据
     */
    public abstract void bind(T data);

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
}