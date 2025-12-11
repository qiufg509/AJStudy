package com.qiufengguang.ajstudy.ui.introduction;


import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.activity.detail.DetailViewModel;
import com.qiufengguang.ajstudy.databinding.ItemIntroductionBinding;

import java.util.ArrayList;
import java.util.List;

public class IntroductionAdapter extends RecyclerView.Adapter<IntroductionAdapter.IntroductionViewHolder> {

    private List<DetailViewModel.Introduction> introductionList = new ArrayList<>();

    public void setIntroduction(List<DetailViewModel.Introduction> introductionList) {
        this.introductionList = introductionList != null ? introductionList : new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IntroductionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemIntroductionBinding binding = ItemIntroductionBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new IntroductionViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull IntroductionViewHolder holder, int position) {
        holder.bind(introductionList.get(position));
    }

    @Override
    public int getItemCount() {
        return introductionList.size();
    }

    public static class IntroductionViewHolder extends RecyclerView.ViewHolder {

        private final ItemIntroductionBinding binding;

        public IntroductionViewHolder(ItemIntroductionBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void bind(DetailViewModel.Introduction introduction) {
            binding.tvTitle.setText(introduction.getTitle());
            binding.tvContent.setText(introduction.getContent());
        }
    }
}
