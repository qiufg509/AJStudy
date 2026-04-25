package com.qiufengguang.ajstudy.activity.markdown;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.data.repository.ArticleDetailRepository;
import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;

/**
 * Markdown页面ViewModel
 *
 * @author qiufengguang
 * @since 2025/12/25 22:22
 */
public class MarkdownModel extends BaseViewModel {
    private final MutableLiveData<String> liveData = new MutableLiveData<>();

    private final ArticleDetailRepository repository;

    public MarkdownModel() {
        repository = ArticleDetailRepository.getInstance();
    }

    public void loadData(String directory) {
        addDisposable(repository.fetchArticleDetailApi(directory)
            .subscribe(liveData::postValue,
                throwable -> liveData.postValue("")
            ));
    }

    public LiveData<String> getLiveData() {
        return liveData;
    }
}