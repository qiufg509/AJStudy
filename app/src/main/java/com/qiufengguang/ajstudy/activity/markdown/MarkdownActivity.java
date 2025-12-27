package com.qiufengguang.ajstudy.activity.markdown;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.core.widget.ImageViewCompat;
import androidx.lifecycle.ViewModelProvider;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.ActivityMarkdownBinding;
import com.qiufengguang.ajstudy.utils.MarkwonHelper;

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
        EdgeToEdge.enable(this);
        binding = ActivityMarkdownBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ColorStateList tintList = ColorStateList.valueOf(ContextCompat.getColor(this,
            R.color.ajstudy_color_toolbar_icon));
        ImageViewCompat.setImageTintList(binding.titleBar.barBack, tintList);
        binding.titleBar.barBack.setOnClickListener(v ->
            getOnBackPressedDispatcher().onBackPressed());
        binding.titleBar.barShare.setVisibility(View.GONE);

        MarkdownModel viewModel = new ViewModelProvider(this).get(MarkdownModel.class);
        String filePath = getIntent().getStringExtra("filePath");
        if (!TextUtils.isEmpty(filePath)) {
            viewModel.readLocalMarkdown(filePath);
        }
        String title = getIntent().getStringExtra("title");
        if (!TextUtils.isEmpty(title)) {
            binding.titleBar.barTitle.setText(title);
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