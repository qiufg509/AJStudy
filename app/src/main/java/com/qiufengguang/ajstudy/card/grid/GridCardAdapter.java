package com.qiufengguang.ajstudy.card.grid;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.model.GridCardBean;
import com.qiufengguang.ajstudy.databinding.ItemGridCardTextBinding;

import java.util.List;

/**
 * 格网卡片适配器
 *
 * @author qiufengguang
 * @since 2025/12/28 18:19
 */
public class GridCardAdapter extends RecyclerView.Adapter<GridCardAdapter.GridCardHolder> {

    private List<GridCardBean> beans;

    private OnItemClickListener<GridCardBean> listener;

    public GridCardAdapter(@Nullable List<GridCardBean> beans) {
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

    public void setOnItemClickListener(OnItemClickListener<GridCardBean> listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public GridCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemGridCardTextBinding binding = ItemGridCardTextBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new GridCardHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GridCardHolder holder, int position) {
        GridCardBean bean = beans.get(position);
        holder.bind(bean);
    }

    @Override
    public int getItemCount() {
        return beans == null ? 0 : beans.size();
    }

    public static class GridCardHolder extends RecyclerView.ViewHolder {

        ItemGridCardTextBinding binding;

        GridCardBean bean;

        RequestOptions options;

        public GridCardHolder(
            @NonNull ItemGridCardTextBinding binding,
            OnItemClickListener<GridCardBean> clickListener
        ) {
            super(binding.getRoot());
            this.binding = binding;
            int iconSize = this.binding.getRoot().getResources().getDimensionPixelSize(R.dimen.item_icon_size_xs);
            options = new RequestOptions().override(iconSize, iconSize).centerCrop();
            this.binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null && bean != null) {
                    clickListener.onItemClick(v.getContext(), bean);
                }
            });
        }

        public void bind(GridCardBean bean) {
            this.bean = bean;
            binding.getRoot().setText(bean.getTitle());
            if (!TextUtils.isEmpty(bean.getIcon())) {
                Glide.with(binding.getRoot().getContext())
                    .load(bean.getIcon())
                    .apply(options)
                    .placeholder(R.drawable.placeholder_icon_circle)
                    .transition(DrawableTransitionOptions.withCrossFade(300))
                    .into(new CustomTarget<Drawable>() {
                        @Override
                        public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                            binding.getRoot().setCompoundDrawablesRelativeWithIntrinsicBounds(
                                null, resource, null, null);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            binding.getRoot().setCompoundDrawablesRelativeWithIntrinsicBounds(
                                null, placeholder, null, null);
                        }
                    });
            } else {
                Drawable drawable = ContextCompat.getDrawable(
                    this.binding.getRoot().getContext(), R.drawable.placeholder_icon_circle);
                binding.getRoot().setCompoundDrawablesRelativeWithIntrinsicBounds(
                    null, drawable, null, null);
            }
        }
    }
}