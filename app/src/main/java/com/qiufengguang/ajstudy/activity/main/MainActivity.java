package com.qiufengguang.ajstudy.activity.main;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
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
import com.qiufengguang.ajstudy.view.MainPageBackPressedCallback;

/**
 * 主页面
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MainViewModel viewModel;

    private GlobalViewModel globalVm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 完全恢复主题
        setTheme(R.style.Theme_AJStudy);
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StatusBarUtil.makeStatusBarTransparent(this);

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

        setupNavigation();
        setupCustomBackNavigation();
    }

    /**
     * 导航设置
     */
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
        // 去掉icon染色，保持svg图多个path不同颜色
        binding.navView.setItemIconTintList(null);

        binding.navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            NavDestination destination = navController.getCurrentDestination();
            if (destination != null && destination.getId() == itemId) {
                // BottomNavigationView会保持原item的选中状态，如果返回false它会先取消选中，然后重新选中，可能闪烁
                return true;
            }
            if (itemId == R.id.navigation_me) {
                if (globalVm == null || !globalVm.isLoggedIn()) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra(LoginAction.ORIGINAL_PAGE, MainActivity.class.getName());
                    intent.putExtra(LoginAction.DESTINATION_ID, itemId);
                    startActivity(intent);
                    // "监听器没有处理这次点击",BottomNavigationView 会尝试恢复之前的选中状态
                    return false;
                }
            }
            navController.navigate(itemId);
            viewModel.setLiveData(itemId);
            return true;
        });
    }

    /**
     * 返回处理
     */
    private void setupCustomBackNavigation() {
        getOnBackPressedDispatcher().addCallback(this,
            new MainPageBackPressedCallback(true, this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (globalVm != null) {
            globalVm.getLoginLiveData().removeObservers(this);
            globalVm.resetLoginAction();
        }
        binding = null;
    }
}