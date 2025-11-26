package com.qiufengguang.ajstudy.ui.dashboard;

import android.graphics.Outline;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.DashboardBean;
import com.qiufengguang.ajstudy.databinding.DashboardListItemBinding;

import java.util.List;
import java.util.Objects;

public class DashboardAdapter extends RecyclerView.Adapter<DashboardAdapter.DashboardViewHolder> {

    private List<DashboardBean> beans;

    public DashboardAdapter(List<DashboardBean> beans) {
        this.beans = beans;
    }

    public void setData(List<DashboardBean> list) {
        if (this.beans == list) {
            return;
        }
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(
            new ItemDiffCallback(this.beans, list));
        this.beans = list;
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemCount() {
        return Objects.isNull(this.beans) ? 0 : this.beans.size();
    }

    @NonNull
    @Override
    public DashboardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new DashboardViewHolder(DashboardListItemBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DashboardViewHolder holder, int position) {
        if (getItemCount() <= 0) {
            return;
        }
        holder.binding.flContainer.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int radius = view.getResources().getDimensionPixelSize(
                    R.dimen.dashboard_item_img_radius);
                outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), radius);
            }
        });
        holder.binding.flContainer.setClipToOutline(true);
        int adapterPosition = holder.getAdapterPosition();
        holder.binding.divider.setVisibility(adapterPosition == 0 ? View.GONE : View.VISIBLE);
        DashboardBean bean = this.beans.get(adapterPosition);
        Glide.with(holder.binding.getRoot().getContext())
            .load(bean.getIcon())
            .error(R.drawable.item_icon_error)
            .into(holder.binding.ivIcon);
        holder.binding.tvTitle.setText(bean.getTitle());
        holder.binding.tvSubtitle.setText(bean.getSubtitle());
        holder.binding.tvBrief.setText(bean.getBrief());
        holder.binding.getRoot().setOnClickListener(v ->
            Toast.makeText(v.getContext(), bean.getTitle(), Toast.LENGTH_SHORT).show());
    }


    public static class DashboardViewHolder extends RecyclerView.ViewHolder {
        DashboardListItemBinding binding;

        public DashboardViewHolder(@NonNull DashboardListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public static class ItemDiffCallback extends DiffUtil.Callback {
        private final List<DashboardBean> oldList;
        private final List<DashboardBean> newList;

        public ItemDiffCallback(List<DashboardBean> oldList, List<DashboardBean> newList) {
            this.oldList = oldList;
            this.newList = newList;
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
            DashboardBean bean = this.oldList.get(oldItemPosition);
            if (Objects.isNull(bean)) {
                return false;
            }
            return bean.isSame(newList.get(newItemPosition));
        }
    }
}
