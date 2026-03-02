package com.qiufengguang.ajstudy.activity.detail;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.qiufengguang.ajstudy.fragment.subpage.SubPageFragment;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.utils.FileSizeFormatter;
import com.qiufengguang.ajstudy.utils.StatusBarUtil;
import com.qiufengguang.ajstudy.utils.ThemeUtils;

import java.util.ArrayList;
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

    private DetailFragmentAdapter adapter;

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

        String title = getIntent().getStringExtra(Router.EXTRA_TITLE);
        if (!TextUtils.isEmpty(title)) {
            binding.titleBar.barTitle.setText(title);
        }
        Bundle bundle = getIntent().getBundleExtra(Router.EXTRA_DATA);
        if (bundle != null) {
            String uri = bundle.getString(Router.EXTRA_URI);
            if (TextUtils.equals(uri, Router.URI.PAGE_APP_DETAIL)) {
                String directory = bundle.getString(Router.EXTRA_DIRECTORY);
                viewModel.loadData(directory);
            }
        }

        setupToolbar();
        setupViewPager();
        setupListeners();
        observeData();
    }


    private void setupToolbar() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        int totalHeight = StatusBarUtil.adaptTitleBar(binding.titleBar.toolbar);

        // icon和titleBar的间距
        int appIconTitleBarSpacing = getResources().getDimensionPixelSize(R.dimen.activity_vertical_margin_m);
        ViewGroup.LayoutParams ivIconParams = binding.ivIcon.getLayoutParams();
        if (ivIconParams instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams iconParams = (ViewGroup.MarginLayoutParams) ivIconParams;
            iconParams.topMargin = totalHeight + appIconTitleBarSpacing;
            binding.ivIcon.setLayoutParams(iconParams);
        }

        int iconSize = getResources().getDimensionPixelSize(R.dimen.item_icon_size_xl);
        // head和appInfo的间隙
        int headInfoSpacing = getResources().getDimensionPixelSize(R.dimen.detail_head_info_spacing);
        offsetChangedCallback = new DetailHeadOffsetChangedCallback(this,
            binding.titleBar.toolbar,
            binding.titleBar.barBack,
            binding.titleBar.barShare,
            binding.titleBar.barTitle,
            appIconTitleBarSpacing + iconSize + headInfoSpacing
        );
        binding.appBarLayout.addOnOffsetChangedListener(offsetChangedCallback);
    }

    private void setupViewPager() {
        // 创建默认子
        List<Fragment> fragments = new ArrayList<>(1);
        fragments.add(SubPageFragment.newInstance());

        // 设置ViewPager2适配器
        adapter = new DetailFragmentAdapter(this, fragments);
        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(adapter.getItemCount());
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
            long scoredBy = detailAppData.getScoredBy();
            long scoreDivide = scoredBy / 10000;
            binding.tvScoredBy.setText(scoreDivide > 0 ? (scoreDivide + "万 人评论") : (scoredBy % 10000 + " 人评论"));
            binding.tvDownloads.setText(detailAppData.getDownloads());
            binding.tvDownloadsSuffix.setText(getString(R.string.detail_download_suffix));
            binding.tvMinAge.setText(detailAppData.getMinAge());
            binding.tvGradeDesc.setText(detailAppData.getGradeInfo().getGradeDesc());
            String fullSize = FileSizeFormatter.format(detailAppData.getFullSize());
            binding.btnInstall.setText(String.format(Locale.getDefault(), "安装 (%s)", fullSize));
        });

        viewModel.getTabData().observe(this, tabData -> {
            if (tabData == null || tabData.size() <= 1) {
                binding.tabLayout.setVisibility(View.GONE);
                return;
            }
            List<Fragment> fragments = new ArrayList<>(tabData.size());
            for (int i = 1, sum = tabData.size(); i < sum; i++) {
                Bundle args = new Bundle();
                String detailId = tabData.get(i).getDetailId();
                args.putString(Router.EXTRA_DATA, detailId);
                fragments.add(SubPageFragment.newInstance(args));
                if (!TextUtils.isEmpty(detailId) && detailId.contains("comment")) {
                    viewModel.loadCommentData(detailId);
                } else if (!TextUtils.isEmpty(detailId) && detailId.contains("recommend")) {
                    viewModel.loadRecommendData(detailId);
                }
            }
            adapter.addFragments(fragments);
            // 关联TabLayout和ViewPager2
            new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> tab.setText(tabData.get(position).getName())
            ).attach();
            binding.tabLayout.setVisibility(View.VISIBLE);
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
}