package com.qiufengguang.ajstudy.card.graphics;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.model.GraphicCardBean;
import com.qiufengguang.ajstudy.databinding.CardGraphicSBinding;
import com.qiufengguang.ajstudy.global.Constant;

import java.lang.ref.WeakReference;
import java.util.Map;

/**
 * 小尺寸图文卡
 *
 * @author qiufengguang
 * @since 2026/2/26 0:12
 */
public class GraphicCardS extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 13;

    private WeakReference<View> rootRef;
    private WeakReference<TextView> titleRef;
    private WeakReference<TextView> subTitleRef;
    private WeakReference<ImageView> picRef;

    private GraphicCardBean bean;

    private OnItemClickListener<GraphicCardBean> listener;

    private GraphicCardS() {
    }

    public void setData(GraphicCardBean bean) {
        this.bean = bean;
        show();
    }

    public void show() {
        if (bean == null) {
            return;
        }
        if (titleRef != null) {
            TextView view = titleRef.get();
            if (view != null) {
                view.setText(bean.getTitle());
            }
        }

        if (subTitleRef != null) {
            TextView view = subTitleRef.get();
            if (view != null) {
                view.setText(bean.getSubtitle());
            }
        }

        if (rootRef != null) {
            View view = rootRef.get();
            if (view != null) {
                view.setOnClickListener(v -> {
                    if (listener != null && bean != null) {
                        listener.onItemClick(v.getContext(), bean);
                    }
                });
            }
        }

        if (picRef == null) {
            return;
        }
        ImageView imageView = picRef.get();
        if (imageView == null) {
            return;
        }
        if (!TextUtils.isEmpty(bean.getImageUrl())) {
            Glide.with(imageView.getContext())
                .load(bean.getImageUrl())
                .transition(DrawableTransitionOptions.withCrossFade(300))
                .into(imageView);
        } else {
            imageView.setImageResource(R.drawable.placeholder_image_1_1);
        }
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardGraphicSBinding binding = CardGraphicSBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new GraphicCardSHolder(binding);
        }

        @Override
        public Map<Integer, Integer> getSpanSize() {
            return getSpanSizeMap(
                Constant.Pln.GRAPHIC_M_4,
                Constant.Pln.GRAPHIC_M_8,
                Constant.Pln.GRAPHIC_M_12
            );
        }
    }

    public static class Builder {
        private View root;
        private TextView tvTitle;
        private TextView tvSubTitle;
        private ImageView ivPic;

        private OnItemClickListener<GraphicCardBean> listener;

        /**
         * 设置卡片根布局
         *
         * @param root View
         * @return Builder
         */
        public GraphicCardS.Builder setRoot(View root) {
            this.root = root;
            return this;
        }

        /**
         * 设置卡片标题控件
         *
         * @param tvTitle TextView
         * @return Builder
         */
        public GraphicCardS.Builder setTvTitle(TextView tvTitle) {
            this.tvTitle = tvTitle;
            return this;
        }

        /**
         * 设置卡片子标题控件
         *
         * @param tvSubTitle TextView
         * @return Builder
         */
        public GraphicCardS.Builder setTvSubTitle(TextView tvSubTitle) {
            this.tvSubTitle = tvSubTitle;
            return this;
        }

        /**
         * 设置卡片图片控件
         *
         * @param ivPic ImageView
         * @return Builder
         */
        public GraphicCardS.Builder setIvPic(ImageView ivPic) {
            this.ivPic = ivPic;
            return this;
        }


        /**
         * 设置卡片点击事件
         *
         * @param listener {@link OnItemClickListener}
         * @return Builder
         */
        public GraphicCardS.Builder setListener(
            OnItemClickListener<GraphicCardBean> listener) {
            this.listener = listener;
            return this;
        }

        public GraphicCardS create() {
            if (this.tvTitle == null) {
                throw new UnsupportedOperationException(
                    "tvTitle is null, call setTvTitle first.");
            }
            GraphicCardS wrapper = new GraphicCardS();
            if (this.root != null) {
                wrapper.rootRef = new WeakReference<>(this.root);
            }
            wrapper.titleRef = new WeakReference<>(this.tvTitle);
            if (this.tvSubTitle != null) {
                wrapper.subTitleRef = new WeakReference<>(this.tvSubTitle);
            }
            if (this.ivPic != null) {
                wrapper.picRef = new WeakReference<>(this.ivPic);
            }
            wrapper.listener = this.listener;
            return wrapper;
        }
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        if (titleRef != null) {
            titleRef.clear();
            titleRef = null;
        }
        if (subTitleRef != null) {
            subTitleRef.clear();
            subTitleRef = null;
        }
        if (picRef != null) {
            picRef.clear();
            picRef = null;
        }
    }
}
