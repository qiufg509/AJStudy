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
    }

    /**
     * 绑定数据
     */
    public abstract void bind(T data);

    /**
     * 清理资源，防止内存泄漏
     */
    public void cleanup() {
        if (binding != null) {
            // 清理 ViewBinding 中的资源
            binding = null;
        }
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