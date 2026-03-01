package com.qiufengguang.ajstudy.card.comment;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.model.CommentCardBean;
import com.qiufengguang.ajstudy.databinding.CardCommentBinding;

import java.lang.ref.WeakReference;

/**
 * 评论卡片
 *
 * @author qiufengguang
 * @since 2026/3/1 17:31
 */
public class CommentCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 19;

    private CommentCardBean bean;

    private WeakReference<CardCommentBinding> bindingRef;

    private OnItemClickListener<CommentCardBean> listener;

    private Drawable likeDrawable;

    private Drawable likeFillDrawable;

    private Drawable dislikeDrawable;

    private Drawable dislikeFillDrawable;

    private CommentCard() {
    }

    public void setData(CommentCardBean bean) {
        this.bean = bean;
        this.show();
    }

    public void show() {
        if (this.bean == null) {
            return;
        }
        if (bindingRef == null) {
            return;
        }
        CardCommentBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }

        // 设置用户信息
        binding.tvUserName.setText(bean.getNickName());
        binding.tvDate.setText(bean.getCommentTime());

        // 设置评分
        binding.rbRating.setRating(bean.getStars());

        // 设置评论内容
        binding.tvContent.setText(bean.getCommentInfo());
        updateMyLike(bean);

        Glide.with(binding.getRoot().getContext())
            .load(bean.getAvatar())
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .placeholder(R.drawable.ic_avatar)
            .into(binding.ivAvatar);

        binding.tvLikes.setOnClickListener(v -> {
            if (listener == null) {
                return;
            }
            int likeType = bean.getLikeType();
            int likes = bean.getLikes();
            int dislikes = bean.getDislikes();
            int newType;
            if (likeType == CommentCardBean.TYPE_LIKE) {
                likes--;
                newType = CommentCardBean.TYPE_DEFAULT;
            } else if (likeType == CommentCardBean.TYPE_DISLIKE) {
                likes++;
                dislikes--;
                newType = CommentCardBean.TYPE_LIKE;
            } else {
                likes++;
                newType = CommentCardBean.TYPE_LIKE;
            }
            bean.setLikes(Math.max(0, likes));
            bean.setDislikes(Math.max(0, dislikes));
            bean.setLikeType(newType);
            updateMyLike(bean);
        });
        binding.tvDislikes.setOnClickListener(v -> {
            if (listener == null) {
                return;
            }
            int likeType = bean.getLikeType();
            int likes = bean.getLikes();
            int dislikes = bean.getDislikes();
            int newType;
            if (likeType == CommentCardBean.TYPE_LIKE) {
                likes--;
                dislikes++;
                newType = CommentCardBean.TYPE_DISLIKE;
            } else if (likeType == CommentCardBean.TYPE_DISLIKE) {
                dislikes--;
                newType = CommentCardBean.TYPE_DEFAULT;
            } else {
                dislikes++;
                newType = CommentCardBean.TYPE_DISLIKE;
            }
            bean.setLikes(Math.max(0, likes));
            bean.setDislikes(Math.max(0, dislikes));
            bean.setLikeType(newType);
            updateMyLike(bean);
        });
    }

    public void updateMyLike(CommentCardBean comment) {
        if (bindingRef == null) {
            return;
        }
        CardCommentBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }
        binding.tvLikes.setText(String.valueOf(comment.getLikes()));
        binding.tvDislikes.setText(String.valueOf(comment.getDislikes()));

        int likeType = comment.getLikeType();
        Drawable likeLeft = likeType == CommentCardBean.TYPE_LIKE
            ? this.likeFillDrawable : this.likeDrawable;
        binding.tvLikes.setCompoundDrawablesRelativeWithIntrinsicBounds(likeLeft, null, null, null);
        Drawable dislikeLeft = likeType == CommentCardBean.TYPE_DISLIKE
            ? this.dislikeFillDrawable : this.dislikeDrawable;
        binding.tvDislikes.setCompoundDrawablesRelativeWithIntrinsicBounds(dislikeLeft, null, null, null);
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        if (bindingRef != null) {
            CardCommentBinding binding = bindingRef.get();
            if (binding != null) {
                binding.tvLikes.setOnClickListener(null);
                binding.tvDislikes.setOnClickListener(null);
                bindingRef.clear();
            }
            bindingRef = null;
        }
        bean = null;
        listener = null;
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardCommentBinding binding = CardCommentBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new CommentCardHolder(binding);
        }
    }

    public static class Builder {
        private CardCommentBinding binding;

        private OnItemClickListener<CommentCardBean> listener;

        /**
         * 设置卡片布局viewbinding
         *
         * @param binding CardCommentBinding
         * @ Builder
         */
        public CommentCard.Builder setBinding(CardCommentBinding binding) {
            this.binding = binding;
            return this;
        }

        /**
         * 设置点击监听
         *
         * @param listener {@link OnItemClickListener}
         * @return Builder
         */
        public CommentCard.Builder setListener(OnItemClickListener<CommentCardBean> listener) {
            this.listener = listener;
            return this;
        }

        public CommentCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            CommentCard wrapper = new CommentCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            wrapper.listener = this.listener;
            Context context = this.binding.getRoot().getContext();
            wrapper.likeDrawable = ContextCompat.getDrawable(context, R.drawable.ic_likes);
            wrapper.likeFillDrawable = ContextCompat.getDrawable(context, R.drawable.ic_likes_fill);
            wrapper.dislikeDrawable = ContextCompat.getDrawable(context, R.drawable.ic_dislikes);
            wrapper.dislikeFillDrawable = ContextCompat.getDrawable(context, R.drawable.ic_dislikes_fill);
            return wrapper;
        }
    }
}

