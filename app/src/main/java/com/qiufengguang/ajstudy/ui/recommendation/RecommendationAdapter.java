package com.qiufengguang.ajstudy.ui.recommendation;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.DetailRecommend;
import com.qiufengguang.ajstudy.databinding.ItemRecommendationBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 详情页-推荐子页面适配器
 *
 * @author qiufengguang
 * @since 2025/12/10 0:18
 */
public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder> {

    private List<DetailRecommend> recommends = new ArrayList<>();
    private OnRecommendClickListener clickListener;

    public void setOnRecommendClickListener(OnRecommendClickListener listener) {
        this.clickListener = listener;
    }

    public void setRecommendations(List<DetailRecommend> recommends) {
        if (recommends == null || recommends.isEmpty()) {
            notifyItemRangeRemoved(0, this.recommends.size());
            this.recommends.clear();
        } else {
            this.recommends = recommends;
            notifyItemRangeInserted(0, this.recommends.size());
        }
    }

    @NonNull
    @Override
    public RecommendationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemRecommendationBinding binding = ItemRecommendationBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new RecommendationViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecommendationViewHolder holder, int position) {
        holder.bind(recommends.get(position));
    }

    @Override
    public int getItemCount() {
        return recommends.size();
    }

    public class RecommendationViewHolder extends RecyclerView.ViewHolder {

        private final ItemRecommendationBinding binding;

        public RecommendationViewHolder(ItemRecommendationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DetailRecommend recommend) {
            // 设置应用信息
            binding.tvAppName.setText(recommend.getName());
            binding.tvDescription.setText(recommend.getMemo());
            binding.tvRating.setText(recommend.getScore());
            binding.tvRating.setText(recommend.getScore());
            binding.rbRating.setRating(recommend.getStars());

            Glide.with(binding.getRoot().getContext())
                .load(recommend.getIcon())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.icon_placeholder)
                .into(binding.ivAppIcon);

            // 设置点击事件
            binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(getAdapterPosition(), recommend);
                }
            });

            binding.btnInstall.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onInstallClick(getAdapterPosition(), recommend);
                }
            });
        }
    }


    public interface OnRecommendClickListener {
        void onItemClick(int position, DetailRecommend recommendation);

        void onInstallClick(int position, DetailRecommend recommendation);
    }
}