package com.qiufengguang.ajstudy.activity.markdown;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.databinding.ActivityMarkdownBinding;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.utils.MarkwonHelper;
import com.qiufengguang.ajstudy.utils.StatusBarUtil;

/**
 * Markdown文件展示页面
 *
 * @author qiufengguang
 * @since 2025/12/26 20:14
 */
public class MarkdownActivity extends AppCompatActivity {

    private ActivityMarkdownBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMarkdownBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        StatusBarUtil.makeStatusBarTransparent(this);
        StatusBarUtil.setLightStatusBar(this, true);
        StatusBarUtil.adaptTitleBar(binding.titleBar);

        binding.titleBar.setOnBackClickListener(() -> getOnBackPressedDispatcher().onBackPressed());

        MarkdownModel viewModel = new ViewModelProvider(this).get(MarkdownModel.class);
        String title = getIntent().getStringExtra(Router.EXTRA_TITLE);
        if (!TextUtils.isEmpty(title)) {
            binding.titleBar.setTitle(title);
        }
        Bundle bundle = getIntent().getBundleExtra(Router.EXTRA_DATA);
        if (bundle != null) {
            String uri = bundle.getString(Router.EXTRA_URI);
            if (TextUtils.equals(uri, Router.URI.PAGE_ARTICLE_DETAIL)) {
                String directory = bundle.getString(Router.EXTRA_DIRECTORY);
                viewModel.loadData(directory);
            }
        }

        viewModel.getLiveData().observe(this, markdownContent ->
            MarkwonHelper.getInstanceSync(getApplicationContext())
                .setMarkdown(binding.tvContent, markdownContent));

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}