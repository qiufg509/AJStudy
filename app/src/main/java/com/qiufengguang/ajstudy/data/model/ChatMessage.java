package com.qiufengguang.ajstudy.data.model;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

import java.util.Objects;

/**
 * Ai对话消息
 *
 * @author qiufengguang
 * @since 2026/3/28 18:20
 */
@Entity(
    tableName = "chat_message",
    foreignKeys = @ForeignKey(entity = Conversation.class,
        parentColumns = "id",
        childColumns = "conversationId",
        // 删除会话时删除消息
        onDelete = ForeignKey.CASCADE
    ),
    // 加速按会话查询
    indices = {@Index("conversationId")})
public class ChatMessage extends BaseCardBean {
    public static final String ROLE_USER = "user";

    public static final String ROLE_ASSISTANT = "assistant";

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long conversationId;

    /**
     * "user" 或 "assistant"
     */
    private String role;
    private String content;

    private long createdAt;

    private long updatedAt;


    public ChatMessage(long conversationId, String role, String content) {
        this.conversationId = conversationId;
        this.role = role;
        this.content = content;
        this.createdAt = System.currentTimeMillis();
        this.updatedAt = System.currentTimeMillis();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        ChatMessage message = (ChatMessage) o;
        return id == message.id
            && conversationId == message.conversationId
            && createdAt == message.createdAt
            && updatedAt == message.updatedAt
            && Objects.equals(role, message.role)
            && Objects.equals(content, message.content);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, conversationId, role, content, createdAt, updatedAt);
    }
}
