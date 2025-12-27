package com.qiufengguang.ajstudy.activity.login;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.data.LoginAction;
import com.qiufengguang.ajstudy.data.User;
import com.qiufengguang.ajstudy.databinding.ActivityLoginBinding;
import com.qiufengguang.ajstudy.global.GlobalApp;
import com.qiufengguang.ajstudy.global.GlobalViewModel;
import com.qiufengguang.ajstudy.utils.ThemeUtils;

/**
 * 登录页面
 *
 * @author qiufengguang
 * @since 2025/11/30 2:15
 */
public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeUtils.getLoginTheme());
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        // ViewBinding初始化
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // ViewModel初始化
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        initView();
        initObservers();
        loginViewModel.loadSavedUser();
    }

    private void initView() {
        // 输入监听
        binding.etPhone.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validateInputs();
            }
        });

        binding.etPassword.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                validateInputs();
            }
        });

        // 登录按钮点击
        binding.btnLogin.setOnClickListener(v -> attemptLogin());
        binding.barBack.setOnClickListener(v -> getOnBackPressedDispatcher().onBackPressed());

        // 其他登录方式
        binding.tvOtherLogin.setOnClickListener(v -> {
            // 跳转到其他登录方式页面
            Toast.makeText(this, "跳转到其他登录方式", Toast.LENGTH_SHORT).show();
        });
    }

    private void initObservers() {
        loginViewModel.getLoginResult().observe(this, result -> {
            if (result == null) {
                return;
            }
            switch (result.getStatus()) {
                case LOADING:
                    showLoading(true);
                    break;
                case SUCCESS:
                    showLoading(false);
                    handleLoginSuccess(result.getUser());
                    break;
                case ERROR:
                    showLoading(false);
                    handleLoginError(result.getMessage());
                    break;
                case INVALID:
                    handleLoginInvalid(result.getUser());
                    break;
            }
        });
    }

    private void handleLoginInvalid(User user) {
        if (user == null || !user.isRememberPwd()) {
            return;
        }
        binding.etPhone.setText(user.getPhone());
        binding.etPassword.setText(user.getPassword());
        binding.cbRememberPwd.setChecked(true);
        validateInputs();
    }

    private void attemptLogin() {
        Editable phoneText = binding.etPhone.getText();
        String phone = TextUtils.isEmpty(phoneText) ? "" : phoneText.toString().trim();
        Editable passwordText = binding.etPassword.getText();
        String password = TextUtils.isEmpty(passwordText) ? "" : passwordText.toString().trim();
        boolean rememberPwd = binding.cbRememberPwd.isChecked();

        loginViewModel.login(phone, password, rememberPwd);
    }

    private void validateInputs() {
        Editable phoneText = binding.etPhone.getText();
        String phone = TextUtils.isEmpty(phoneText) ? "" : phoneText.toString().trim();
        Editable passwordText = binding.etPassword.getText();
        String password = TextUtils.isEmpty(passwordText) ? "" : passwordText.toString().trim();
        boolean isValid = !phone.isEmpty() && !password.isEmpty();
        binding.btnLogin.setEnabled(isValid);
    }

    private void showLoading(boolean show) {
        binding.progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        binding.btnLogin.setEnabled(!show);
        binding.btnLogin.setText(show ? "登录中..." : "登录");
    }

    private void handleLoginSuccess(User user) {
        Application application = getApplication();
        if (application instanceof GlobalApp) {
            GlobalViewModel globalViewModel = ((GlobalApp) application).getGlobalViewModel();
            globalViewModel.setCurrentUser(user);
            Intent intent = getIntent();
            LoginAction loginAction = new LoginAction(true);
            loginAction.setOriginalPage(intent.getStringExtra(LoginAction.ORIGINAL_PAGE));
            loginAction.setDestinationAction(intent.getStringExtra(LoginAction.DESTINATION_ACTION));
            loginAction.setDestinationId(intent.getIntExtra(LoginAction.DESTINATION_ID, -1));
            globalViewModel.setLoginAction(loginAction);
        }
        finish();
    }

    private void handleLoginError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    /**
     * 简化的TextWatcher
     */
    private abstract static class SimpleTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }
    }
}