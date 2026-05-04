package com.qiufengguang.ajstudy.data.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.qiufengguang.ajstudy.data.model.Conversation;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;

/**
 * AI会话DAO接口
 *
 * @author qiufengguang
 * @since 2026/4/22 23:52
 */
@Dao
public interface ConversationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Single<Long> insert(Conversation conversation);

    @Query("SELECT * FROM conversation ORDER BY updatedAt DESC")
    LiveData<List<Conversation>> getAllConversationsLive();

    @Query("SELECT * FROM conversation WHERE id = :convId")
    Single<Conversation> getConversationById(long convId);

    @Query("UPDATE conversation SET updatedAt = :timestamp WHERE id = :convId")
    Completable updateConversationTimestamp(long convId, long timestamp);

    @Query("UPDATE conversation SET updatedAt = :timestamp, title = :title WHERE id = :convId")
    Completable updateConversation(long convId, long timestamp, String title);

    @Query("DELETE FROM conversation WHERE id = :convId")
    Completable deleteConversationById(long convId);
}