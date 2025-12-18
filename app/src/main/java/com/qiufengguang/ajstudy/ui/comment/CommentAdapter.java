package com.qiufengguang.ajstudy.ui.comment;


import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qiufengguang.ajstudy.R;
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

    private CommentAdapter.OnLikeClickListener clickListener;

    public void setLikeClickListener(OnLikeClickListener likeClickListener) {
        this.clickListener = likeClickListener;
    }

    public void setReviews(List<DetailComment> detailComments) {
        if (detailComments == null || detailComments.isEmpty()) {
            notifyItemRangeRemoved(0, this.detailComments.size());
            this.detailComments.clear();
        } else {
            this.detailComments = detailComments;
            notifyItemRangeInserted(0, this.detailComments.size());
        }
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
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position, @NonNull List<Object> payloads) {
        if (payloads.isEmpty()) {
            // 如果没有payload，执行完整的绑定
            onBindViewHolder(holder, position);
        } else {
            // 根据payload的内容，只更新特定的视图
            for (Object payload : payloads) {
                if (payload instanceof DetailComment) {
                    // 只更新点赞按钮
                    holder.updateMyLike((DetailComment) payload);
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        return detailComments.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private final ItemCommentBinding binding;
        private final Drawable likeDrawable;
        private final Drawable likeFillDrawable;
        private final Drawable dislikeDrawable;
        private final Drawable dislikeFillDrawable;

        public CommentViewHolder(ItemCommentBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            likeDrawable = ContextCompat.getDrawable(this.binding.getRoot().getContext(),
                R.drawable.ic_likes);
            likeFillDrawable = ContextCompat.getDrawable(this.binding.getRoot().getContext(),
                R.drawable.ic_likes_fill);
            dislikeDrawable = ContextCompat.getDrawable(this.binding.getRoot().getContext(),
                R.drawable.ic_dislikes);
            dislikeFillDrawable = ContextCompat.getDrawable(this.binding.getRoot().getContext(),
                R.drawable.ic_dislikes_fill);
        }

        public void bind(DetailComment comment) {
            // 设置用户信息
            binding.tvUserName.setText(comment.getNickName());
            binding.tvDate.setText(comment.getCommentTime());

            // 设置评分
            binding.rbRating.setRating(comment.getStars());

            // 设置评论内容
            binding.tvContent.setText(comment.getCommentInfo());
            updateMyLike(comment);

            Glide.with(binding.getRoot().getContext())
                .load(comment.getAvatar())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.ic_avatar)
                .into(binding.ivAvatar);

            binding.tvLikes.setOnClickListener(v -> {
                if (clickListener == null) {
                    return;
                }
                int likeType = comment.getLikeType();
                int likes = comment.getLikes();
                int dislikes = comment.getDislikes();
                int newType;
                if (likeType == DetailComment.TYPE_LIKE) {
                    likes--;
                    newType = DetailComment.TYPE_DEFAULT;
                } else if (likeType == DetailComment.TYPE_DISLIKE) {
                    likes++;
                    dislikes--;
                    newType = DetailComment.TYPE_LIKE;
                } else {
                    likes++;
                    newType = DetailComment.TYPE_LIKE;
                }
                comment.setLikes(Math.max(0, likes));
                comment.setDislikes(Math.max(0, dislikes));
                comment.setLikeType(newType);
                clickListener.onLikeClick(getAdapterPosition(), comment);
            });
            binding.tvDislikes.setOnClickListener(v -> {
                if (clickListener == null) {
                    return;
                }
                int likeType = comment.getLikeType();
                int likes = comment.getLikes();
                int dislikes = comment.getDislikes();
                int newType;
                if (likeType == DetailComment.TYPE_LIKE) {
                    likes--;
                    dislikes++;
                    newType = DetailComment.TYPE_DISLIKE;
                } else if (likeType == DetailComment.TYPE_DISLIKE) {
                    dislikes--;
                    newType = DetailComment.TYPE_DEFAULT;
                } else {
                    dislikes++;
                    newType = DetailComment.TYPE_DISLIKE;
                }
                comment.setLikes(Math.max(0, likes));
                comment.setDislikes(Math.max(0, dislikes));
                comment.setLikeType(newType);
                clickListener.onLikeClick(getAdapterPosition(), comment);
            });
        }

        public void updateMyLike(DetailComment comment) {
            binding.tvLikes.setText(String.valueOf(comment.getLikes()));
            binding.tvDislikes.setText(String.valueOf(comment.getDislikes()));

            int likeType = comment.getLikeType();
            Drawable likeLeft = likeType == DetailComment.TYPE_LIKE
                ? this.likeFillDrawable : this.likeDrawable;
            binding.tvLikes.setCompoundDrawablesRelativeWithIntrinsicBounds(likeLeft, null, null, null);
            Drawable dislikeLeft = likeType == DetailComment.TYPE_DISLIKE
                ? this.dislikeFillDrawable : this.dislikeDrawable;
            binding.tvDislikes.setCompoundDrawablesRelativeWithIntrinsicBounds(dislikeLeft, null, null, null);
        }
    }

    public interface OnLikeClickListener {
        void onLikeClick(int position, DetailComment comment);
    }
}
