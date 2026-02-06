package com.qiufengguang.ajstudy.fragment.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.banner.BannerCard;
import com.qiufengguang.ajstudy.card.grid.GridCard;
import com.qiufengguang.ajstudy.card.largegraphic.LargeGraphicCard;
import com.qiufengguang.ajstudy.card.largegraphicgrid.LargeGraphicGridCard;
import com.qiufengguang.ajstudy.card.luckywheel.LuckyWheelCard;
import com.qiufengguang.ajstudy.card.series.SeriesCard;
import com.qiufengguang.ajstudy.data.BannerBean;
import com.qiufengguang.ajstudy.data.GridCardBean;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.data.LuckyWheelCardBean;
import com.qiufengguang.ajstudy.data.SeriesCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.base.SingleLayoutData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 首页ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class HomeViewModel extends ViewModel {

    private final MutableLiveData<List<LayoutData<?>>> liveData;

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
        LayoutData<List<BannerBean>> bannerCardData = LayoutDataFactory.createCollection(BannerCard.LAYOUT_ID, bannerBeans);
        LayoutData<List<BannerBean>> bannerCardData2 = LayoutDataFactory.createCollection(BannerCard.LAYOUT_ID, new ArrayList<>(bannerBeans));

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
        LayoutData<List<GridCardBean>> gridCardData = LayoutDataFactory.createCollection(GridCard.LAYOUT_ID, gridCardBeans);

        List<SeriesCardBean> seriesCardBeans = new ArrayList<>();
        seriesCardBeans.add(new SeriesCardBean("与宠物双向治愈，让爱与责任同行", "https://www.foodiesfeed.com/wp-content/uploads/2023/10/girl-holding-takeaway-coffee-cup.jpg", "VIP", "891", "9:51"));
        seriesCardBeans.add(new SeriesCardBean("抓住科技风口，觅得高薪工作", "https://www.foodiesfeed.com/wp-content/uploads/2023/05/juicy-cheeseburger.jpg", "新", "14", "10:51"));
        seriesCardBeans.add(new SeriesCardBean("每天刷新，每天积累", "https://ts2.tc.mm.bing.net/th/id/OIP-C.HF1qwV9btnJwbj4SWnZqogHaE7?rs=1&pid=ImgDetMain&o=7&rm=3", "免费"));
        seriesCardBeans.add(new SeriesCardBean("冬天的 passion 来自滑雪", "https://plus.unsplash.com/premium_photo-1664438942504-cc05d2c80f38?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        seriesCardBeans.add(new SeriesCardBean("用影像讲述她的故事", "https://images.unsplash.com/photo-1768898794985-35c68b2df9b7?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "免费"));
        seriesCardBeans.add(new SeriesCardBean("用这些 APP 探索宇宙", "https://images.unsplash.com/photo-1768325400062-2b63fec226c3?q=80&w=1175&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "VIP", "368", "12:26"));
        seriesCardBeans.add(new SeriesCardBean("让孩子在趣味中获得知识", "https://images.unsplash.com/photo-1761850648640-2ee5870ee883?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        seriesCardBeans.add(new SeriesCardBean("种草人生第一件汉服", "https://www.foodiesfeed.com/wp-content/uploads/2023/03/french-fries-detail.jpg", "免费"));
        seriesCardBeans.add(new SeriesCardBean("珍惜每一口粮食", "https://plus.unsplash.com/premium_photo-1761839920135-4bf781de96e3?q=80&w=685&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D", "66", "17:51"));
        LayoutData<List<SeriesCardBean>> seriesCardData = LayoutDataFactory.createCollection(SeriesCard.LAYOUT_ID, seriesCardBeans, "原创短剧：日常自拍");
        ArrayList<SeriesCardBean> seriesCardBeans2 = new ArrayList<>(seriesCardBeans);
        Collections.reverse(seriesCardBeans2);
        LayoutData<List<SeriesCardBean>> seriesCardData2 = LayoutDataFactory.createCollection(SeriesCard.LAYOUT_ID, seriesCardBeans2, "SERIES 1");

        List<LargeGraphicCardBean> lgcBeans = new ArrayList<>();
        lgcBeans.add(new LargeGraphicCardBean("1秒滑下坡的刺激", "冬天的 passion 来自滑雪", "https://plus.unsplash.com/premium_photo-1664438942504-cc05d2c80f38?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        lgcBeans.add(new LargeGraphicCardBean("欢迎加入爱宠一族", "与宠物双向治愈，让爱与责任同行", "https://www.foodiesfeed.com/wp-content/uploads/2023/10/girl-holding-takeaway-coffee-cup.jpg"));
        LargeGraphicCardBean graphicCardBean = new LargeGraphicCardBean("敲 AI 赛道的门", "抓住科技风口，觅得高薪工作", "https://www.foodiesfeed.com/wp-content/uploads/2023/05/juicy-cheeseburger.jpg");
        lgcBeans.add(graphicCardBean);
        lgcBeans.add(new LargeGraphicCardBean("为妈妈拍个 VLOG", "用影像讲述她的故事", "https://images.unsplash.com/photo-1768898794985-35c68b2df9b7?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        lgcBeans.add(new LargeGraphicCardBean("九天揽星河", "用这些 APP 探索宇宙", "https://images.unsplash.com/photo-1768325400062-2b63fec226c3?q=80&w=1175&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        lgcBeans.add(new LargeGraphicCardBean("边玩边学", "让孩子在趣味中获得知识", "https://images.unsplash.com/photo-1761850648640-2ee5870ee883?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        lgcBeans.add(new LargeGraphicCardBean("跟李子柒见证汉服魅力", "种草人生第一件汉服", "https://www.foodiesfeed.com/wp-content/uploads/2023/03/french-fries-detail.jpg"));
        lgcBeans.add(new LargeGraphicCardBean("从零开始学理财", "每天刷新，每天积累", "https://ts2.tc.mm.bing.net/th/id/OIP-C.HF1qwV9btnJwbj4SWnZqogHaE7?rs=1&pid=ImgDetMain&o=7&rm=3"));
        lgcBeans.add(new LargeGraphicCardBean("一粥一饭，来之不易", "珍惜每一口粮食", "https://plus.unsplash.com/premium_photo-1761839920135-4bf781de96e3?q=80&w=685&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
        SingleLayoutData<LargeGraphicCardBean> graphicCardData = LayoutDataFactory.createSingle(LargeGraphicCard.LAYOUT_ID, graphicCardBean);
        LayoutData<List<LargeGraphicCardBean>> lgcData = LayoutDataFactory.createCollection(LargeGraphicGridCard.LAYOUT_ID, lgcBeans, "时下热门");

        List<LuckyWheelCardBean> wheelCardBeans = List.of(
            new LuckyWheelCardBean("炖猪脚", "https://ts2.tc.mm.bing.net/th/id/OIP-C.zNBQPxhzp8IBnvdl3K589AHaGd?rs=1&pid=ImgDetMain&o=7&rm=3", "#7B35D1"),
            new LuckyWheelCardBean("五指毛桃鸡汤", "https://ts1.tc.mm.bing.net/th/id/OIP-C.i3kkhgHNTFZStre1RHsFlQAAAA?rs=1&pid=ImgDetMain&o=7&rm=3", "#1F59CE"),
            new LuckyWheelCardBean("紫苏鱼", "https://ts1.tc.mm.bing.net/th/id/OIP-C.KKjSIjdKkoFVnt2Lpgys5wHaGH?rs=1&pid=ImgDetMain&o=7&rm=3", "#3F90BB"),
            new LuckyWheelCardBean("辣椒炒鱿鱼", "https://ts3.tc.mm.bing.net/th/id/OIP-C.h9nbcn5v25Nib_e5GEdvqAHaFy?rs=1&pid=ImgDetMain&o=7&rm=3", "#82AF48"),
            new LuckyWheelCardBean("肉末茄子", "https://ts4.tc.mm.bing.net/th/id/OIP-C.3tPKliS241LpwDCLwY4s6AHaHa?rs=1&pid=ImgDetMain&o=7&rm=3", "#EB6F3E"),
            new LuckyWheelCardBean("蒜香黑鱼片", "https://ts3.tc.mm.bing.net/th/id/OIP-C.Z63O5HeuJDQBwSmWXihhSgHaJ4?rs=1&pid=ImgDetMain&o=7&rm=3", "#D63339")
        );
        LayoutData<List<LuckyWheelCardBean>> wheelCardData = LayoutDataFactory.createCollection(LuckyWheelCard.LAYOUT_ID, wheelCardBeans, "今天吃什么？");

        List<LayoutData<?>> dataList = new ArrayList<>();
        dataList.add(bannerCardData);
        dataList.add(gridCardData);
        dataList.add(seriesCardData);
        dataList.add(wheelCardData);
        dataList.add(graphicCardData);
        dataList.add(seriesCardData2);
        dataList.add(bannerCardData2);
        dataList.add(lgcData);
        liveData.setValue(dataList);
    }

    public LiveData<List<LayoutData<?>>> getLiveData() {
        return liveData;
    }
}