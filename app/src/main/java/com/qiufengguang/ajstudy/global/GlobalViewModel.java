package com.qiufengguang.ajstudy.global;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.data.LoginAction;
import com.qiufengguang.ajstudy.data.User;

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

    public void setCurrentUser(User user) {
        userLiveData.setValue(user);
    }

    public User getCurrentUser() {
        return userLiveData.getValue();
    }

    public LiveData<LoginAction> getLoginLiveData() {
        return loginLiveData;
    }

    public void setLoginAction(LoginAction action) {
        loginLiveData.setValue(action);
    }

    public void resetLoginAction() {
        LoginAction action = loginLiveData.getValue();
        if (action == null) {
            return;
        }
        action.setOriginalPage("");
        action.setDestinationAction("");
        action.setDestinationId(-1);
        loginLiveData.setValue(action);
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
        isNetworkAvailable.setValue(available);
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
