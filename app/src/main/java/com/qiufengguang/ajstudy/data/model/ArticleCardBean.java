package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;
import com.qiufengguang.ajstudy.global.Constant;

import java.util.List;

/**
 * 文章卡数据bean
 *
 * @author qiufengguang
 * @since 2026/2/7 0:34
 */
public class ArticleCardBean extends BaseCardBean {
    private String author;

    private String avatar;

    private long publishTime;

    private List<Article> articles;

    public ArticleCardBean(String author, String avatar, long publishTime, List<Article> articles) {
        this.author = author;
        this.avatar = avatar;
        this.publishTime = publishTime;
        this.articles = articles;
        setUri(Constant.Data.DETAIL_SPRING);
    }

    public String getAuthor() {
        return author;
    }

    public String getAvatar() {
        return avatar;
    }

    public long getPublishTime() {
        return publishTime;
    }

    public List<Article> getArticles() {
        return articles;
    }

    public static class Article {
        private String title;

        private String imageUrl;

        public Article(String title, String imageUrl) {
            this.title = title;
            this.imageUrl = imageUrl;
        }

        public String getTitle() {
            return title;
        }

        public String getImageUrl() {
            return imageUrl;
        }
    }
}
