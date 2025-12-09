package com.qiufengguang.ajstudy.activity.detail;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.data.DetailComment;
import com.qiufengguang.ajstudy.databinding.ItemReviewBinding;

import java.util.ArrayList;
import java.util.List;

public class DetailCommentAdapter extends RecyclerView.Adapter<DetailCommentAdapter.CommentViewHolder> {

    private List<DetailComment> detailComments = new ArrayList<>();

    public void setReviews(List<DetailComment> detailComments) {
        this.detailComments = detailComments != null ? detailComments : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReviewBinding binding = ItemReviewBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new CommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bind(detailComments.get(position));
    }

    @Override
    public int getItemCount() {
        return detailComments.size();
    }

    public static class CommentViewHolder extends RecyclerView.ViewHolder {

        private final ItemReviewBinding binding;

        public CommentViewHolder(ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DetailComment comment) {
            // 设置用户信息
            binding.tvUserName.setText(comment.getNickName());
            binding.tvDate.setText(comment.getCommentTime());

            // 设置评分
            binding.rbRating.setRating(comment.getStars());

            // 设置评论内容
            binding.tvContent.setText(comment.getCommentInfo());
        }
    }
}
