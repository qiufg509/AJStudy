package com.qiufengguang.ajstudy.card.largegraphic;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.databinding.ItemLargeGraphicBinding;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 大图文卡适配器
 *
 * @author qiufengguang
 * @since 2026/1/24 23:30
 */
public class LargeGraphicCardAdapter extends RecyclerView.Adapter<LargeGraphicCardAdapter.LargeGraphicViewHolder> {

    private List<LargeGraphicCardBean> beans;

    private OnItemClickListener<LargeGraphicCardBean> listener;

    public LargeGraphicCardAdapter(@Nullable List<LargeGraphicCardBean> beans) {
        this.beans = beans;
    }

    public void setData(List<LargeGraphicCardBean> beans) {
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

    public void setOnItemClickListener(OnItemClickListener<LargeGraphicCardBean> listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return beans == null ? 0 : beans.size();
    }

    @NonNull
    @Override
    public LargeGraphicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemLargeGraphicBinding binding = ItemLargeGraphicBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new LargeGraphicViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull LargeGraphicViewHolder holder, int position) {
        LargeGraphicCardBean bean = beans.get(position);
        holder.bind(bean);
    }

    public static class LargeGraphicViewHolder extends RecyclerView.ViewHolder {

        ItemLargeGraphicBinding binding;

        LargeGraphicCardBean bean;

        final RequestOptions requestOptions;

        public LargeGraphicViewHolder(
            @NonNull ItemLargeGraphicBinding binding,
            OnItemClickListener<LargeGraphicCardBean> clickListener
        ) {
            super(binding.getRoot());
            this.binding = binding;
            int radius = itemView.getResources().getDimensionPixelSize(R.dimen.radius_l);
            this.requestOptions = new RequestOptions()
                .centerCrop()
                .error(R.drawable.placeholder_image_1_1)
                .transform(new CenterCrop(), new RoundedCornersTransformation(radius, 0,
                    RoundedCornersTransformation.CornerType.TOP));
            this.binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null && bean != null) {
                    clickListener.onItemClick(v.getContext(), bean);
                }
            });
        }

        public void bind(LargeGraphicCardBean bean) {
            this.bean = bean;
            binding.tvTitle.setText(bean.getTitle());
            binding.tvSubtitle.setText(bean.getSubtitle());
            if (!TextUtils.isEmpty(bean.getImageUrl())) {
                Glide.with(itemView.getContext())
                    .load(bean.getImageUrl())
                    .apply(requestOptions)
                    .transition(DrawableTransitionOptions.withCrossFade(300))
                    .into(this.binding.ivPic);
            } else {
                this.binding.ivPic.setImageResource(R.drawable.placeholder_image_1_1);
            }
        }
    }
}