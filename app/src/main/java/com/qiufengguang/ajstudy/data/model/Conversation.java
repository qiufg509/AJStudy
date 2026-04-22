package com.qiufengguang.ajstudy.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

/**
 * 会话数据
 *
 * @author qiufengguang
 * @since 2026/4/22 23:47
 */
@Entity(tableName = "conversation")
public class Conversation {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private long createdAt;
    private long updatedAt;

    public Conversation(String title) {
        this.title = title;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    public long getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(long updatedAt) {
        this.updatedAt = updatedAt;
    }
}