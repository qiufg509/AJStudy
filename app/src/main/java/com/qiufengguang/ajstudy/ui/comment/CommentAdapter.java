package com.qiufengguang.ajstudy.ui.comment;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.data.DetailComment;
import com.qiufengguang.ajstudy.databinding.ItemCommentBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * 详情页-评论子页面适配器
 *
 * @author qiufengguang
 * @since 2025/12/10 0:18
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private List<DetailComment> detailComments = new ArrayList<>();

    public void setReviews(List<DetailComment> detailComments) {
        this.detailComments = detailComments != null ? detailComments : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemCommentBinding binding = ItemCommentBinding.inflate(
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

        private final ItemCommentBinding binding;

        public CommentViewHolder(ItemCommentBinding binding) {
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
