package com.qiufengguang.ajstudy.data;

import android.text.TextUtils;

/**
 * 详情页-评论子页面数据bean
 *
 * @author qiufengguang
 * @since 2025/12/10 0:18
 */
public class DetailComment {
    public static final String LAYOUT_NAME = "pcscorecommentlistcard";

    String avatar;

    String commentInfo;

    String commentTime;

    String nickName;

    String stars;

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
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public float getStars() {
        if (TextUtils.isEmpty(stars)) {
            return 0.0f;
        }
        try {
            return Float.parseFloat(stars);
        } catch (NumberFormatException e) {
            return 0.0f;
        }
    }

    public void setStars(String stars) {
        this.stars = stars;
    }
}
