package com.qiufengguang.ajstudy.activity.ai;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.ActivityAiBinding;
import com.qiufengguang.ajstudy.fragment.ai.AiFragment;
import com.qiufengguang.ajstudy.fragment.base.BaseListFragment;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.utils.ThemeUtils;
import com.qiufengguang.ajstudy.view.OnToolBarListener;

/**
 * Ai对话页面
 * [高级开发重构]：完善发送交互与新会话功能
 *
 * @author qiufengguang
 * @since 2026/3/28 18:20
 */
public class AiActivity extends AppCompatActivity {
    private ActivityAiBinding binding;
    private AiViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeUtils.getChatTheme());
        super.onCreate(savedInstanceState);
        binding = ActivityAiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            Bundle args = getIntent().getBundleExtra(Router.EXTRA_DATA);
            BaseListFragment f = AiFragment.newInstance(args);
            getSupportFragmentManager().beginTransaction().add(R.id.container, f).commitNow();
        }

        viewModel = new ViewModelProvider(this).get(AiViewModel.class);
        binding.titleBar.setTitle("新对话");
        addListener();
    }

    private void addListener() {
        binding.titleBar.setListener(new OnToolBarListener() {
            @Override
            public void onMenuClick() {
                hideKeyboard();
                binding.drawerLayout.postDelayed(() -> {
                    if (binding.drawerLayout.isOpen()) binding.drawerLayout.close();
                    else binding.drawerLayout.open();
                }, 150);
            }

            @Override
            public void onCloseClick() {
                finish();
            }
        });

        // 5. 点击 new_conversation 创建新会话
        View newConvBtn = findViewById(R.id.new_conversation);
        if (newConvBtn != null) {
            newConvBtn.setOnClickListener(v -> {
                viewModel.startNewConversation();
                binding.titleBar.setTitle("新对话");
                binding.drawerLayout.close();
            });
        }

        // 2. 点击发送按钮触发逻辑
        binding.layoutInputSend.btnSend.setOnClickListener(v -> {
            String input = binding.layoutInputSend.etMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(input)) {
                viewModel.sendMessage(input);
                binding.layoutInputSend.etMessage.setText("");
                hideKeyboard();
            }
        });
    }

    public void hideKeyboard() {
        View view = getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}