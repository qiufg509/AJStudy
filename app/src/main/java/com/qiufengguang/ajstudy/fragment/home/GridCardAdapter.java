package com.qiufengguang.ajstudy.fragment.home;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.CardGridItemBinding;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.List;
import java.util.Map;

public class GridCardAdapter extends RecyclerView.Adapter<GridCardAdapter.GridCardHolder> {

    private static final List<Map.Entry<String, Integer>> DATA_LIST = List.of(
        new SimpleImmutableEntry<>("key2", R.drawable.ic_book_0),
        new SimpleImmutableEntry<>("key2", R.drawable.ic_book_1),
        new SimpleImmutableEntry<>("key2", R.drawable.ic_book_2),
        new SimpleImmutableEntry<>("key2", R.drawable.ic_book_3),
        new SimpleImmutableEntry<>("key2", R.drawable.ic_book_4),
        new SimpleImmutableEntry<>("key2", R.drawable.ic_book_5),
        new SimpleImmutableEntry<>("key2", R.drawable.ic_book_6),
        new SimpleImmutableEntry<>("key1", R.drawable.ic_book_7),
        new SimpleImmutableEntry<>("key2", R.drawable.ic_book_8),
        new SimpleImmutableEntry<>("key3", R.drawable.ic_book_9)
    );

    private OnItemClickListener listener;

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public GridCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardGridItemBinding binding = CardGridItemBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new GridCardHolder(binding, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull GridCardHolder holder, int position) {
        Map.Entry<String, Integer> entry = DATA_LIST.get(position);
        holder.bind(entry);
    }

    @Override
    public int getItemCount() {
        return DATA_LIST.size();
    }

    public static class GridCardHolder extends RecyclerView.ViewHolder {

        CardGridItemBinding binding;

        public GridCardHolder(@NonNull CardGridItemBinding binding, OnItemClickListener clickListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.binding.getRoot().setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(0);
                }
            });
        }

        public void bind(Map.Entry<String, Integer> entry) {
            binding.getRoot().setText(entry.getKey());
            Drawable drawable = ContextCompat.getDrawable(
                this.binding.getRoot().getContext(), entry.getValue());
            binding.getRoot().setCompoundDrawablesRelativeWithIntrinsicBounds(
                null, drawable, null, null);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }
}