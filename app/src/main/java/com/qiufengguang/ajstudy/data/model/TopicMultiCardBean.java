package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

import java.util.List;
import java.util.Objects;

/**
 * 多主题聚合卡片数据bean
 *
 * @author qiufengguang
 * @since 2026/2/7 0:34
 */
public class TopicMultiCardBean extends BaseCardBean {
    private String author;

    private String avatar;

    private long publishTime;

    private List<Article> articles;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(long publishTime) {
        this.publishTime = publishTime;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public void setArticles(List<Article> articles) {
        this.articles = articles;
    }

    public static class Article extends BaseCardBean {
        private String imageUrl;

        public String getImageUrl() {
            return imageUrl;
        }

        public void setImageUrl(String imageUrl) {
            this.imageUrl = imageUrl;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            if (!super.equals(o)) {
                return false;
            }
            Article article = (Article) o;
            return Objects.equals(imageUrl, article.imageUrl);
        }

        @Override
        public int hashCode() {
            return Objects.hash(super.hashCode(), imageUrl);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TopicMultiCardBean that = (TopicMultiCardBean) o;
        return publishTime == that.publishTime
            && Objects.equals(author, that.author)
            && Objects.equals(avatar, that.avatar)
            && Objects.equals(articles, that.articles);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), author, avatar, publishTime, articles);
    }
}
