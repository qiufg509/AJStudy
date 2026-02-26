package com.qiufengguang.ajstudy.activity.login;

// LoginViewModel.java

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.qiufengguang.ajstudy.data.model.LoginResult;
import com.qiufengguang.ajstudy.data.model.User;
import com.qiufengguang.ajstudy.network.LoginCallback;
import com.qiufengguang.ajstudy.network.LoginRepository;

/**
 * 登录页面ViewModel
 *
 * @author qiufengguang
 * @since 2025/11/30 2:15
 */
public class LoginViewModel extends ViewModel {
    private final MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private final LoginRepository loginRepository;

    public LoginViewModel() {
        this.loginRepository = new LoginRepository();
    }

    public LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String phone, String password, boolean rememberPwd) {
        // 显示加载状态
        loginResult.setValue(new LoginResult(LoginResult.Status.LOADING, "登录中...", null));

        // 输入验证
        if (!isValidPhone(phone)) {
            loginResult.setValue(new LoginResult(LoginResult.Status.ERROR, "请输入正确的手机号", null));
            return;
        }

        if (!isValidPassword(password)) {
            loginResult.setValue(new LoginResult(LoginResult.Status.ERROR, "密码长度6-20位", null));
            return;
        }

        // 执行登录
        loginRepository.login(phone, password, new LoginCallback() {
            @Override
            public void onSuccess(User user) {
                user.setRememberPwd(rememberPwd);
                loginRepository.saveUserInfo(user);
                loginResult.postValue(new LoginResult(LoginResult.Status.SUCCESS, "登录成功", user));
            }

            @Override
            public void onError(String error) {
                loginResult.postValue(new LoginResult(LoginResult.Status.ERROR, error, null));
            }
        });
    }

    private boolean isValidPhone(String phone) {
        return phone != null && phone.matches("^1[3-9]\\d{9}$");
    }

    private boolean isValidPassword(String password) {
        return password != null && password.length() >= 6 && password.length() <= 20;
    }

    public void loadSavedUser() {
        User savedUser = loginRepository.getSavedUser();
        if (savedUser == null) {
            return;
        }
        LoginResult.Status status = savedUser.isInvalid() ? LoginResult.Status.INVALID
            : LoginResult.Status.SUCCESS;
        loginResult.setValue(new LoginResult(status, "", savedUser));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (loginRepository != null) {
            loginRepository.release();
        }
    }
}