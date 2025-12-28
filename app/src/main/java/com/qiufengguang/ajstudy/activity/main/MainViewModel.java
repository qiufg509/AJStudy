package com.qiufengguang.ajstudy.activity.main;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.utils.MarkwonHelper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 主页面ViewModel
 *
 * @author qiufengguang
 * @since 2025/11/27 17:42
 */
public class MainViewModel extends ViewModel {
    private static final String TAG = "MainViewModel";

    private final MutableLiveData<Integer> liveData = new MutableLiveData<>();

    private ExecutorService sExecutor;

    private static Future<?> sInitFuture;

    public MainViewModel() {
        initThirdSdk();
    }

    public LiveData<Integer> getLiveData() {
        return liveData;
    }

    public void setLiveData(int itemId) {
        liveData.setValue(itemId);
    }

    /**
     * 三方sdk初始化
     */
    private void initThirdSdk() {
        if (sExecutor == null) {
            sExecutor = Executors.newSingleThreadExecutor();
        }
        sInitFuture = sExecutor.submit(() -> {
            try {
                MarkwonHelper.initialize();
            } catch (Exception e) {
                Log.w(TAG, "initThirdSdk error: " + e.getMessage());
            }
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (sInitFuture != null && !sInitFuture.isDone()) {
            sInitFuture.cancel(true);
        }
        if (sExecutor != null) {
            sExecutor.shutdownNow();
            sExecutor = null;
        }
    }
}
