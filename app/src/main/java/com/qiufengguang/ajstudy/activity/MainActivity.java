package com.qiufengguang.ajstudy.activity;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.activity.login.LoginActivity;
import com.qiufengguang.ajstudy.data.LoginAction;
import com.qiufengguang.ajstudy.databinding.ActivityMainBinding;
import com.qiufengguang.ajstudy.global.GlobalApp;
import com.qiufengguang.ajstudy.global.GlobalViewModel;
import com.qiufengguang.ajstudy.utils.StatusBarUtil;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MainViewModel viewModel;

    private GlobalViewModel globalVm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 完全恢复主题
        setTheme(R.style.Theme_AJStudy);
        super.onCreate(savedInstanceState);
        StatusBarUtil.makeStatusBarTransparent(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // 彻底隐藏系统 ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Application application = getApplication();
        if (application instanceof GlobalApp) {
            globalVm = ((GlobalApp) application).getGlobalViewModel();
            globalVm.getLoginLiveData().observe(this, loginAction -> {
                if (!loginAction.isLoggedIn()) {
                    return;
                }
                if (!TextUtils.equals(loginAction.getOriginalPage(), MainActivity.class.getName())) {
                    return;
                }
                int destinationId = loginAction.getDestinationId();
                binding.navView.setSelectedItemId(destinationId);
                viewModel.setLiveData(destinationId);
            });
        }

        setupEdgeToEdge();
        setupNavigation();
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
            .findFragmentById(R.id.nav_host_fragment_activity_main);
        if (navHostFragment == null) {
            return;
        }
        NavController navController = navHostFragment.getNavController();

        // 设置导航图
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.main_navigation);
        navController.setGraph(navGraph);

        // 仅设置底部导航与导航控制器的绑定
        NavigationUI.setupWithNavController(binding.navView, navController);

        binding.navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.navigation_notifications) {
                if (globalVm == null || !globalVm.isLoggedIn()) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra(LoginAction.ORIGINAL_PAGE, MainActivity.class.getName());
                    intent.putExtra(LoginAction.DESTINATION_ID, itemId);
                    startActivity(intent);
                    // 跳转登录页面
                    return false;
                }
            }
            navController.navigate(itemId);
            viewModel.setLiveData(itemId);
            return true;
        });
    }

    private void setupEdgeToEdge() {
        // 设置导航栏颜色
        Window window = getWindow();
        if (window == null) {
            return;
        }
        window.setNavigationBarContrastEnforced(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 确保状态栏设置持续有效
        StatusBarUtil.makeStatusBarTransparent(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}