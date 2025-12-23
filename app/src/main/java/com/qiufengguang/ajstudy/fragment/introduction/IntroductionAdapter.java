package com.qiufengguang.ajstudy.fragment.introduction;


import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.DetailIntroduction;
import com.qiufengguang.ajstudy.databinding.ItemIntroductionAboutBinding;
import com.qiufengguang.ajstudy.databinding.ItemIntroductionEditorRecoBinding;
import com.qiufengguang.ajstudy.databinding.ItemIntroductionNormalBinding;
import com.qiufengguang.ajstudy.databinding.ItemIntroductionScreenBinding;

import java.util.List;

/**
 * 详情页-介绍子页面适配器
 *
 * @author qiufengguang
 * @since 2025/12/10 0:18
 */
public class IntroductionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    /**
     * 截屏预览item
     */
    private static final int VIEW_TYPE_SCREENSHOT = 0;
    /**
     * 一句话描述item
     */
    private static final int VIEW_TYPE_EDITOR_RECOMMEND = 1;
    /**
     * 关于此应用item
     */
    private static final int VIEW_TYPE_ABOUT_APP = 3;
    /**
     * 开发者item
     */
    private static final int VIEW_TYPE_DEVELOPER = 4;

    private DetailIntroduction detailIntro;

    public void setIntroduction(DetailIntroduction detailIntro) {
        this.detailIntro = detailIntro;
        if (detailIntro == null) {
            notifyItemRangeRemoved(0, getItemCount());
        } else {
            notifyItemRangeInserted(0, getItemCount());
        }
    }

    /**
     * 获取ViewType，根据position判断
     */
    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return VIEW_TYPE_SCREENSHOT;
            case 1:
                return VIEW_TYPE_EDITOR_RECOMMEND;
            case 2:
                return VIEW_TYPE_ABOUT_APP;
            default:
                return VIEW_TYPE_DEVELOPER;
        }
    }


    @Override
    public int getItemCount() {
        if (detailIntro == null) {
            return 0;
        }
        int itemCount = 0;
        List<String> images = detailIntro.getImages();
        if (images != null && !images.isEmpty()) {
            itemCount++;
        }
        if (!TextUtils.isEmpty(detailIntro.getEditorRecommend())) {
            itemCount++;
        }
        if (detailIntro.getAboutApp() != null) {
            itemCount++;
        }
        if (!TextUtils.isEmpty(detailIntro.getDeveloper())) {
            itemCount++;
        }
        return itemCount;
    }

    /**
     * 创建ViewHolder
     */
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_SCREENSHOT:
                ItemIntroductionScreenBinding sBinding = ItemIntroductionScreenBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                return new ScreenshotViewHolder(sBinding);
            case VIEW_TYPE_EDITOR_RECOMMEND:
                ItemIntroductionEditorRecoBinding eBinding = ItemIntroductionEditorRecoBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                return new IntroEditorViewHolder(eBinding);
            case VIEW_TYPE_ABOUT_APP:
                ItemIntroductionAboutBinding aBinding = ItemIntroductionAboutBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                return new IntroAboutViewHolder(aBinding);
            default:
                ItemIntroductionNormalBinding nBinding = ItemIntroductionNormalBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
                return new IntroNormalViewHolder(nBinding);
        }
    }

    /**
     * 绑定ViewHolder
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ScreenshotViewHolder) {
            ((ScreenshotViewHolder) holder).bind(detailIntro.getImages());
        } else if (holder instanceof IntroEditorViewHolder) {
            ((IntroEditorViewHolder) holder).bind(detailIntro.getEditorRecommend());
        } else if (holder instanceof IntroAboutViewHolder) {
            ((IntroAboutViewHolder) holder).bind(detailIntro.getAboutApp());
        } else {
            ((IntroNormalViewHolder) holder).bind(detailIntro.getDeveloper());
        }
    }

    /**
     * 横向RecyclerView的ViewHolder
     */
    public static class ScreenshotViewHolder extends RecyclerView.ViewHolder {
        private final ScreenshotAdapter adapter;

        public ScreenshotViewHolder(ItemIntroductionScreenBinding binding) {
            super(binding.getRoot());
            // 使用LinearLayoutManager设置为横向
            RecyclerView recyclerView = binding.getRoot();
            LinearLayoutManager layoutManager = new LinearLayoutManager(
                recyclerView.getContext(),
                LinearLayoutManager.HORIZONTAL,
                false
            );
            recyclerView.setLayoutManager(layoutManager);

            // 优化RecyclerView性能
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false); // 避免嵌套滚动冲突

            // 创建横向Adapter
            adapter = new ScreenshotAdapter();
            recyclerView.setAdapter(adapter);
        }

        /**
         * 绑定数据
         */
        public void bind(List<String> urls) {
            adapter.updateData(urls);
        }
    }


    public static class IntroEditorViewHolder extends RecyclerView.ViewHolder {

        private final ItemIntroductionEditorRecoBinding binding;

        public IntroEditorViewHolder(ItemIntroductionEditorRecoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String editorRecommend) {
            binding.tvContent.setCompoundDrawablesWithIntrinsicBounds(
                R.drawable.ic_quotation_start,
                0,
                R.drawable.ic_quotation_end,
                0
            );
            binding.tvContent.setCompoundDrawablePadding(
                binding.tvContent.getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_xs));
            binding.tvContent.setText(editorRecommend);
        }
    }


    public static class IntroAboutViewHolder extends RecyclerView.ViewHolder {

        private final ItemIntroductionAboutBinding binding;

        public IntroAboutViewHolder(ItemIntroductionAboutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DetailIntroduction.AboutApp aboutApp) {
            binding.tvTitle.setText("关于此应用");
            binding.tvTariff.setText(aboutApp.getTariffDesc());
            binding.tvVersion.setText(aboutApp.getVersion());
            binding.tvContent.setText(aboutApp.getAppIntro());
        }
    }

    public static class IntroNormalViewHolder extends RecyclerView.ViewHolder {

        private final ItemIntroductionNormalBinding binding;

        public IntroNormalViewHolder(ItemIntroductionNormalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(String developer) {
            binding.tvTitle.setText("开发者");
            binding.tvContent.setText(developer);
        }
    }
}
