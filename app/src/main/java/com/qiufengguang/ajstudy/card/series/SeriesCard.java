package com.qiufengguang.ajstudy.card.series;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.SeriesCardBean;
import com.qiufengguang.ajstudy.databinding.CardSeriesBinding;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 系列卡片
 *
 * @author qiufengguang
 * @since 2026/1/25 15:44
 */
public class SeriesCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 5;

    private String cardTitle;

    private List<SeriesCardBean> beans;

    private WeakReference<CardSeriesBinding> bindingRef;

    private OnItemClickListener<SeriesCardBean> listener;

    private int showIndex = -1;

    private RequestOptions requestOptions;

    private SeriesCard() {
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
        fillItem(binding.ivCover1, binding.tvTitle1, binding.tvTag1, binding.tvCount1, binding.tvDuration1);
        fillItem(binding.ivCover2, binding.tvTitle2, binding.tvTag2, binding.tvCount2, binding.tvDuration2);
        fillItem(binding.ivCover3, binding.tvTitle3, binding.tvTag3, binding.tvCount3, binding.tvDuration3);
        fillItem(binding.ivCover4, binding.tvTitle4, binding.tvTag4, binding.tvCount4, binding.tvDuration4);
        fillItem(binding.ivCover5, binding.tvTitle5, binding.tvTag5, binding.tvCount5, binding.tvDuration5);

        binding.tvMore.setOnClickListener(v -> {
            if (listener == null) {
                return;
            }
            listener.onItemClick(v.getContext(), null);
        });

        binding.tvChange.setOnClickListener(v -> show());
    }

    private SeriesCardBean getIncrementData() {
        this.showIndex++;
        if (this.showIndex < 0 || this.showIndex >= this.beans.size()) {
            this.showIndex = 0;
        }
        return this.beans.get(showIndex);
    }

    private void fillItem(ImageView imageView, TextView textView, TextView tvTag, TextView tvCount, TextView tvDuration) {
        if (this.requestOptions == null) {
            int radius = imageView.getResources().getDimensionPixelSize(R.dimen.radius_m);
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
        if (TextUtils.isEmpty(bean.getTag())) {
            tvTag.setVisibility(View.GONE);
        } else {
            tvTag.setVisibility(View.VISIBLE);
            tvTag.setText(bean.getTag());
        }
        if (TextUtils.isEmpty(bean.getViewCount())) {
            tvCount.setVisibility(View.GONE);
        } else {
            tvCount.setVisibility(View.VISIBLE);
            tvCount.setText(bean.getViewCount());
        }
        if (TextUtils.isEmpty(bean.getTotalDuration())) {
            tvDuration.setVisibility(View.GONE);
        } else {
            tvDuration.setVisibility(View.VISIBLE);
            tvDuration.setText(bean.getTotalDuration());
        }
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

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardSeriesBinding binding = CardSeriesBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new SeriesCardHolder(binding);
        }
    }

    public static class Builder {
        private CardSeriesBinding binding;

        private OnItemClickListener<SeriesCardBean> listener;

        /**
         * 设置卡片布局viewbinding
         *
         * @param binding CardSeriesBinding
         * @ Builder
         */
        public SeriesCard.Builder setBinding(CardSeriesBinding binding) {
            this.binding = binding;
            return this;
        }

        /**
         * 设置点击监听
         *
         * @param listener {@link OnItemClickListener}
         * @return Builder
         */
        public SeriesCard.Builder setListener(OnItemClickListener<SeriesCardBean> listener) {
            this.listener = listener;
            return this;
        }

        public SeriesCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            SeriesCard wrapper = new SeriesCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            wrapper.listener = this.listener;
            return wrapper;
        }
    }
}

