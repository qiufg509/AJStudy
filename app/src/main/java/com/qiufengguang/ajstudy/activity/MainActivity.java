package com.qiufengguang.ajstudy.activity;

import android.os.Bundle;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.ActivityMainBinding;
import com.qiufengguang.ajstudy.utils.StatusBarUtil;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MainViewModel viewModel;

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

        // 添加目的地改变监听器来更新选中状态
        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {
            int itemId = destination.getId();
            // 防止重复设置导致闪烁
            if (binding.navView.getSelectedItemId() != itemId) {
                binding.navView.setSelectedItemId(itemId);
            }
            viewModel.setLiveData(itemId);
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