package com.qiufengguang.ajstudy.card.series;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.SeriesCardBean;
import com.qiufengguang.ajstudy.databinding.CardSeriesBinding;
import com.qiufengguang.ajstudy.global.GlobalApp;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 系列卡片
 *
 * @author qiufengguang
 * @since 2026/1/25 15:44
 */
public class SeriesCardWrapper {
    private String cardTitle;

    private List<SeriesCardBean> beans;

    private WeakReference<CardSeriesBinding> bindingRef;

    private OnItemClickListener listener;

    private int showIndex = -1;

    private RequestOptions requestOptions;

    private SeriesCardWrapper() {
    }

    public void setData(List<SeriesCardBean> beans, String cardTitle) {
        this.cardTitle = cardTitle;
        this.beans = beans;
        this.show();
    }

    public void show() {
        if (this.beans == null || this.beans.isEmpty()) {
            return;
        }
        if (bindingRef == null) {
            return;
        }
        CardSeriesBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }
        binding.tvTitle.setText(cardTitle);
        fillItem(binding.ivCover1, binding.tvTitle1);
        fillItem(binding.ivCover2, binding.tvTitle2);
        fillItem(binding.ivCover3, binding.tvTitle3);
        fillItem(binding.ivCover4, binding.tvTitle4);
        fillItem(binding.ivCover5, binding.tvTitle5);

        binding.tvMore.setOnClickListener(v ->
            Toast.makeText(GlobalApp.getContext(), "查看更多", Toast.LENGTH_SHORT).show());

        binding.tvChange.setOnClickListener(v -> show());
    }

    private SeriesCardBean getIncrementData() {
        this.showIndex++;
        if (this.showIndex < 0 || this.showIndex >= this.beans.size()) {
            this.showIndex = 0;
        }
        return this.beans.get(showIndex);
    }

    private void fillItem(ImageView imageView, TextView textView) {
        if (this.requestOptions == null) {
            int radius = imageView.getResources().getDimensionPixelSize(R.dimen.radius_l);
            this.requestOptions = new RequestOptions()
                .centerCrop()
                .error(R.drawable.placeholder_image_20_8)
                .transform(new CenterCrop(), new RoundedCorners(radius));
        }
        SeriesCardBean bean = getIncrementData();
        textView.setText(bean.getTitle());
        if (!TextUtils.isEmpty(bean.getImageUrl())) {
            Glide.with(imageView.getContext())
                .load(bean.getImageUrl())
                .apply(requestOptions)
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.placeholder_image_20_8);
        }

        View.OnClickListener onClickListener = v -> {
            if (listener == null) {
                return;
            }
            listener.onItemClick(v.getContext(), bean);
        };
        textView.setOnClickListener(onClickListener);
        imageView.setOnClickListener(onClickListener);
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        if (bindingRef != null) {
            CardSeriesBinding binding = bindingRef.get();
            if (binding != null) {
                binding.tvTitle1.setOnClickListener(null);
                binding.tvTitle2.setOnClickListener(null);
                binding.tvTitle3.setOnClickListener(null);
                binding.tvTitle4.setOnClickListener(null);
                binding.tvTitle5.setOnClickListener(null);
                binding.ivCover1.setOnClickListener(null);
                binding.ivCover2.setOnClickListener(null);
                binding.ivCover3.setOnClickListener(null);
                binding.ivCover4.setOnClickListener(null);
                binding.ivCover5.setOnClickListener(null);
                bindingRef.clear();
            }
            bindingRef = null;
        }
        beans = null;
        requestOptions = null;
        listener = null;
        showIndex = -1;
    }

    public static class Builder {
        private CardSeriesBinding binding;

        private OnItemClickListener listener;

        /**
         * 设置系列卡片布局viewbinding
         *
         * @param binding CardSeriesBinding
         * @ Builder
         */
        public SeriesCardWrapper.Builder setBinding(CardSeriesBinding binding) {
            this.binding = binding;
            return this;
        }

        /**
         * 设置点击监听
         *
         * @param listener {@link OnItemClickListener}
         * @return Builder
         */
        public SeriesCardWrapper.Builder setListener(OnItemClickListener listener) {
            this.listener = listener;
            return this;
        }

        public SeriesCardWrapper create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            SeriesCardWrapper wrapper = new SeriesCardWrapper();
            wrapper.bindingRef = new WeakReference<>(binding);
            wrapper.listener = this.listener;
            return wrapper;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Context context, SeriesCardBean bean);
    }
}

