package com.qiufengguang.ajstudy.card.setting;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.model.SettingCardBean;
import com.qiufengguang.ajstudy.databinding.ItemSettingBinding;
import com.qiufengguang.ajstudy.global.Constant;

import java.util.List;

/**
 * 设置卡片适配器
 *
 * @author qiufengguang
 * @since 2026/1/31 11:44
 */
public class SettingCardAdapter extends RecyclerView.Adapter<SettingCardAdapter.SettingViewHolder> {

    private List<SettingCardBean> beans;

    private OnItemClickListener<SettingCardBean> listener;

    private int spanCount = Constant.Pln.DEF_4;

    public SettingCardAdapter(@Nullable List<SettingCardBean> beans) {
        this.beans = beans;
    }

    public void setData(List<SettingCardBean> beans) {
        if (beans == null || beans.isEmpty()) {
            this.beans = beans;
            notifyItemRangeRemoved(0, getItemCount());
            return;
        }
        if (this.beans == null || this.beans.isEmpty()) {
            this.beans = beans;
            notifyItemRangeInserted(0, getItemCount());
        } else {
            this.beans = beans;
            notifyItemRangeChanged(0, getItemCount());
        }
    }

    public void setSpanCount(int spanCount) {
        if (spanCount <= 0) {
            return;
        }
        this.spanCount = spanCount;
    }

    public void setOnItemClickListener(OnItemClickListener<SettingCardBean> listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return beans == null ? 0 : beans.size();
    }

    @NonNull
    @Override
    public SettingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSettingBinding binding = ItemSettingBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new SettingViewHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SettingViewHolder holder, int position) {
        SettingCardBean bean = beans.get(position);
        holder.bind(bean, position >= beans.size() - spanCount);
    }

    public static class SettingViewHolder extends RecyclerView.ViewHolder {

        ItemSettingBinding binding;

        SettingCardBean bean;

        final Drawable arrowDrawable;

        int selectableItemBackground;

        public SettingViewHolder(
            @NonNull ItemSettingBinding binding,
            OnItemClickListener<SettingCardBean> clickListener
        ) {
            super(binding.getRoot());
            this.binding = binding;
            arrowDrawable = ContextCompat.getDrawable(this.binding.getRoot().getContext(),
                R.drawable.ic_chevron_right);
            try (TypedArray ta = this.binding.getRoot().getContext().obtainStyledAttributes(
                new int[]{android.R.attr.selectableItemBackground})) {
                selectableItemBackground = ta.getResourceId(0, 0);
            }
            this.binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null && bean != null
                    && !TextUtils.isEmpty(bean.getUri())) {
                    clickListener.onItemClick(v.getContext(), bean);
                }
            });
            binding.btnSwitch.setOnCheckedChangeListener(
                (buttonView, isChecked) -> {
                    if (clickListener != null && bean != null) {
                        clickListener.onCheckChange(buttonView.getContext(), bean, isChecked);
                    }
                });
        }

        public void bind(SettingCardBean bean, boolean isLastLine) {
            this.bean = bean;
            binding.tvTitle.setText(bean.getTitle());
            binding.divider.setVisibility(isLastLine ? View.GONE : View.VISIBLE);
            if (bean.isSwitchBtn()) {
                binding.btnSwitch.setVisibility(View.VISIBLE);
                binding.tvContent.setVisibility(View.GONE);
                return;
            }
            boolean contentEmpty = TextUtils.isEmpty(bean.getContent());
            if (contentEmpty) {
                binding.tvContent.setText("");
            } else {
                binding.tvContent.setText(bean.getContent());
            }
            boolean clickable = !TextUtils.isEmpty(bean.getUri());
            if (clickable) {
                binding.getRoot().setBackgroundResource(selectableItemBackground);
                binding.tvContent.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, arrowDrawable, null);
            } else {
                binding.getRoot().setBackground(null);
                binding.tvContent.setCompoundDrawablesRelativeWithIntrinsicBounds(null, null, null, null);
            }
            binding.btnSwitch.setVisibility(View.GONE);
            if (!contentEmpty || clickable) {
                binding.tvContent.setVisibility(View.VISIBLE);
            } else {
                binding.tvContent.setVisibility(View.GONE);
            }
        }
    }
}