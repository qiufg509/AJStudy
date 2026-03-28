package com.qiufengguang.ajstudy.fragment.ai;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.data.repository.ChatRepository;
import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * 二级页ViewModel
 *
 * @author qiufengguang
 * @since 2026/1/31 21:53
 */
public class AiViewModel extends BaseViewModel {
    private final MutableLiveData<List<LayoutData<?>>> liveData = new MutableLiveData<>();

    private final ChatRepository repository;

    private Call<ResponseBody> currentCall;

    public AiViewModel() {
        repository = ChatRepository.getInstance();
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