package com.qiufengguang.ajstudy.activity.detail;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.qiufengguang.ajstudy.databinding.ItemIntroductionBinding;

import java.util.ArrayList;
import java.util.List;

public class IntroductionAdapter extends RecyclerView.Adapter<IntroductionAdapter.ViewHolder> {

    private List<DetailViewModel.Introduction> introductionList = new ArrayList<>();

    public void setIntroduction(List<DetailViewModel.Introduction> introductionList) {
        this.introductionList = introductionList != null ? introductionList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemIntroductionBinding binding = ItemIntroductionBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(introductionList.get(position));
    }

    @Override
    public int getItemCount() {
        return introductionList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ItemIntroductionBinding binding;

        public ViewHolder(ItemIntroductionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DetailViewModel.Introduction introduction) {
            binding.tvTitle.setText(introduction.getTitle());
            binding.tvContent.setText(introduction.getContent());
        }
    }
}
