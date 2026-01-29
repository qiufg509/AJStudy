package com.qiufengguang.ajstudy.fragment.knowhow;

import android.os.Handler;
import android.os.HandlerThread;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiufengguang.ajstudy.data.NormalCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.global.GlobalApp;
import com.qiufengguang.ajstudy.utils.FileUtil;

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
    }

    public void initData(String course) {
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
                GlobalApp.getContext(), course);
            if (fileNames != null && !fileNames.isEmpty()) {
                int size = Math.min(fileNames.size(), beans.size());
                for (int index = 0; index < size; index++) {
                    NormalCardBean bean = beans.get(index);
                    bean.setTitle(fileNames.get(index));
                    bean.setTargetPage(course);
                }
            }

            List<LayoutData<?>> dataList = Optional.of(beans)
                .orElse(Collections.emptyList())
                .stream()
                .map(LayoutDataFactory::createSingle)
                .collect(Collectors.toList());

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