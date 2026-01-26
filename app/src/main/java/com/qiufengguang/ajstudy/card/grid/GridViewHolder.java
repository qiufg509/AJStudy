package com.qiufengguang.ajstudy.card.grid;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.GridCardBean;
import com.qiufengguang.ajstudy.data.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardGridBinding;

import java.util.List;

/**
 * 格网卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/19 14:03
 */
public class GridViewHolder extends BaseViewHolder<CardGridBinding> {

    private GridCardWrapper cardWrapper;

    public GridViewHolder(@NonNull CardGridBinding binding) {
        super(binding);
    }

    @Override
    public void initCardWrapper() {
        if (cardWrapper != null) {
            return;
        }
        int spacing = itemView.getResources().getDimensionPixelSize(
            R.dimen.activity_horizontal_margin_s);
        cardWrapper = new GridCardWrapper.Builder()
            .setRecyclerView(binding.getRoot())
            .setItemType(GridCardWrapper.TYPE_TEXT)
            .setHorizontalSpacing(spacing)
            .setListener(GridViewHolder::onItemClickListener)
            .create();
        cardWrapper.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getBeans() == null) {
            return;
        }
        if (cardWrapper == null) {
            initCardWrapper();
        }
        @SuppressWarnings("unchecked")
        List<GridCardBean> beans = (List<GridCardBean>) data.getBeans();
        cardWrapper.setData(beans);
    }

    private static void onItemClickListener(Context context, GridCardBean bean) {
        if (!(context instanceof AppCompatActivity)) {
            return;
        }
        AppCompatActivity activity = (AppCompatActivity) context;
        NavController navController = Navigation.findNavController(activity,
            R.id.nav_host_fragment_activity_main);
        Bundle bundle = new Bundle();
        bundle.putString("Course", bean.getNavigatePage());
        navController.navigate(R.id.navigation_know_how, bundle);
    }

    @Override
    public void cleanup() {
        if (cardWrapper != null) {
            cardWrapper.release();
            cardWrapper = null;
        }
        super.cleanup();
    }
}