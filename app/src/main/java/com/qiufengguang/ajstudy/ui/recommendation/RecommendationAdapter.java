package com.qiufengguang.ajstudy.ui.recommendation;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.activity.detail.DetailViewModel;
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

    private List<DetailViewModel.Recommendation> recommendationList = new ArrayList<>();
    private OnItemClickListener onItemClickListener;
    private OnInstallClickListener onInstallClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position, DetailViewModel.Recommendation recommendation);
    }

    public interface OnInstallClickListener {
        void onInstallClick(int position, DetailViewModel.Recommendation recommendation);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public void setOnInstallClickListener(OnInstallClickListener listener) {
        this.onInstallClickListener = listener;
    }

    public void setRecommendations(List<DetailViewModel.Recommendation> recommendationList) {
        this.recommendationList = recommendationList != null ? recommendationList : new ArrayList<>();
        notifyDataSetChanged();
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
        holder.bind(recommendationList.get(position));
    }

    @Override
    public int getItemCount() {
        return recommendationList.size();
    }

    public class RecommendationViewHolder extends RecyclerView.ViewHolder {

        private final ItemRecommendationBinding binding;

        public RecommendationViewHolder(ItemRecommendationBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DetailViewModel.Recommendation recommendation) {
            // 设置应用信息
            binding.tvAppName.setText(recommendation.getAppName());
            binding.tvDescription.setText(recommendation.getDescription());
            binding.tvRating.setText(String.valueOf(recommendation.getRating()));
            binding.tvInstalls.setText(recommendation.getInstalls());

            // 设置评分
            binding.rbRating.setRating(recommendation.getRating());

            // 设置应用图标
            binding.ivAppIcon.setImageResource(recommendation.getIconResId());

            // 设置点击事件
            binding.getRoot().setOnClickListener(v -> {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(getAdapterPosition(), recommendation);
                }
            });

            binding.btnInstall.setOnClickListener(v -> {
                if (onInstallClickListener != null) {
                    onInstallClickListener.onInstallClick(getAdapterPosition(), recommendation);
                }
            });
        }
    }
}