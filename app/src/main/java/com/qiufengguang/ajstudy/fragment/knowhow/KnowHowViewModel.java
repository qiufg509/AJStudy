package com.qiufengguang.ajstudy.fragment.knowhow;

import android.os.Handler;
import android.os.HandlerThread;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.graphicm.GraphicCardM;
import com.qiufengguang.ajstudy.card.graphics.GraphicCardS;
import com.qiufengguang.ajstudy.card.grid.GridCard;
import com.qiufengguang.ajstudy.data.GridCardBean;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.base.SingleLayoutData;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 知识列表页ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class KnowHowViewModel extends ViewModel {
    private static final String TAG = "KnowHowViewModel";

    private final MutableLiveData<List<LayoutData<?>>> liveData;

    /**
     * 每一页加载总数量
     */
    private static final int PAGE_SIZE = 10;

    private HandlerThread handlerThread;

    public KnowHowViewModel() {
        liveData = new MutableLiveData<>();
        initData();
    }

    public void initData() {
        handlerThread = new HandlerThread(TAG + "-Thread");
        if (!handlerThread.isAlive()) {
            handlerThread.start();
        }
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(() -> {
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


            LargeGraphicCardBean graphicCardBean1 = new LargeGraphicCardBean("天天领现金", "最高20元", "https://ts2.tc.mm.bing.net/th/id/OIP-C.-NxRwvTgS3CWh4yBPYMc7AHaHa?rs=1&pid=ImgDetMain&o=7&rm=3");
            SingleLayoutData<LargeGraphicCardBean> layoutData1 = LayoutDataFactory.createSingle(GraphicCardS.LAYOUT_ID, graphicCardBean1);

            LargeGraphicCardBean graphicCardBean2 = new LargeGraphicCardBean("是兄弟就来砍我", "再砍一刀就挂了", "https://ts2.tc.mm.bing.net/th/id/OIP-C.-NxRwvTgS3CWh4yBPYMc7AHaHa?rs=1&pid=ImgDetMain&o=7&rm=3");
            SingleLayoutData<LargeGraphicCardBean> layoutData2 = LayoutDataFactory.createSingle(GraphicCardS.LAYOUT_ID, graphicCardBean2);


            List<LargeGraphicCardBean> lgcBeans = new ArrayList<>();
            lgcBeans.add(new LargeGraphicCardBean("1秒滑下坡的刺激", "冬天的 passion 来自滑雪", "https://plus.unsplash.com/premium_photo-1664438942504-cc05d2c80f38?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
            lgcBeans.add(new LargeGraphicCardBean("欢迎加入爱宠一族", "与宠物双向治愈，让爱与责任同行", "https://www.foodiesfeed.com/wp-content/uploads/2023/10/girl-holding-takeaway-coffee-cup.jpg"));
            lgcBeans.add(new LargeGraphicCardBean("敲 AI 赛道的门", "抓住科技风口，觅得高薪工作", "https://www.foodiesfeed.com/wp-content/uploads/2023/05/juicy-cheeseburger.jpg"));
            lgcBeans.add(new LargeGraphicCardBean("为妈妈拍个 VLOG", "用影像讲述她的故事", "https://images.unsplash.com/photo-1768898794985-35c68b2df9b7?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
            lgcBeans.add(new LargeGraphicCardBean("九天揽星河", "用这些 APP 探索宇宙", "https://images.unsplash.com/photo-1768325400062-2b63fec226c3?q=80&w=1175&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
            lgcBeans.add(new LargeGraphicCardBean("边玩边学", "让孩子在趣味中获得知识", "https://images.unsplash.com/photo-1761850648640-2ee5870ee883?q=80&w=1170&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDF8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));
            lgcBeans.add(new LargeGraphicCardBean("跟李子柒见证汉服魅力", "种草人生第一件汉服", "https://www.foodiesfeed.com/wp-content/uploads/2023/03/french-fries-detail.jpg"));
            lgcBeans.add(new LargeGraphicCardBean("从零开始学理财", "每天刷新，每天积累", "https://ts2.tc.mm.bing.net/th/id/OIP-C.HF1qwV9btnJwbj4SWnZqogHaE7?rs=1&pid=ImgDetMain&o=7&rm=3"));
            lgcBeans.add(new LargeGraphicCardBean("一粥一饭，来之不易", "珍惜每一口粮食", "https://plus.unsplash.com/premium_photo-1761839920135-4bf781de96e3?q=80&w=685&auto=format&fit=crop&ixlib=rb-4.1.0&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D"));

            List<LayoutData<?>> dataList = Optional.of(lgcBeans)
                .orElse(Collections.emptyList())
                .stream()
                .map(bean ->
                    LayoutDataFactory.createSingle(GraphicCardM.LAYOUT_ID, bean))
                .collect(Collectors.toList());

            dataList.add(0, gridCardData);
            dataList.add(1, layoutData1);
            dataList.add(7, layoutData2);

            liveData.postValue(dataList);
        });
    }

    public List<LayoutData<?>> getPageData(int page) {
        List<LayoutData<?>> value = liveData.getValue();
        if (value == null || page < 0) {
            return null;
        }

        int fromIndex = page * PAGE_SIZE;
        int toIndex = Math.min((page + 1) * PAGE_SIZE, value.size());
        if (fromIndex >= toIndex) {
            return null;
        }
        return new ArrayList<>(value.subList(fromIndex, toIndex));
    }

    public LiveData<List<LayoutData<?>>> getLiveData() {
        return liveData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }
    }
}