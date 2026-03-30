package com.qiufengguang.ajstudy.activity.ai;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.chat.UserMessageCard;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.model.ChatMessage;
import com.qiufengguang.ajstudy.databinding.ActivityAiBinding;
import com.qiufengguang.ajstudy.fragment.ai.AiFragment;
import com.qiufengguang.ajstudy.fragment.base.BaseListFragment;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.utils.ThemeUtils;
import com.qiufengguang.ajstudy.view.OnToolBarListener;

import java.util.List;

/**
 * Ai对话页面
 *
 * @author qiufengguang
 * @since 2026/3/28 18:20
 */
public class AiActivity extends AppCompatActivity {
    private ActivityAiBinding binding;

    private AiViewModel viewModel;

    private Observer<List<LayoutData<?>>> observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeUtils.getChatTheme());
        super.onCreate(savedInstanceState);
        binding = ActivityAiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if (savedInstanceState == null) {
            Bundle args = getIntent().getBundleExtra(Router.EXTRA_DATA);
            BaseListFragment f = AiFragment.newInstance(args);
            getSupportFragmentManager().beginTransaction()
                .add(R.id.container, f)
                .commitNow();
        }

        binding.titleBar.setTitle("新对话");
        addListener();
        viewModel = new ViewModelProvider(this).get(AiViewModel.class);
        observer = dataList -> {
            if (dataList == null || dataList.size() != 1) {
                return;
            }
            LayoutData<?> layoutData = dataList.get(0);
            if (layoutData == null || layoutData.getLayoutId() != UserMessageCard.LAYOUT_ID) {
                return;
            }
            ChatMessage message = (ChatMessage) layoutData.getData();
            String content = message.getContent();
            binding.titleBar.setTitle(content);
            viewModel.getLiveData().removeObserver(observer);
            observer = null;
        };
        viewModel.getLiveData().observe(this, observer);
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
        binding.layoutInputSend.btnSend.setOnClickListener(v -> {
            String input = binding.layoutInputSend.etMessage.getText().toString().trim();
            if (TextUtils.isEmpty(input)) {
                return;
            }
            viewModel.sendMessage(new ChatMessage(ChatMessage.ROLE_USER, input));
            hideKeyboard();
            binding.layoutInputSend.etMessage.setText("");
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