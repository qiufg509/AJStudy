package com.qiufengguang.ajstudy.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.data.BannerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<BannerBean>> liveData;

    public HomeViewModel() {
        liveData = new MutableLiveData<>();
        initData();
    }

    private void initData() {
        List<BannerBean> items = new ArrayList<>();
        items.add(new BannerBean("解压神器", "https://appimg-drcn.dbankcdn.com/entranceIcon/72e6129b8f7444a399d91c875d15c620/64/1595299825119_1080x684.jpg"));
        items.add(new BannerBean("多读科普涨知识", "https://appimg-drcn.dbankcdn.com/entranceIcon/9a3d003858bc47969d2bb461a2c11b12/64/1595299811024_1080x684.jpg"));
        items.add(new BannerBean("手工客，感受匠心之美", "https://appimg-drcn.dbankcdn.com/entranceIcon/f2d2895630c14be28306aa5466e76754/64/1595299797898_1080x684.jpg"));
        items.add(new BannerBean("掌上的生活管家", "https://appimg-drcn.dbankcdn.com/entranceIcon/74ddfa53ce2244848e4b4c61471a7353/64/1595299783266_1080x684.jpg"));
        liveData.setValue(items);
    }

    public LiveData<List<BannerBean>> getLiveData() {
        return liveData;
    }
}