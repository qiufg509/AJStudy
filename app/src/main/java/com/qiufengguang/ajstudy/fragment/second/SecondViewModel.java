package com.qiufengguang.ajstudy.fragment.second;

import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.grid.GridCard;
import com.qiufengguang.ajstudy.card.normal.NormalCard;
import com.qiufengguang.ajstudy.data.GridCardBean;
import com.qiufengguang.ajstudy.data.NormalCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.global.GlobalApp;
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

    /**
     * 每一页加载总数量
     */
    private static final int PAGE_SIZE = 10;

    private HandlerThread handlerThread;

    public SecondViewModel() {
        liveData = new MutableLiveData<>();
    }

    public void initData(String uri) {
        if (TextUtils.isEmpty(uri)) {
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
            liveData.setValue(dataList);
            return;
        }
        handlerThread = new HandlerThread(TAG + "-Thread");
        if (!handlerThread.isAlive()) {
            handlerThread.start();
        }
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(() -> {
            String listStr = FileUtil.readAssetsToString(GlobalApp.getContext(),
                Constant.Data.LIST_CONTENT_FILE);
            List<NormalCardBean> beans = new Gson().fromJson(listStr,
                new TypeToken<List<NormalCardBean>>() {
                }.getType());
            if (beans == null || beans.isEmpty()) {
                return;
            }
            List<String> fileNames = FileUtil.getExternalFileName(
                GlobalApp.getContext(), uri);
            if (fileNames != null && !fileNames.isEmpty()) {
                int size = Math.min(fileNames.size(), beans.size());
                for (int index = 0; index < size; index++) {
                    NormalCardBean bean = beans.get(index);
                    bean.setTitle(fileNames.get(index));
                    bean.setTargetPage(uri);
                }
            }

            List<LayoutData<?>> dataList = Optional.of(beans)
                .orElse(Collections.emptyList())
                .stream()
                .map(normalCardBean ->
                    LayoutDataFactory.createSingle(NormalCard.LAYOUT_ID, normalCardBean))
                .collect(Collectors.toList());

            liveData.postValue(dataList);
        });
    }

    public LiveData<List<LayoutData<?>>> getLiveData() {
        return liveData;
    }

    public void saveThemeIndex(GridCardBean bean) {
        if (bean.getItemType() != GridCardBean.TYPE_IMAGE) {
            return;
        }
        List<LayoutData<?>> value = liveData.getValue();
        if (value == null) {
            return;
        }
        for (int index = 0, sum = value.size(); index < sum; index++) {
            LayoutData<?> layoutData = value.get(index);
            if (layoutData.getLayoutId() != GridCard.LAYOUT_ID) {
                continue;
            }
            @SuppressWarnings("unchecked")
            List<GridCardBean> beans = (List<GridCardBean>) layoutData.getData();
            for (int pos = 0, size = beans.size(); pos < size; pos++) {
                GridCardBean cardBean = beans.get(pos);
                if (bean.getBackgroundTint() == cardBean.getBackgroundTint()
                    && bean.getItemType() == cardBean.getItemType()) {
                    cardBean.setIcon(R.drawable.ic_checkmark);
                    ThemeUtils.setSelectedThemeIndex(pos);
                    continue;
                }
                cardBean.setIcon(0);
            }
        }

        liveData.setValue(value);
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }
    }
}