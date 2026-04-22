package com.qiufengguang.ajstudy.data.repository;

import androidx.lifecycle.LiveData;

import com.qiufengguang.ajstudy.data.dao.ChatMessageDao;
import com.qiufengguang.ajstudy.data.dao.ConversationDao;
import com.qiufengguang.ajstudy.data.database.AppDatabase;
import com.qiufengguang.ajstudy.data.model.ChatMessage;
import com.qiufengguang.ajstudy.data.model.Conversation;
import com.qiufengguang.ajstudy.data.remote.api.DeepSeekApi;
import com.qiufengguang.ajstudy.data.remote.dto.ChatCompletionRequest;
import com.qiufengguang.ajstudy.data.remote.service.DeepSeekClient;

import java.util.Collections;
import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * AI 业务统一仓库
 * [性能专家重构]：合并会话管理与消息管理，提供统一的本地+远程数据入口
 *
 * @author qiufengguang
 * @since 2026/4/22 23:48
 */
public class AiRepository {
    private static volatile AiRepository instance;

    private final DeepSeekApi api;
    private final ChatMessageDao messageDao;
    private final ConversationDao conversationDao;

    private AiRepository() {
        AppDatabase db = AppDatabase.getInstance();
        this.api = DeepSeekClient.getDeepSeekApi();
        this.messageDao = db.chatMessageDao();
        this.conversationDao = db.conversationDao();
    }

    public static AiRepository getInstance() {
        if (instance == null) {
            synchronized (AiRepository.class) {
                if (instance == null) {
                    instance = new AiRepository();
                }
            }
        }
        return instance;
    }

    // ========== 会话管理 ==========

    public Single<Long> createNewConversation(String title) {
        return conversationDao.insert(new Conversation(title));
    }

    public LiveData<List<Conversation>> getAllConversations() {
        return conversationDao.getAllConversationsLive();
    }

    // ========== 消息管理 ==========

    public LiveData<List<ChatMessage>> getMessagesLive(long conversationId) {
        return messageDao.getMessagesByConversationIdLive(conversationId);
    }

    /**
     * 保存消息并更新会话活跃时间
     */
    public Completable saveMessage(ChatMessage message) {
        return messageDao.insert(message)
            .andThen(conversationDao.updateConversation(message.getConversationId(), System.currentTimeMillis(), null));
    }

    /**
     * 远程 AI 请求
     */
    public Single<ChatMessage> requestAiReply(long conversationId, String content) {
        // [逻辑优化]：当前简单传单条，后续可扩展为从 DB 读取历史消息作为 Context
        ChatMessage userMsg = new ChatMessage(conversationId, ChatMessage.ROLE_USER, content);
        ChatCompletionRequest request = new ChatCompletionRequest(Collections.singletonList(userMsg));
        
        return api.chatCompletion(request)
            .subscribeOn(Schedulers.io())
            .map(response -> {
                if (response.getChoices() != null && !response.getChoices().isEmpty()) {
                    String reply = response.getChoices().get(0).getMessage().getContent();
                    return new ChatMessage(conversationId, ChatMessage.ROLE_ASSISTANT, reply);
                }
                throw new RuntimeException("AI 响应为空");
            });
    }

    public void clear() {
        // 预留清理接口
    }
}
