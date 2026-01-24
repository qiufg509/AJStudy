package com.qiufengguang.ajstudy.card.grid;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.GridCardBean;
import com.qiufengguang.ajstudy.databinding.CardGridImageBinding;
import com.qiufengguang.ajstudy.databinding.CardGridTextBinding;

import java.util.List;

/**
 * 格网卡片适配器
 *
 * @author qiufengguang
 * @since 2025/12/28 18:19
 */
public class GridCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int itemType;

    private List<GridCardBean> beans;

    private OnItemClickListener listener;

    public GridCardAdapter(int itemType, @Nullable List<GridCardBean> beans) {
        this.itemType = itemType;
        this.beans = beans;
    }

    public void setData(List<GridCardBean> beans) {
        if (beans == null || beans.isEmpty()) {
            this.beans = beans;
            notifyItemRangeRemoved(0, getItemCount());
            return;
        }
        if (this.beans == null || this.beans.isEmpty()) {
            this.beans = beans;
            notifyItemRangeInserted(0, getItemCount());
        } else {
            this.beans = beans;
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 获取ViewType，根据position判断
     */
    @Override
    public int getItemViewType(int position) {
        return itemType;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == GridCardWrapper.TYPE_TEXT) {
            CardGridTextBinding binding = CardGridTextBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new GridCardTextHolder(binding, listener);
        } else {
            CardGridImageBinding binding = CardGridImageBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new GridCardImageHolder(binding, listener);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        GridCardBean bean = beans.get(position);
        if (holder instanceof GridCardTextHolder) {
            ((GridCardTextHolder) holder).bind(bean);
        } else {
            ((GridCardImageHolder) holder).bind(bean);
        }
    }

    @Override
    public int getItemCount() {
        return beans == null ? 0 : beans.size();
    }

    public static class GridCardTextHolder extends RecyclerView.ViewHolder {

        CardGridTextBinding binding;

        GridCardBean bean;

        public GridCardTextHolder(@NonNull CardGridTextBinding binding, OnItemClickListener clickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null && bean != null) {
                    clickListener.onItemClick(v.getContext(), bean);
                }
            });
        }

        public void bind(GridCardBean bean) {
            this.bean = bean;
            binding.getRoot().setText(bean.getTitle());
            Drawable drawable = ContextCompat.getDrawable(
                this.binding.getRoot().getContext(), bean.getIcon());
            binding.getRoot().setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, drawable, null, null);
        }
    }

    public static class GridCardImageHolder extends RecyclerView.ViewHolder {

        CardGridImageBinding binding;

        GridCardBean bean;

        public GridCardImageHolder(@NonNull CardGridImageBinding binding, OnItemClickListener clickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null && bean != null
                    && bean.getIcon() != R.drawable.ic_checkmark) {
                    bean.setIcon(R.drawable.ic_checkmark);
                    clickListener.onItemClick(v.getContext(), bean);
                }
            });
        }

        public void bind(GridCardBean bean) {
            this.bean = bean;
            binding.getRoot().setBackgroundTintList(ColorStateList.valueOf(
                ContextCompat.getColor(binding.getRoot().getContext(), bean.getBackgroundTint())));

            if (bean.getIcon() == 0) {
                binding.getRoot().setImageDrawable(null);
            } else {
                binding.getRoot().setImageResource(bean.getIcon());
            }
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Context context, GridCardBean bean);
    }
}