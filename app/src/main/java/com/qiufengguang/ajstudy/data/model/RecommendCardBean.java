package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

import java.util.Objects;

/**
 * 推荐卡片数据bean
 *
 * @author qiufengguang
 * @since 2025/12/12 15:47
 */
public class RecommendCardBean extends BaseCardBean {
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        RecommendCardBean that = (RecommendCardBean) o;
        return Float.compare(stars, that.stars) == 0
            && Objects.equals(icon, that.icon)
            && Objects.equals(name, that.name)
            && Objects.equals(memo, that.memo)
            && Objects.equals(score, that.score);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), icon, name, memo, stars, score);
    }
}
