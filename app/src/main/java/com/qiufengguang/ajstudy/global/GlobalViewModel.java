package com.qiufengguang.ajstudy.global;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.data.model.LoginAction;
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
     * 登录跳转动作
     */
    private final MutableLiveData<LoginAction> loginLiveData = new MutableLiveData<>();

    // 网络状态
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

    public LiveData<LoginAction> getLoginLiveData() {
        return loginLiveData;
    }

    /**
     * 设置登录动作
     * [性能专家优化]：自动识别线程环境，支持后台线程调用
     */
    public void setLoginAction(LoginAction action) {
        if (Thread.currentThread() == android.os.Looper.getMainLooper().getThread()) {
            loginLiveData.setValue(action);
        } else {
            loginLiveData.postValue(action);
        }
    }

    public void resetLoginAction() {
        LoginAction action = loginLiveData.getValue();
        if (action == null) {
            return;
        }
        action.setOriginalPage("");
        action.setDestinationAction("");
        action.setDestinationId(-1);
        setLoginAction(action);
    }

    public LoginAction getLoginAction() {
        return loginLiveData.getValue();
    }

    public boolean isLoggedIn() {
        LoginAction action = loginLiveData.getValue();
        return action != null && action.isLoggedIn();
    }

    public LiveData<Boolean> getIsNetworkAvailable() {
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