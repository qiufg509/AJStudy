package com.qiufengguang.ajstudy.global;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.data.model.User;

/**
 * 全局ViewModel
 *
 * @author qiufengguang
 * @since 2025/5/5 23:54
 */
public class GlobalViewModel extends ViewModel {
    /**
     * 用户信息
     */
    private final MutableLiveData<User> userLiveData = new MutableLiveData<>();

    /**
     * 网络状态
     */
    private final MutableLiveData<Boolean> isNetworkAvailable = new MutableLiveData<>(true);

    public LiveData<User> getUserLiveData() {
        return userLiveData;
    }

    /**
     * 设置当前用户
     * [性能专家优化]：自动识别线程环境，支持后台线程调用
     */
    public void setCurrentUser(User user) {
        if (Thread.currentThread() == android.os.Looper.getMainLooper().getThread()) {
            userLiveData.setValue(user);
        } else {
            userLiveData.postValue(user);
        }
    }

    public User getCurrentUser() {
        return userLiveData.getValue();
    }

    public LiveData<Boolean> getNetworkLive() {
        return isNetworkAvailable;
    }

    public void setNetworkAvailable(boolean available) {
        if (Thread.currentThread() == android.os.Looper.getMainLooper().getThread()) {
            isNetworkAvailable.setValue(available);
        } else {
            isNetworkAvailable.postValue(available);
        }
    }

    public boolean isNetworkAvailable() {
        Boolean value = isNetworkAvailable.getValue();
        return value != null && value;
    }

    @Override
    protected void onCleared() {
        // 清理资源
        super.onCleared();
    }
}