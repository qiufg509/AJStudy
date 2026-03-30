package com.qiufengguang.ajstudy.activity.ai;

import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.card.chat.AiMessageCard;
import com.qiufengguang.ajstudy.card.chat.UserMessageCard;
import com.qiufengguang.ajstudy.card.welcome.AiWelcomeCard;
import com.qiufengguang.ajstudy.data.base.BaseCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.base.SingleLayoutData;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.model.ChatMessage;
import com.qiufengguang.ajstudy.data.remote.dto.DsRespData;
import com.qiufengguang.ajstudy.data.repository.ChatRepository;
import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

/**
 * Ai对话页面ViewModel
 *
 * @author qiufengguang
 * @since 2026/3/28 18:20
 */
public class AiViewModel extends BaseViewModel {
    private final MutableLiveData<List<LayoutData<?>>> liveData = new MutableLiveData<>();

    private final ChatRepository repository;

    private Call<DsRespData> currentCall;

    public AiViewModel() {
        repository = ChatRepository.getInstance();
        loadData();
    }

    private void loadData() {
        SingleLayoutData<BaseCardBean> stateCardData = LayoutDataFactory.createSingle(
            AiWelcomeCard.LAYOUT_ID, null);
        List<LayoutData<?>> dataList = new ArrayList<>(1);
        dataList.add(stateCardData);
        liveData.setValue(dataList);
    }

    public void sendMessage(@NonNull ChatMessage message) {
        addMessageToList(message);
        currentCall = repository.sendMessage(message, new OnDataLoadedCallback<>() {

            @Override
            public void onSuccess(ChatMessage data) {
                addMessageToList(data);
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private void addMessageToList(@NonNull ChatMessage message) {
        int layoutId = TextUtils.equals(message.getRole(), ChatMessage.ROLE_USER)
            ? UserMessageCard.LAYOUT_ID : AiMessageCard.LAYOUT_ID;
        SingleLayoutData<ChatMessage> userMessageData = LayoutDataFactory.createSingle(
            layoutId, message);
        List<LayoutData<?>> value = liveData.getValue();
        if (value == null || value.isEmpty()) {
            List<LayoutData<?>> dataList = new ArrayList<>(1);
            dataList.add(userMessageData);
            liveData.setValue(dataList);
        } else {
            if (value.size() == 1) {
                LayoutData<?> layoutData = value.get(0);
                if (layoutData.getLayoutId() != UserMessageCard.LAYOUT_ID
                    && layoutData.getLayoutId() != AiMessageCard.LAYOUT_ID) {
                    value.clear();
                }
            }
            value.add(userMessageData);
            liveData.setValue(value);
        }
    }

    public LiveData<List<LayoutData<?>>> getLiveData() {
        return liveData;
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
        }
    }
}