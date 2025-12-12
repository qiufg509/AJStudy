package com.qiufengguang.ajstudy.data;

/**
 * 详情页-推荐子页面数据bean
 *
 * @author qiufengguang
 * @since 2025/12/12 15:47
 */
public class DetailRecommend {
    public static final String LAYOUT_NAME = "horizonhomecard";

    private String icon;

    private String name;

    private String memo;

    private float stars;

    private String score;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public float getStars() {
        return stars;
    }

    public void setStars(float stars) {
        this.stars = stars;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
