package com.qiufengguang.ajstudy.activity.ai;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.card.welcome.AiWelcomeCard;
import com.qiufengguang.ajstudy.data.base.BaseCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.base.SingleLayoutData;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.data.repository.ChatRepository;
import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
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

    private Call<ResponseBody> currentCall;

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

    public void sendMessage(String msg) {
        currentCall = repository.sendMessage(msg, new OnDataLoadedCallback<>() {
            @Override
            public void onSuccess(String data) {

            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        if (currentCall == null) {
            List<LayoutData<?>> dataList = fetchStateData(State.EMPTY);
            liveData.setValue(dataList);
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