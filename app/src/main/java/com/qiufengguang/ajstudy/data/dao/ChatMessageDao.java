// data/dao/NoteDao.java
package com.qiufengguang.ajstudy.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.qiufengguang.ajstudy.data.model.ChatMessage;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

/**
 * AI对话消息DAO接口
 *
 * @author qiufengguang
 * @since 2026/3/31 16:09
 */
@Dao
public interface ChatMessageDao {

    // ========== 插入操作 ==========

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insert(ChatMessage message);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Completable insertAll(List<ChatMessage> messages);

    // ========== 更新操作 ==========

    @Update
    Completable update(ChatMessage messages);

    // ========== 删除操作 ==========

    @Delete
    Completable delete(ChatMessage messages);

    @Query("DELETE FROM chat_message WHERE id = :id")
    Completable deleteById(long id);

    @Query("DELETE FROM chat_message")
    Completable deleteAll();


    // 删除指定会话的所有消息
    @Query("DELETE FROM chat_message WHERE conversationId = :convId")
    Completable deleteMessagesByConversationId(long convId);

    // ========== 查询操作（一次性读取）==========

    @Query("SELECT * FROM chat_message WHERE id = :id")
    Single<ChatMessage> getChatMessageById(long id);

    @Query("SELECT * FROM chat_message ORDER BY createdAt DESC")
    Single<List<ChatMessage>> getAllChatMessagesOnce();

    // ========== 查询操作（可观察响应式）==========

    @Query("SELECT * FROM chat_message ORDER BY createdAt DESC")
    LiveData<List<ChatMessage>> getAllChatMessagesLive();

    @Query("SELECT * FROM chat_message ORDER BY createdAt DESC")
    Flowable<List<ChatMessage>> getAllChatMessagesFlowable();

    // ========== 条件查询 ==========

    @Query("SELECT * FROM chat_message WHERE role LIKE '%' || :role || '%' OR content LIKE '%' || :content || '%' ORDER BY createdAt DESC")
    LiveData<List<ChatMessage>> searchChatMessages(String role, String content);

    // 按会话ID查询消息（按时间升序，正常对话顺序）
    @Query("SELECT * FROM chat_message WHERE conversationId = :convId ORDER BY createdAt ASC")
    LiveData<List<ChatMessage>> getMessagesByConversationIdLive(long convId);

    @Query("SELECT * FROM chat_message WHERE conversationId = :convId ORDER BY createdAt ASC")
    Single<List<ChatMessage>> getMessagesByConversationIdOnce(long convId);

    // ========== 统计 ==========

    @Query("SELECT COUNT(*) FROM chat_message")
    Single<Integer> getChatMessageCount();
}