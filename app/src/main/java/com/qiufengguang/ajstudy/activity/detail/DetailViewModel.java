package com.qiufengguang.ajstudy.activity.detail;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.DetailAppData;
import com.qiufengguang.ajstudy.data.model.DetailHead;
import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.data.model.TabData;
import com.qiufengguang.ajstudy.data.repository.AppDetailRepository;
import com.qiufengguang.ajstudy.fragment.base.BaseViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * 详情页ViewModel
 *
 * @author qiufengguang
 * @since 2025/12/10 0:18
 */
public class DetailViewModel extends BaseViewModel {
    private static final String TAG = "DetailViewModel";

    /**
     * 头部信息
     */
    private final MutableLiveData<DetailHead> detailHead = new MutableLiveData<>();

    /**
     * 应用信息
     */
    private final MutableLiveData<DetailAppData> appData = new MutableLiveData<>();

    /**
     * tab
     */
    private final MutableLiveData<List<TabData>> tabData = new MutableLiveData<>();

    /**
     * 评论信息
     */
    private final MutableLiveData<List<LayoutData<?>>> comments = new MutableLiveData<>();

    /**
     * 推荐列表
     */
    private final MutableLiveData<List<LayoutData<?>>> recommendations = new MutableLiveData<>();

    /**
     * 介绍信息
     */
    private final MutableLiveData<List<LayoutData<?>>> introduction = new MutableLiveData<>();

    /**
     * 默认选中评论tab
     */
    private final MutableLiveData<Integer> selectedTab = new MutableLiveData<>(1);

    private final AppDetailRepository repository;

    public DetailViewModel() {
        repository = AppDetailRepository.getInstance();
    }

    public LiveData<DetailHead> getDetailHead() {
        return detailHead;
    }

    public LiveData<DetailAppData> getAppData() {
        return appData;
    }

    public LiveData<List<TabData>> getTabData() {
        return tabData;
    }

    public LiveData<List<LayoutData<?>>> getComments() {
        return comments;
    }

    public LiveData<List<LayoutData<?>>> getRecommendations() {
        return recommendations;
    }

    public LiveData<List<LayoutData<?>>> getIntroduction() {
        return introduction;
    }

    public LiveData<Integer> getSelectedTab() {
        return selectedTab;
    }

    public void setSelectedTab(int position) {
        selectedTab.setValue(position);
    }

    public void loadData(String directory) {
        addDisposable(repository.fetchAppDetailData(directory)
            .subscribe(data -> {
                    tabData.postValue(data.getTabs());
                    List<LayoutData<?>> dataList = data.getLayoutData();
                    if (dataList == null || dataList.isEmpty()) {
                        return;
                    }
                    List<LayoutData<?>> list = new ArrayList<>(dataList.size());
                    for (LayoutData<?> layoutData : dataList) {
                        if (layoutData.getLayoutId() == DetailHead.LAYOUT_ID && !layoutData.isCollection()) {
                            detailHead.postValue((DetailHead) layoutData.getData());
                        } else if (layoutData.getLayoutId() == DetailAppData.LAYOUT_ID && !layoutData.isCollection()) {
                            appData.postValue((DetailAppData) layoutData.getData());
                        } else {
                            list.add(layoutData);
                        }
                    }
                    introduction.postValue(list);
                },
                throwable -> {
                    List<LayoutData<?>> dataList = fetchStateData(State.ERROR);
                    introduction.postValue(dataList);
                }
            ));
    }

    public void loadCommentData(String directory) {
        addDisposable(repository.fetchCommentData(directory)
            .subscribe(data -> comments.postValue(data.getLayoutData()),
                throwable -> {
                    List<LayoutData<?>> dataList = fetchStateData(State.ERROR);
                    comments.postValue(dataList);
                }
            ));
    }

    public void loadRecommendData(String directory) {
        addDisposable(repository.fetchRecommendData(directory)
            .subscribe(data -> recommendations.postValue(data.getLayoutData()),
                throwable -> {
                    List<LayoutData<?>> dataList = fetchStateData(State.ERROR);
                    recommendations.postValue(dataList);
                }
            ));
    }
}