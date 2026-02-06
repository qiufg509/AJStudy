package com.qiufengguang.ajstudy.activity.markdown;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.text.TextUtils;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.global.GlobalApp;
import com.qiufengguang.ajstudy.utils.FileUtil;

/**
 * Markdown页面ViewModel
 *
 * @author qiufengguang
 * @since 2025/12/25 22:22
 */
public class MarkdownModel extends ViewModel {
    private static final String TAG = "MarkdownModel";
    private final MutableLiveData<String> liveData = new MutableLiveData<>();

    private HandlerThread handlerThread;

    public LiveData<String> getLiveData() {
        return liveData;
    }

    public void readLocalMarkdown(String fileName) {
        handlerThread = new HandlerThread(TAG + "-Thread");
        if (!handlerThread.isAlive()) {
            handlerThread.start();
        }
        Handler handler = new Handler(handlerThread.getLooper());
        handler.post(() -> {
            Context context = GlobalApp.getContext();
            if (context == null) {
                return;
            }
            String fileStr = FileUtil.readExternalFileToString(context, fileName);
            if (TextUtils.isEmpty(fileStr)) {
                fileStr = FileUtil.readAssetsToString(context, fileName);
            }
            liveData.postValue(fileStr);
        });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (handlerThread != null) {
            handlerThread.quitSafely();
        }
    }
}
