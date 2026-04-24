package com.qiufengguang.ajstudy.card.series;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.model.SeriesCardBean;
import com.qiufengguang.ajstudy.databinding.CardSeriesBinding;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;

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

    private SeriesCard() {
    }

    public void setData(List<SeriesCardBean> beans, String cardTitle) {
        // 如果数据和标题都没变，不执行后续逻辑
        if (Objects.equals(this.beans, beans) && Objects.equals(this.cardTitle, cardTitle)) {
            return;
        }
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

        // 重置 showIndex 确保每次 show 都是从头取数据（除非是点击“换一换”）
        // 但为了保持原有的“换一换”逻辑，这里不重置 showIndex

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
        SeriesCardBean bean = getIncrementData();

        // 1. 文本防重复设置
        if (!Objects.equals(textView.getText(), bean.getTitle())) {
            textView.setText(bean.getTitle());
        }

        // 2. 图片防重复加载：通过 Tag 记录当前加载的 URL
        String url = bean.getImageUrl();
        Object lastUrl = imageView.getTag(R.id.iv_cover_1); // 借用一个已有的 id 作为 tag 的 key
        if (!Objects.equals(lastUrl, url)) {
            imageView.setTag(R.id.iv_cover_1, url);
            if (!TextUtils.isEmpty(url)) {
                Glide.with(imageView.getContext())
                    .load(url)
                    .centerCrop()
                    .placeholder(R.drawable.placeholder_image_20_8)
                    .into(imageView);
            } else {
                imageView.setImageResource(R.drawable.placeholder_image_20_8);
            }
        }

        View.OnClickListener onClickListener = v -> {
            if (listener == null) {
                return;
            }
            listener.onItemClick(v.getContext(), bean);
        };
        textView.setOnClickListener(onClickListener);
        imageView.setOnClickListener(onClickListener);

        // 3. 显隐控制优化
        updateTextVisibility(tvTag, bean.getTag());
        updateTextVisibility(tvCount, bean.getViewCount());
        updateTextVisibility(tvDuration, bean.getTotalDuration());
    }

    private void updateTextVisibility(TextView tv, String text) {
        if (TextUtils.isEmpty(text)) {
            if (tv.getVisibility() != View.GONE) {
                tv.setVisibility(View.GONE);
            }
        } else {
            if (tv.getVisibility() != View.VISIBLE) {
                tv.setVisibility(View.VISIBLE);
            }
            if (!Objects.equals(tv.getText(), text)) {
                tv.setText(text);
            }
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
                Context context = binding.getRoot().getContext();
                releaseImage(context, binding);

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
        listener = null;
        showIndex = -1;
    }

    private static void releaseImage(Context context, CardSeriesBinding binding) {
        if (!(context instanceof AppCompatActivity)) {
            return;
        }
        AppCompatActivity activity = (AppCompatActivity) context;
        if (activity.isFinishing() || activity.isDestroyed()) {
            return;
        }
        Glide.with(context).clear(binding.ivCover1);
        Glide.with(context).clear(binding.ivCover2);
        Glide.with(context).clear(binding.ivCover3);
        Glide.with(context).clear(binding.ivCover4);
        Glide.with(context).clear(binding.ivCover5);
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
