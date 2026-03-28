package com.qiufengguang.ajstudy.activity.ai;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.ActivityAiBinding;
import com.qiufengguang.ajstudy.fragment.ai.AiFragment;
import com.qiufengguang.ajstudy.fragment.base.BaseListFragment;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.utils.StatusBarUtil;
import com.qiufengguang.ajstudy.view.OnToolBarListener;

/**
 * Ai对话页面
 *
 * @author qiufengguang
 * @since 2026/3/28 18:20
 */
public class AiActivity extends AppCompatActivity {
    private ActivityAiBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // 配置了软键盘adjustResize,如果使用全屏或透明状态栏导致无法resize
        StatusBarUtil.setLightStatusBar(this, true);
        StatusBarUtil.throttleUpdateStatusBarColor(this, getColor(R.color.ajstudy_window_background));

        if (savedInstanceState == null) {
            Bundle args = getIntent().getBundleExtra(Router.EXTRA_DATA);
            BaseListFragment f = AiFragment.newInstance(args);
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, f)
                .commitNow();
        }

        addListener();
    }

    private void addListener() {
        binding.titleBar.setListener(new OnToolBarListener() {
            @Override
            public void onMenuClick() {
                hideKeyboard();

                // 延迟一小段时间后打开/关闭抽屉，确保键盘收起动画完成
                binding.drawerLayout.postDelayed(() -> {
                    if (binding.drawerLayout.isOpen()) {
                        binding.drawerLayout.close();
                    } else {
                        binding.drawerLayout.open();
                    }
                }, 150);
            }

            @Override
            public void onCloseClick() {
                finish();
            }
        });
        binding.drawerLayout.addDrawerListener(new DrawerLayout.SimpleDrawerListener() {
            @Override
            public void onDrawerOpened(View drawerView) {
                StatusBarUtil.throttleUpdateStatusBarColor(AiActivity.this, Color.WHITE);
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                StatusBarUtil.throttleUpdateStatusBarColor(AiActivity.this, getColor(R.color.ajstudy_window_background));
                super.onDrawerClosed(drawerView);
            }
        });
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view == null) {
            return;
        }
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}