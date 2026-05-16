package com.qiufengguang.ajstudy.activity.markdown;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.card.state.StateCard;
import com.qiufengguang.ajstudy.data.model.State;
import com.qiufengguang.ajstudy.databinding.ActivityMarkdownBinding;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.utils.MarkwonHelper;
import com.qiufengguang.ajstudy.utils.StatusBarUtil;
import com.qiufengguang.ajstudy.utils.ThemeUtils;
import com.qiufengguang.ajstudy.view.OnToolBarListener;

/**
 * Markdown文件展示页面
 *
 * @author qiufengguang
 * @since 2025/12/26 20:14
 */
public class MarkdownActivity extends AppCompatActivity {

    private ActivityMarkdownBinding binding;

    private MarkdownModel viewModel;

    private StateCard stateCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeUtils.getAppTheme());
        super.onCreate(savedInstanceState);
        binding = ActivityMarkdownBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StatusBarUtil.makeStatusBarTransparent(this);
        StatusBarUtil.setLightStatusBar(this, true);
        StatusBarUtil.adaptTitleBar(binding.titleBar);

        stateCard = new StateCard.Builder()
            .setBinding(binding.stateCard)
            .setListener((context, data) -> loadData())
            .create();

        binding.titleBar.setListener(new OnToolBarListener() {
            @Override
            public void onBackClick() {
                getOnBackPressedDispatcher().onBackPressed();
            }
        });

        viewModel = new ViewModelProvider(this).get(MarkdownModel.class);
        String title = getIntent().getStringExtra(Router.EXTRA_TITLE);
        if (!TextUtils.isEmpty(title)) {
            binding.titleBar.setTitle(title);
        }
        loadData();

        viewModel.getLiveData().observe(this, markdownContent -> {
            show(markdownContent, State.ERROR);
        });
    }

    private void loadData() {
        Bundle bundle = getIntent().getBundleExtra(Router.EXTRA_DATA);
        if (bundle != null) {
            String uri = bundle.getString(Router.EXTRA_URI);
            if (TextUtils.equals(uri, Router.URI.PAGE_ARTICLE_DETAIL)) {
                show(null, State.LOADING);

                String directory = bundle.getString(Router.EXTRA_DIRECTORY);
                viewModel.loadData(directory);
                return;
            }
        }
        show(null, State.EMPTY);
    }

    private void show(String content, State state) {
        if (!TextUtils.isEmpty(content)) {
            MarkwonHelper.getInstanceSync(getApplicationContext())
                .setMarkdown(binding.tvContent, content);
            binding.stateCard.getRoot().setVisibility(View.GONE);
            binding.tvContent.setVisibility(View.VISIBLE);
        } else {
            binding.tvContent.setVisibility(View.GONE);
            binding.stateCard.getRoot().setVisibility(View.VISIBLE);
            stateCard.update(state);
        }
    }

    @Override
    protected void onDestroy() {
        if (stateCard != null) {
            stateCard.release();
        }
        super.onDestroy();
        binding = null;
    }
}