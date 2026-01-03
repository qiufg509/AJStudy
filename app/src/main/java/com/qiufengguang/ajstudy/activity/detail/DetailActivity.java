package com.qiufengguang.ajstudy.activity.detail;

import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.tabs.TabLayoutMediator;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.ActivityDetailBinding;
import com.qiufengguang.ajstudy.dialog.Dialog;
import com.qiufengguang.ajstudy.dialog.manager.DialogWrapper;
import com.qiufengguang.ajstudy.dialog.manager.DialogsManager;
import com.qiufengguang.ajstudy.fragment.comment.CommentFragment;
import com.qiufengguang.ajstudy.fragment.introduction.IntroductionFragment;
import com.qiufengguang.ajstudy.fragment.recommendation.RecommendationFragment;
import com.qiufengguang.ajstudy.utils.DisplayMetricsHelper;
import com.qiufengguang.ajstudy.utils.StatusBarUtil;
import com.qiufengguang.ajstudy.utils.ThemeUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * 详情页
 *
 * @author qiufengguang
 * @since 2025/12/10 0:18
 */
public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;

    private DetailViewModel viewModel;

    private DetailHeadOffsetChangedCallback offsetChangedCallback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(ThemeUtils.getAppTheme());
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        StatusBarUtil.makeStatusBarTransparent(this);
        StatusBarUtil.setLightStatusBar(this, false);

        // 初始化ViewModel
        viewModel = new ViewModelProvider(this).get(DetailViewModel.class);

        int detailIndex = getIntent().getIntExtra("detail_index", 0);
        viewModel.loadData(detailIndex);
        setupToolbar();
        setupViewPager();
        setupListeners();
        observeData();
    }


    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        int statusBarHeight = DisplayMetricsHelper.getStatusBarHeight(this);
        int actionBarHeight = DisplayMetricsHelper.getActionBarHeight(this);

        Toolbar toolbar = binding.titleBar.toolbar;
        toolbar.setPadding(
            toolbar.getPaddingLeft(),
            statusBarHeight,
            toolbar.getPaddingRight(),
            toolbar.getPaddingBottom()
        );
        // 设置Toolbar最小高度为ActionBar高度 + 状态栏高度
        int totalHeight = actionBarHeight + statusBarHeight;
        ViewGroup.LayoutParams toolbarParams = toolbar.getLayoutParams();
        toolbarParams.height = totalHeight;
        toolbar.setLayoutParams(toolbarParams);

        ViewGroup.LayoutParams ivIconParams = binding.ivIcon.getLayoutParams();
        if (ivIconParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams iconParams = (ViewGroup.MarginLayoutParams) ivIconParams;
            iconParams.topMargin = totalHeight;
            binding.ivIcon.setLayoutParams(iconParams);
        }

        int expectedScrollRange = getResources().getDimensionPixelSize(R.dimen.item_icon_size_xl)
            + getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin_m);
        offsetChangedCallback = new DetailHeadOffsetChangedCallback(this,
            toolbar,
            binding.titleBar.barBack,
            binding.titleBar.barShare,
            binding.titleBar.barTitle,
            expectedScrollRange
        );
        binding.appBarLayout.addOnOffsetChangedListener(offsetChangedCallback);
    }

    private void setupViewPager() {
        // 创建Fragment列表
        List<Fragment> fragments = Arrays.asList(
            IntroductionFragment.newInstance(),
            CommentFragment.newInstance(),
            RecommendationFragment.newInstance()
        );

        // 创建Tab标题
        List<String> tabTitles = java.util.Arrays.asList(
            "介绍",
            "评论 79",
            "推荐"
        );

        // 设置ViewPager2适配器
        DetailFragmentAdapter adapter = new DetailFragmentAdapter(this, fragments);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(adapter.getItemCount());

        // 关联TabLayout和ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPager,
            (tab, position) -> tab.setText(tabTitles.get(position))
        ).attach();
    }

    private void setupListeners() {
        // ViewPager2 页面改变监听
        binding.viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                // 页面切换时的操作
                viewModel.setSelectedTab(position);
            }
        });
        binding.titleBar.barBack.setOnClickListener(v ->
            getOnBackPressedDispatcher().onBackPressed());

        // 设置菜单按钮点击事件
        binding.titleBar.barShare.setOnClickListener(v -> showShareDialog());

        binding.btnInstall.setOnClickListener(view -> {
            binding.btnInstall.setText("正在安装...");
            binding.btnInstall.setEnabled(false);

            view.postDelayed(() -> {
                binding.btnInstall.setText("打开");
                binding.btnInstall.setEnabled(true);

                // 设置新的点击事件
                binding.btnInstall.setOnClickListener(v -> {
                    // 打开应用
                    Toast.makeText(getApplicationContext(), "打开应用", Toast.LENGTH_SHORT).show();
                });
            }, 2000);
        });
    }

    private void showShareDialog() {
        Dialog.Builder builder = new Dialog.Builder(this)
            .setDialogView(R.layout.dialog_share_detail)
            .setWindowBackgroundP(0.5f)
            .setScreenWidthP(1.0f)
            .setGravity(Gravity.BOTTOM)
            .setCancelableOutSide(true)
            .setAnimStyle(R.style.DialogAnimPushBottom)
            .setBuildChildListener((dialog, parent, layoutRes) -> {
                parent.findViewById(R.id.tv_share_wx).setOnClickListener(v1 -> {
                    Toast.makeText(getApplicationContext(), "分享到微信", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
                parent.findViewById(R.id.tv_share_zfb).setOnClickListener(v1 -> {
                    Toast.makeText(getApplicationContext(), "分享到支付宝", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
                parent.findViewById(R.id.tv_share_local).setOnClickListener(v1 -> {
                    Toast.makeText(getApplicationContext(), "保存到本地", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                });
            });
        DialogsManager.getInstance().requestShow(new DialogWrapper(builder));
    }

    private void observeData() {
        viewModel.getDetailHead().observe(this, detailHead -> {
            if (detailHead == null) {
                return;
            }
            Glide.with(binding.getRoot().getContext())
                .load(detailHead.getIcoUri())
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .placeholder(R.drawable.placeholder_icon_l)
                .into(binding.ivIcon);
            binding.tvAppName.setText(detailHead.getName());
            binding.tvTariff.setText(detailHead.getTariffDesc());
            binding.tvLabel.setText(detailHead.getLabelNames());
            binding.titleBar.barTitle.setText(detailHead.getName());
        });

        viewModel.getAppData().observe(this, detailAppData -> {
            if (detailAppData == null) {
                return;
            }
            binding.tvStars.setText(detailAppData.getStars());
            binding.tvScoredBy.setText(detailAppData.getScoredBy());
            binding.tvDownloads.setText(detailAppData.getDownloads());
            binding.tvMinAge.setText(detailAppData.getMinAge());
            binding.tvGradeDesc.setText(detailAppData.getGradeInfo().getGradeDesc());
            String fullSize = detailAppData.getFullSize();
            binding.btnInstall.setText(String.format(Locale.getDefault(), "安装 (%s)", fullSize));
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (offsetChangedCallback != null) {
            offsetChangedCallback.release();
            binding.appBarLayout.removeOnOffsetChangedListener(offsetChangedCallback);
        }
        binding = null;
    }
}