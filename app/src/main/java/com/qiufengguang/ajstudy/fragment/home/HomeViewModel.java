package com.qiufengguang.ajstudy.fragment.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.BannerBean;
import com.qiufengguang.ajstudy.data.BaseCardBean;
import com.qiufengguang.ajstudy.data.GridCardBean;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.data.SeriesCardBean;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<List<? extends BaseCardBean>>> liveData;

    public HomeViewModel() {
        liveData = new MutableLiveData<>();
        initData();
    }

    private void initData() {
        List<BannerBean> bannerBeans = new ArrayList<>();
        bannerBeans.add(new BannerBean("解压神器", "https://appimg-drcn.dbankcdn.com/entranceIcon/72e6129b8f7444a399d91c875d15c620/64/1595299825119_1080x684.jpg"));
        bannerBeans.add(new BannerBean("多读科普涨知识", "https://appimg-drcn.dbankcdn.com/entranceIcon/9a3d003858bc47969d2bb461a2c11b12/64/1595299811024_1080x684.jpg"));
        bannerBeans.add(new BannerBean("手工客，感受匠心之美", "https://appimg-drcn.dbankcdn.com/entranceIcon/f2d2895630c14be28306aa5466e76754/64/1595299797898_1080x684.jpg"));
        bannerBeans.add(new BannerBean("掌上的生活管家", "https://appimg-drcn.dbankcdn.com/entranceIcon/74ddfa53ce2244848e4b4c61471a7353/64/1595299783266_1080x684.jpg"));

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

        List<SeriesCardBean> seriesCardBeans = new ArrayList<>();
        seriesCardBeans.add(new SeriesCardBean("与宠物双向治愈，让爱与责任同行", "https://www.foodiesfeed.com/wp-content/uploads/2023/10/girl-holding-takeaway-coffee-cup.jpg"));
        seriesCardBeans.add(new SeriesCardBean("抓住科技风口，觅得高薪工作", "https://www.foodiesfeed.com/wp-content/uploads/2023/05/juicy-cheeseburger.jpg"));
        seriesCardBeans.add(new SeriesCardBean("每天刷新，每天积累", "https://images.unsplash.com/photo-1768924401996-4c8d79462660?q=80&w=1102&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        seriesCardBeans.add(new SeriesCardBean("冬天的 passion 来自滑雪", "https://plus.unsplash.com/premium_photo-1664438942504-cc05d2c80f38?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        seriesCardBeans.add(new SeriesCardBean("用影像讲述她的故事", "https://images.unsplash.com/photo-1768898794985-35c68b2df9b7?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        seriesCardBeans.add(new SeriesCardBean("用这些 APP 探索宇宙", "https://images.unsplash.com/photo-1768325400062-2b63fec226c3?q=80&w=1175&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        seriesCardBeans.add(new SeriesCardBean("让孩子在趣味中获得知识", "https://images.unsplash.com/photo-1761850648640-2ee5870ee883?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        seriesCardBeans.add(new SeriesCardBean("种草人生第一件汉服", "https://www.foodiesfeed.com/wp-content/uploads/2023/03/french-fries-detail.jpg"));
        seriesCardBeans.add(new SeriesCardBean("珍惜每一口粮食", "https://plus.unsplash.com/premium_photo-1761839920135-4bf781de96e3?q=80&w=685&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));

        List<LargeGraphicCardBean> lgcBeans = new ArrayList<>();
        lgcBeans.add(new LargeGraphicCardBean("欢迎加入爱宠一族", "与宠物双向治愈，让爱与责任同行", "https://www.foodiesfeed.com/wp-content/uploads/2023/10/girl-holding-takeaway-coffee-cup.jpg"));
        lgcBeans.add(new LargeGraphicCardBean("敲 AI 赛道的门", "抓住科技风口，觅得高薪工作", "https://www.foodiesfeed.com/wp-content/uploads/2023/05/juicy-cheeseburger.jpg"));
        lgcBeans.add(new LargeGraphicCardBean("从零开始学理财", "每天刷新，每天积累", "https://images.unsplash.com/photo-1768924401996-4c8d79462660?q=80&w=1102&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        lgcBeans.add(new LargeGraphicCardBean("1秒滑下坡的刺激", "冬天的 passion 来自滑雪", "https://plus.unsplash.com/premium_photo-1664438942504-cc05d2c80f38?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        lgcBeans.add(new LargeGraphicCardBean("为妈妈拍个 VLOG", "用影像讲述她的故事", "https://images.unsplash.com/photo-1768898794985-35c68b2df9b7?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        lgcBeans.add(new LargeGraphicCardBean("九天揽星河", "用这些 APP 探索宇宙", "https://images.unsplash.com/photo-1768325400062-2b63fec226c3?q=80&w=1175&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        lgcBeans.add(new LargeGraphicCardBean("边玩边学", "让孩子在趣味中获得知识", "https://images.unsplash.com/photo-1761850648640-2ee5870ee883?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        lgcBeans.add(new LargeGraphicCardBean("跟李子柒见证汉服魅力", "种草人生第一件汉服", "https://www.foodiesfeed.com/wp-content/uploads/2023/03/french-fries-detail.jpg"));
        lgcBeans.add(new LargeGraphicCardBean("一粥一饭，来之不易", "珍惜每一口粮食", "https://plus.unsplash.com/premium_photo-1761839920135-4bf781de96e3?q=80&w=685&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));

        List<List<? extends BaseCardBean>> dataList = new ArrayList<>();
        dataList.add(bannerBeans);
        dataList.add(gridCardBeans);
        dataList.add(seriesCardBeans);
        dataList.add(lgcBeans);
        dataList.add(new ArrayList<>(seriesCardBeans));
        dataList.add(new ArrayList<>(lgcBeans));
        liveData.setValue(dataList);
    }

    public LiveData<List<List<? extends BaseCardBean>>> getLiveData() {
        return liveData;
    }
}