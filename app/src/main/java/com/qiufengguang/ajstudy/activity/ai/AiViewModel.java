package com.qiufengguang.ajstudy.activity.ai;

import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.qiufengguang.ajstudy.card.chat.AiMessageCard;
import com.qiufengguang.ajstudy.card.chat.UserMessageCard;
import com.qiufengguang.ajstudy.card.welcome.AiWelcomeCard;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.model.ChatMessage;
import com.qiufengguang.ajstudy.data.repository.AiRepository;
import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * Ai对话页面ViewModel
 * [高级开发重构]：实现基于 Room LiveData 的全自动响应式消息刷新，彻底消除手动维护列表的复杂度。
 *
 * @author qiufengguang
 * @since 2026/3/28 18:20
 */
public class AiViewModel extends BaseViewModel {
    private final MutableLiveData<Long> currentConversationId = new MutableLiveData<>();

    // 暴露给 Fragment 观察的响应式 LiveData
    private final LiveData<List<LayoutData<?>>> chatMessageLive;

    private final AiRepository repository;

    public AiViewModel() {
        repository = AiRepository.getInstance();

        // ✅ [架构精华]：建立响应式管道。只要 currentConversationId 变化或数据库消息更新，UI 都会自动刷新。
        chatMessageLive = Transformations.switchMap(currentConversationId, id -> {
            if (id == null || id <= 0) {
                // 初始状态或新建会话：返回展示欢迎卡片的 LiveData
                return new MutableLiveData<>(Collections.singletonList(
                    LayoutDataFactory.createSingle(AiWelcomeCard.LAYOUT_ID, null)));
            }
            // 既有会话：实时监听数据库中该会话的消息变化
            return Transformations.map(repository.getMessagesLive(id), messages ->
                messages.stream().map(message -> {
                    int layoutId = TextUtils.equals(message.getRole(), ChatMessage.ROLE_USER)
                        ? UserMessageCard.LAYOUT_ID : AiMessageCard.LAYOUT_ID;
                    return LayoutDataFactory.createSingle(layoutId, message);
                }).collect(Collectors.toList()));
        });

        // 默认进入新对话模式
        startNewConversation();
    }

    public LiveData<List<LayoutData<?>>> getChatMessageLive() {
        return chatMessageLive;
    }

    /**
     * 开启新会话：重置 ID 即可触发响应式管道切回欢迎页
     */
    public void startNewConversation() {
        currentConversationId.setValue(null);
    }

    /**
     * 加载历史会话：设置 ID 后，响应式管道会自动从数据库加载历史记录
     */
    public void loadConversationData(long convId) {
        currentConversationId.setValue(convId);
    }

    /**
     * 发送消息核心逻辑：处理会话创建与本地/远程双向保存
     */
    public void sendMessage(String content) {
        if (TextUtils.isEmpty(content)) return;

        Long convId = currentConversationId.getValue();
        if (convId == null || convId <= 0) {
            // 首次发送：异步创建会话（标题取首条消息内容）
            addDisposable(repository.createNewConversation(content)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(newId -> {
                    currentConversationId.setValue(newId);
                    saveAndRequestReply(newId, content);
                }, Throwable::printStackTrace));
        } else {
            saveAndRequestReply(convId, content);
        }
    }

    private void saveAndRequestReply(long convId, String content) {
        ChatMessage userMsg = new ChatMessage(convId, ChatMessage.ROLE_USER, content);
        // ✅ 步骤 1：保存用户消息入库。存入后，chatMessageLiveData 会自动推送更新，UI 立即显示用户气泡。
        addDisposable(repository.saveMessage(userMsg)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(() -> fetchAiResponse(convId, content), Throwable::printStackTrace));
    }

    private void fetchAiResponse(long convId, String content) {
        // ✅ 步骤 2：请求 AI 回复，并将其保存到数据库。
        // 保存后，Room 自动触发 chatMessageLiveData 下发最新列表，UI 自动显示 AI 气泡。
        addDisposable(repository.requestAiReply(convId, content)
            .flatMapCompletable(repository::saveMessage)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(() -> {
                // 成功保存后可以处理如播放音效等逻辑
            }, throwable -> {
                // 错误处理：存入一条模拟 AI 的错误提示，UI 也会自动显示
                ChatMessage errorMsg = new ChatMessage(convId, ChatMessage.ROLE_ASSISTANT, "抱歉，网络开小差了...");
                addDisposable(repository.saveMessage(errorMsg).subscribe());
            }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        repository.clear();
    }
}