package com.qiufengguang.ajstudy.fragment.introduction;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.SubItemIntroductionScreenBinding;

import java.util.List;

/**
 * 详情-介绍子页面截图预览适配器
 *
 * @author qiufengguang
 * @since 2025/12/13 11:49
 */
public class ScreenshotAdapter extends RecyclerView.Adapter<ScreenshotAdapter.ScreenshotViewHolder> {

    private List<String> screenshotUrls;

    public void updateData(List<String> screenshotUrls) {
        this.screenshotUrls = screenshotUrls;
        if (screenshotUrls == null) {
            notifyItemRangeRemoved(0, getItemCount());
        } else {
            notifyItemRangeInserted(0, getItemCount());
        }
    }


    @NonNull
    @Override
    public ScreenshotViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        SubItemIntroductionScreenBinding binding = SubItemIntroductionScreenBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new ScreenshotViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ScreenshotViewHolder holder, int position) {
        String url = screenshotUrls.get(position);
        holder.bind(url);
    }


    @Override
    public int getItemCount() {
        return screenshotUrls != null ? screenshotUrls.size() : 0;
    }

    @Override
    public void onViewRecycled(@NonNull ScreenshotViewHolder holder) {
        super.onViewRecycled(holder);
        Glide.with(holder.itemView.getContext()).clear(holder.itemView);
    }

    public static class ScreenshotViewHolder extends RecyclerView.ViewHolder {

        private final int roundingRadius;

        ScreenshotViewHolder(SubItemIntroductionScreenBinding binding) {
            super(binding.getRoot());
            roundingRadius = itemView.getResources().getDimensionPixelSize(R.dimen.radius_l);
        }

        private void bind(String url) {
            // 使用Glide加载图片，添加圆角和过渡动画
            RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .error(R.drawable.placeholder_image_vertical)
                .placeholder(R.drawable.placeholder_image_vertical)
                .transform(new CenterCrop(), new RoundedCorners(roundingRadius));

            Glide.with(itemView.getContext())
                .load(url)
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into((ImageView) itemView);

            // 点击事件
            itemView.setOnClickListener(v -> {

            });
        }
    }
}