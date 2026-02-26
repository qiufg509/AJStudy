package com.qiufengguang.ajstudy.data.model;

import android.text.TextUtils;

/**
 * 详情页-评论子页面数据bean
 *
 * @author qiufengguang
 * @since 2025/12/10 0:18
 */
public class DetailComment {
    public static final String LAYOUT_NAME = "pcscorecommentlistcard";

    public static final int TYPE_LIKE = 1;

    public static final int TYPE_DEFAULT = 0;

    public static final int TYPE_DISLIKE = -1;

    String avatar;

    String commentInfo;

    String commentTime;

    String nickName;

    float stars;

    int likes;

    int dislikes;

    int likeType;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCommentInfo() {
        return commentInfo;
    }

    public void setCommentInfo(String commentInfo) {
        this.commentInfo = commentInfo;
    }

    public String getCommentTime() {
        return commentTime;
    }

    public void setCommentTime(String commentTime) {
        this.commentTime = commentTime;
        this.likes = Math.abs(commentTime.hashCode() % 100);
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public float getStars() {
        return this.stars;
    }

    public void setStars(String stars) {
        if (TextUtils.isEmpty(stars)) {
            this.stars = 0.0f;
        }
        try {
            this.stars = Float.parseFloat(stars);
        } catch (NumberFormatException e) {
            this.stars = 0.0f;
        }
        this.dislikes = Math.abs((int) this.stars + 4);
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getLikes() {
        return likes;
    }

    public void setDislikes(int dislikes) {
        this.dislikes = dislikes;
    }

    public int getDislikes() {
        return dislikes;
    }

    public int getLikeType() {
        return likeType;
    }

    public void setLikeType(int likeType) {
        this.likeType = likeType;
    }
}
