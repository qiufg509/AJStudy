package com.qiufengguang.ajstudy.activity.markdown;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.data.callback.OnDataLoadedCallback;
import com.qiufengguang.ajstudy.data.repository.ArticleDetailRepository;

import okhttp3.ResponseBody;
import retrofit2.Call;

/**
 * Markdown页面ViewModel
 *
 * @author qiufengguang
 * @since 2025/12/25 22:22
 */
public class MarkdownModel extends ViewModel {
    private final MutableLiveData<String> liveData = new MutableLiveData<>();

    private final ArticleDetailRepository repository;

    private Call<ResponseBody> currentCall;

    public MarkdownModel() {
        repository = ArticleDetailRepository.getInstance();
    }

    public void loadData(String directory) {
        if (currentCall != null && !currentCall.isCanceled()) {
            currentCall.cancel();
        }

        currentCall = repository.fetchArticleDetailApi(directory, new OnDataLoadedCallback<>() {
            @Override
            public void onSuccess(String data) {
                liveData.postValue(data);
            }

            @Override
            public void onFailure(Throwable t) {
                liveData.postValue("");
            }
        });
    }

    public LiveData<String> getLiveData() {
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
