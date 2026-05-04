package com.qiufengguang.ajstudy.activity.ai;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.ActivityAiBinding;
import com.qiufengguang.ajstudy.fragment.ai.AiFragment;
import com.qiufengguang.ajstudy.fragment.base.BaseListFragment;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.utils.ThemeUtils;
import com.qiufengguang.ajstudy.view.OnToolBarListener;

/**
 * Ai对话页面
 * [样式重构]：实现侧边栏历史会话与新对话样式，对接数据库实时刷新
 *
 * @author qiufengguang
 * @since 2026/3/28 18:20
 */
public class AiActivity extends AppCompatActivity {
    private ActivityAiBinding binding;
    private AiViewModel viewModel;
    private ConversationAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeUtils.getChatTheme());
        super.onCreate(savedInstanceState);
        binding = ActivityAiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AiViewModel.class);

        if (savedInstanceState == null) {
            Bundle args = getIntent().getBundleExtra(Router.EXTRA_DATA);
            BaseListFragment f = AiFragment.newInstance(args);
            getSupportFragmentManager().beginTransaction().add(R.id.container, f).commitNow();
        }

        initHistoryList();
        addListener();
        observeViewModel();
    }

    private void initHistoryList() {
        historyAdapter = new ConversationAdapter();
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.rvHistory.setAdapter(historyAdapter);
    }

    private void observeViewModel() {
        // 观察历史记录，由 Room 驱动自动更新
        viewModel.getHistoryLive().observe(this, conversations ->
            historyAdapter.setData(conversations));
    }

    private void addListener() {
        binding.titleBar.setListener(new OnToolBarListener() {
            @Override
            public void onMenuClick() {
                hideKeyboard();
                binding.drawerLayout.open();
            }

            @Override
            public void onCloseClick() {
                finish();
            }
        });

        // 侧边栏：关闭按钮
        binding.btnCloseDrawer.setOnClickListener(v -> binding.drawerLayout.close());

        // 侧边栏：开启新对话
        binding.newConversation.setOnClickListener(v -> {
            viewModel.startNewConversation();
            binding.titleBar.setTitle("新对话");
            binding.drawerLayout.close();
        });

        // 主界面：发送按钮
        binding.layoutInputSend.btnSend.setOnClickListener(v -> {
            String input = binding.layoutInputSend.etMessage.getText().toString().trim();
            if (!TextUtils.isEmpty(input)) {
                viewModel.sendMessage(input);
                binding.layoutInputSend.etMessage.setText("");
                hideKeyboard();
            }
        });
        historyAdapter.setListener(conversation -> {
            viewModel.loadConversationData(conversation.getId());
            binding.titleBar.setTitle(conversation.getTitle());
            binding.drawerLayout.close();
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