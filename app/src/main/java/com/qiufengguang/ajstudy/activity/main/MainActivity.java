package com.qiufengguang.ajstudy.activity.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavGraph;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.ActivityMainBinding;
import com.qiufengguang.ajstudy.router.AppNavigator;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.utils.StatusBarUtil;
import com.qiufengguang.ajstudy.utils.ThemeUtils;

/**
 * 主页面
 *
 * @author qiufengguang
 * @since 2025/5/5 22:12
 */
public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private MainViewModel viewModel;

    private NavController navController;

    private NavController.OnDestinationChangedListener destinationChangedListener;

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra(Router.EXTRA_RESTART, false)) {
            recreate();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // 完全恢复主题
        setTheme(ThemeUtils.getMianTheme());
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StatusBarUtil.makeStatusBarTransparent(this);

        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

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
        navController = navHostFragment.getNavController();

        // 设置导航图
        NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.main_navigation);
        navController.setGraph(navGraph);

        // 仅设置底部导航与导航控制器的绑定
        NavigationUI.setupWithNavController(binding.navView, navController);
        // 去掉icon染色，保持svg图多个path不同颜色
        binding.navView.setItemIconTintList(null);
        ThemeUtils.reapplyNavigationIcons(binding.navView);

        binding.navView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            onNavItemSelected(itemId);
            return true;
        });

        // 将监听器保存为成员变量，以便在 onDestroy 中移除
        destinationChangedListener = (controller, navDestination, bundle) -> {
            int destinationId = navDestination.getId();
            if (viewModel != null) {
                viewModel.setLiveData(destinationId);
            }
        };
        navController.addOnDestinationChangedListener(destinationChangedListener);
    }

    /**
     * 已经选中导航栏后的处理
     */
    private void onNavItemSelected(int itemId) {
        NavDestination destination = navController.getCurrentDestination();
        if (destination != null && destination.getId() == itemId) {
            return;
        }
        if (itemId == R.id.navigation_know_how) {
            binding.navView.setBackgroundResource(R.drawable.bottom_nav_background_blur);
        } else {
            binding.navView.setBackgroundColor(
                ContextCompat.getColor(this, R.color.ajstudy_window_background));
        }
        AppNavigator.getInstance().navigateTo(navController, itemId);
    }

    private void setupCustomBackNavigation() {
        getOnBackPressedDispatcher().addCallback(this,
            new MainPageBackPressedCallback(true, this));
    }

    @Override
    protected void onDestroy() {
        if (navController != null && destinationChangedListener != null) {
            navController.removeOnDestinationChangedListener(destinationChangedListener);
        }
        super.onDestroy();
        binding = null;
    }
}