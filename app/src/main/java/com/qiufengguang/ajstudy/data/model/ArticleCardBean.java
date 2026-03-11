package com.qiufengguang.ajstudy.data.model;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

import java.util.Objects;

/**
 * 文章卡片数据bean
 *
 * @author qiufengguang
 * @since 2026/3/11 16:55
 */
public class ArticleCardBean extends BaseCardBean {
    private String content;

    private String imageUrl;

    private String author;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ArticleCardBean that = (ArticleCardBean) o;
        return Objects.equals(content, that.content) && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(author, that.author);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), content, imageUrl, author);
    }
}
