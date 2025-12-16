package com.qiufengguang.ajstudy.ui.knowhow;

import android.os.Handler;
import android.os.HandlerThread;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.SavedStateHandle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qiufengguang.ajstudy.data.KnowHowBean;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.global.GlobalApp;
import com.qiufengguang.ajstudy.ui.base.BaseViewModel;
import com.qiufengguang.ajstudy.utils.FileUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 知识列表页ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class KnowHowViewModel extends BaseViewModel {
    private static final String TAG = "KnowHowViewModel";

    private static final String KEY_CACHE_DATA = "cacheData";

    /**
     * 每一页加载总数量
     */
    private static final int PAGE_SIZE = 10;

    /**
     * 不需要保存的临时状态（使用普通的 LiveData）
     */
    private final MutableLiveData<List<KnowHowBean>> liveData = new MutableLiveData<>();

    /**
     * 使用 SavedStateHandle 管理的状态
     */
    private final LiveData<Map<Integer, Integer>> cacheLiveData;

    public KnowHowViewModel(SavedStateHandle savedStateHandle) {
        super(savedStateHandle);

        // 初始化保存的状态，设置默认值
        this.cacheLiveData = getSavedStateLiveData(KEY_CACHE_DATA, Collections.emptyMap());
        initData();
    }

    private void initData() {
        HandlerThread handlerThread = new HandlerThread(TAG + "-Thread");
        if (!handlerThread.isAlive()) {
            handlerThread.start();
        }
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(() -> {
            String listStr = FileUtil.readAssetsToString(GlobalApp.getContext(),
                Constant.LIST_CONTENT_FILE);
            List<KnowHowBean> beans = new Gson().fromJson(listStr,
                new TypeToken<List<KnowHowBean>>() {
                }.getType());
            liveData.postValue(beans);
        });

    }

    public LiveData<List<KnowHowBean>> getLiveData() {
        return liveData;
    }

    public List<KnowHowBean> getPageData(int page) {
        List<KnowHowBean> value = liveData.getValue();
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

    public void cacheData(String key) {
        Object savedState = getSavedState(KEY_CACHE_DATA);
        Map<String, Integer> cacheMap = new HashMap<>();
        if (savedState instanceof Map) {
            // 为了安全，我们创建一个新的Map，并尝试将符合类型的值放入
            Map<?, ?> originalMap = (Map<?, ?>) savedState;
            for (Map.Entry<?, ?> entry : originalMap.entrySet()) {
                if (entry.getKey() instanceof String && entry.getValue() instanceof Integer) {
                    cacheMap.put((String) entry.getKey(), (Integer) entry.getValue());
                }
            }
        }

        // 获取当前key的计数，如果没有则为0
        Integer number = cacheMap.get(key);
        int count = 0;
        if (number != null) {
            count = number + 1;
        }
        cacheMap.put(key, count);
        setSavedState(KEY_CACHE_DATA, cacheMap);
    }
}