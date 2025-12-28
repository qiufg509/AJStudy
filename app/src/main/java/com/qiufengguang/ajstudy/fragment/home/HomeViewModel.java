package com.qiufengguang.ajstudy.fragment.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.BannerBean;
import com.qiufengguang.ajstudy.data.GridCardBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<BannerBean>> bannerLiveData;
    private final MutableLiveData<List<GridCardBean>> gridCardLiveData;

    public HomeViewModel() {
        bannerLiveData = new MutableLiveData<>();
        gridCardLiveData = new MutableLiveData<>();
        initData();
    }

    private void initData() {
        List<BannerBean> items = new ArrayList<>();
        items.add(new BannerBean("解压神器", "https://appimg-drcn.dbankcdn.com/entranceIcon/72e6129b8f7444a399d91c875d15c620/64/1595299825119_1080x684.jpg"));
        items.add(new BannerBean("多读科普涨知识", "https://appimg-drcn.dbankcdn.com/entranceIcon/9a3d003858bc47969d2bb461a2c11b12/64/1595299811024_1080x684.jpg"));
        items.add(new BannerBean("手工客，感受匠心之美", "https://appimg-drcn.dbankcdn.com/entranceIcon/f2d2895630c14be28306aa5466e76754/64/1595299797898_1080x684.jpg"));
        items.add(new BannerBean("掌上的生活管家", "https://appimg-drcn.dbankcdn.com/entranceIcon/74ddfa53ce2244848e4b4c61471a7353/64/1595299783266_1080x684.jpg"));
        bannerLiveData.setValue(items);

        List<GridCardBean> gridCardBeans = new ArrayList<>();
        gridCardBeans.add(new GridCardBean("Advance", R.drawable.ic_book_0, "AdavancedPart"));
        gridCardBeans.add(new GridCardBean("alg.", R.drawable.ic_book_1, "Algorithm"));
        gridCardBeans.add(new GridCardBean("AS", R.drawable.ic_book_2, "AndroidStudioCourse"));
        gridCardBeans.add(new GridCardBean("Architect", R.drawable.ic_book_3, "Architect"));
        gridCardBeans.add(new GridCardBean("Dagger2", R.drawable.ic_book_4, "Dagger2"));
        gridCardBeans.add(new GridCardBean("Basic", R.drawable.ic_book_5, "BasicKnowledge"));
        gridCardBeans.add(new GridCardBean("Java", R.drawable.ic_book_6, "JavaKnowledge"));
        gridCardBeans.add(new GridCardBean("Jetpack", R.drawable.ic_book_7, "Jetpack"));
        gridCardBeans.add(new GridCardBean("Kotlin", R.drawable.ic_book_8, "KotlinCourse"));
        gridCardBeans.add(new GridCardBean("RxJava", R.drawable.ic_book_9, "RxJavaPart"));
        gridCardLiveData.setValue(gridCardBeans);
    }

    public LiveData<List<BannerBean>> getBannerLiveData() {
        return bannerLiveData;
    }

    public LiveData<List<GridCardBean>> getGridCardLiveData() {
        return gridCardLiveData;
    }
}