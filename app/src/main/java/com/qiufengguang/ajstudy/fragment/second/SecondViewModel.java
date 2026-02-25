package com.qiufengguang.ajstudy.fragment.second;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.empty.EmptyCard;
import com.qiufengguang.ajstudy.card.grid.GridCard;
import com.qiufengguang.ajstudy.card.graphicl.GraphicCardL;
import com.qiufengguang.ajstudy.card.normal.NormalCard;
import com.qiufengguang.ajstudy.data.GridCardBean;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.data.NormalCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.base.SingleLayoutData;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.global.GlobalApp;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.utils.FileUtil;
import com.qiufengguang.ajstudy.utils.ThemeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 二级页ViewModel
 *
 * @author qiufengguang
 * @since 2026/1/31 21:53
 */
public class SecondViewModel extends ViewModel {
    private static final String TAG = "SecondViewModel";

    private final MutableLiveData<List<LayoutData<?>>> liveData;

    private HandlerThread handlerThread;

    public SecondViewModel() {
        liveData = new MutableLiveData<>();
    }

    public void initData(String uri, String navigateTo) {
        if (TextUtils.isEmpty(uri)) {
            return;
        }
        handlerThread = new HandlerThread(TAG + "-Thread");
        if (!handlerThread.isAlive()) {
            handlerThread.start();
        }
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(new ParsePageDataTask(uri, navigateTo));
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

    private class ParsePageDataTask implements Runnable {
        private final String uri;
        private final String navigateTo;

        public ParsePageDataTask(String uri, String navigateTo) {
            this.uri = uri;
            this.navigateTo = navigateTo;
        }

        @Override
        public void run() {
            if (TextUtils.isEmpty(uri)) {
                return;
            }
            if (uri.startsWith(Router.URI.PAGE_APP_LIST)) {
                fetchAppListData(navigateTo);
            } else if (uri.startsWith(Router.URI.PAGE_ARTICLE_LIST)) {
                fetchArticleListData();
            } else if (uri.startsWith(Router.URI.PAGE_COLOR_SCHEME)) {
                fetchColorSchemeData();
            } else if (uri.startsWith(Router.URI.PAGE_STUDY_RECORD)) {
                fetchStudyRecord();
            } else if (uri.startsWith(Router.URI.PAGE_FAVORITES)) {
                fetchFavorites();
            } else if (uri.startsWith(Router.URI.PAGE_LAB)) {
                fetchLab();
            } else if (uri.startsWith(Router.URI.PAGE_HELP_FEEDBACK)) {
                fetchHelpFeedback();
            }
        }

        private void fetchArticleListData() {
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
                    LayoutDataFactory.createSingle(GraphicCardL.LAYOUT_ID, bean))
                .collect(Collectors.toList());

            liveData.postValue(dataList);
        }

        private void fetchAppListData(String navigateTo) {
            String listStr = FileUtil.readAssetsToString(GlobalApp.getContext(),
                Constant.Data.LIST_CONTENT_FILE);
            List<NormalCardBean> beans = new Gson().fromJson(listStr,
                new TypeToken<List<NormalCardBean>>() {
                }.getType());
            if (beans == null || beans.isEmpty()) {
                return;
            }
            List<String> fileNames = FileUtil.getExternalFileName(
                GlobalApp.getContext(), navigateTo);
            if (fileNames == null || fileNames.isEmpty()) {
                return;
            }
            List<NormalCardBean> resultBeans = getNormalCardBeans(navigateTo, fileNames, beans);
            List<LayoutData<?>> dataList = resultBeans
                .stream()
                .map(bean ->
                    LayoutDataFactory.createSingle(NormalCard.LAYOUT_ID, bean))
                .collect(Collectors.toList());
            liveData.postValue(dataList);
        }

        @NonNull
        private List<NormalCardBean> getNormalCardBeans(
            String navigateTo,
            List<String> fileNames,
            List<NormalCardBean> beans
        ) {
            int size = Math.min(fileNames.size(), beans.size());
            List<NormalCardBean> resultBeans = new ArrayList<>(size);
            for (int index = 0; index < size; index++) {
                NormalCardBean bean = beans.get(index);
                String filePath = fileNames.get(index);
                String title = filePath.replaceAll("\\.(md|txt|json|xml)$", "");
                bean.setTitle(title);
                bean.setUri(Router.URI.PAGE_ARTICLE_LIST);
                bean.setNavigateTo(navigateTo + "/" + filePath);
                resultBeans.add(bean);
            }
            return resultBeans;
        }

        private void fetchColorSchemeData() {
            List<GridCardBean> gridCardBeans = List.of(
                new GridCardBean(R.color.ajstudy_primary_red),
                new GridCardBean(R.color.ajstudy_primary_blue),
                new GridCardBean(R.color.ajstudy_primary_green),
                new GridCardBean(R.color.ajstudy_primary_orange),
                new GridCardBean(R.color.ajstudy_primary_pink),
                new GridCardBean(R.color.ajstudy_primary_black),
                new GridCardBean(R.color.ajstudy_primary_amber),
                new GridCardBean(R.color.ajstudy_primary_indigo),
                new GridCardBean(R.color.ajstudy_primary_lime),
                new GridCardBean(R.color.ajstudy_primary_purple)
            );
            int themeIndex = ThemeUtils.getSelectedThemeIndex();
            GridCardBean bean = gridCardBeans.get(themeIndex);
            bean.setIcon(R.drawable.ic_checkmark);
            LayoutData<List<GridCardBean>> gridCardData = LayoutDataFactory.createCollection(GridCard.LAYOUT_ID, gridCardBeans, "主题色");
            List<LayoutData<?>> dataList = new ArrayList<>();
            dataList.add(gridCardData);
            liveData.postValue(dataList);
        }

        private void fetchStudyRecord() {
            SingleLayoutData<?> emptyCardData = LayoutDataFactory.createSingle(EmptyCard.LAYOUT_ID, null);
            List<LayoutData<?>> dataList = new ArrayList<>();
            dataList.add(emptyCardData);
            liveData.postValue(dataList);
        }

        private void fetchFavorites() {
            SingleLayoutData<?> emptyCardData = LayoutDataFactory.createSingle(EmptyCard.LAYOUT_ID, null);
            List<LayoutData<?>> dataList = new ArrayList<>();
            dataList.add(emptyCardData);
            liveData.postValue(dataList);
        }

        private void fetchLab() {
            SingleLayoutData<?> emptyCardData = LayoutDataFactory.createSingle(EmptyCard.LAYOUT_ID, null);
            List<LayoutData<?>> dataList = new ArrayList<>();
            dataList.add(emptyCardData);
            liveData.postValue(dataList);
        }

        private void fetchHelpFeedback() {
            SingleLayoutData<?> emptyCardData = LayoutDataFactory.createSingle(EmptyCard.LAYOUT_ID, null);
            List<LayoutData<?>> dataList = new ArrayList<>();
            dataList.add(emptyCardData);
            liveData.postValue(dataList);
        }
    }
}