package com.qiufengguang.ajstudy.ui.knowhow;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.activity.detail.DetailActivity;
import com.qiufengguang.ajstudy.data.KnowHowBean;
import com.qiufengguang.ajstudy.databinding.ItemKnowHowBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 知识列表页适配器
 *
 * @author qiufengguang
 * @since 2025/11/26 22:19
 */
public class KnowHowAdapter extends RecyclerView.Adapter<KnowHowAdapter.KnowHowViewHolder> {
    private List<KnowHowBean> beans;

    public KnowHowAdapter(List<KnowHowBean> beans) {
        this.beans = beans;
    }

    public void setData(List<KnowHowBean> list) {
        if (Objects.equals(this.beans, list)) {
            return;
        }
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
            new ItemDiffCallback(this.beans, list), true);
        this.beans = list;
        diffResult.dispatchUpdatesTo(this);
    }

    public void addData(List<KnowHowBean> list) {
        if (list == null || list.isEmpty()) {
            return;
        }
        if (this.beans == null) {
            this.beans = list;
        } else {
            this.beans.addAll(list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return Objects.isNull(this.beans) ? 0 : this.beans.size();
    }

    @NonNull
    @Override
    public KnowHowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new KnowHowViewHolder(ItemKnowHowBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull KnowHowViewHolder holder, int position) {
        int adapterPosition = holder.getAdapterPosition();
        KnowHowBean bean = this.beans.get(adapterPosition);
        holder.bind(bean);
    }


    public static class KnowHowViewHolder extends RecyclerView.ViewHolder {
        private final ItemKnowHowBinding binding;

        public KnowHowViewHolder(@NonNull ItemKnowHowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        /**
         * 绑定数据到View，避免重复设置相同属性
         */
        public void bind(KnowHowBean bean) {
            // 优化Glide加载，添加错误处理和进度指示
            if (!TextUtils.isEmpty(bean.getIcon())) {
                Glide.with(binding.getRoot().getContext())
                    .load(bean.getIcon())
                    .diskCacheStrategy(DiskCacheStrategy.DATA)
                    .placeholder(R.drawable.icon_placeholder)
                    .into(binding.ivIcon);
            } else {
                binding.ivIcon.setImageResource(R.drawable.icon_placeholder);
            }

            CharSequence title = binding.tvTitle.getText();
            if (title == null || !title.equals(bean.getTitle())) {
                binding.tvTitle.setText(bean.getTitle());
            }

            CharSequence subtitle = binding.tvSubtitle.getText();
            if (subtitle == null || !subtitle.equals(bean.getSubtitle())) {
                binding.tvSubtitle.setText(bean.getSubtitle());
            }

            CharSequence brief = binding.tvBrief.getText();
            if (brief == null || !brief.equals(bean.getBrief())) {
                binding.tvBrief.setText(bean.getBrief());
            }

            // 优化点击事件，使用单个监听器
            binding.getRoot().setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, DetailActivity.class);
                context.startActivity(intent);
            });
        }
    }

    private static class ItemDiffCallback extends DiffUtil.Callback {
        private List<KnowHowBean> oldList;
        private List<KnowHowBean> newList;

        public ItemDiffCallback(List<KnowHowBean> oldList, List<KnowHowBean> newList) {
            if (oldList != null) {
                this.oldList = new ArrayList<>(oldList);
            }
            if (newList != null) {
                this.newList = new ArrayList<>(newList);
            }
        }

        @Override
        public int getOldListSize() {
            return Objects.isNull(oldList) ? 0 : oldList.size();
        }

        @Override
        public int getNewListSize() {
            return Objects.isNull(newList) ? 0 : newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            if (Objects.isNull(this.oldList) || Objects.isNull(this.newList)
                || oldItemPosition >= this.oldList.size()
                || newItemPosition >= this.newList.size()) {
                return false;
            }
            return this.oldList.get(oldItemPosition) == this.newList.get(newItemPosition);
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            if (Objects.isNull(this.oldList) || Objects.isNull(this.newList)
                || oldItemPosition >= this.oldList.size()
                || newItemPosition >= this.newList.size()) {
                return false;
            }
            KnowHowBean bean = this.oldList.get(oldItemPosition);
            if (Objects.isNull(bean)) {
                return false;
            }
            return bean.isSame(newList.get(newItemPosition));
        }
    }
}
