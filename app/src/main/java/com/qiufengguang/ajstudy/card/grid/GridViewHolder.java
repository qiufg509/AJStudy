package com.qiufengguang.ajstudy.card.grid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.activity.main.MainActivity;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.GridCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardGridBinding;
import com.qiufengguang.ajstudy.fragment.second.SecondViewModel;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * 格网卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/19 14:03
 */
public class GridViewHolder extends BaseViewHolder<CardGridBinding> {

    private GridCard card;

    private WeakReference<SecondViewModel> viewModelRef;

    public GridViewHolder(@NonNull CardGridBinding binding) {
        super(binding);
        Context context = binding.getRoot().getContext();
        if (!(context instanceof AppCompatActivity)) {
            return;
        }
        AppCompatActivity activity = (AppCompatActivity) context;
        SecondViewModel viewModel =
            new ViewModelProvider(activity).get(SecondViewModel.class);
        viewModelRef = new WeakReference<>(viewModel);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        int spacing = itemView.getResources().getDimensionPixelSize(
            R.dimen.activity_horizontal_margin_s);
        card = new GridCard.Builder()
            .setRecyclerView(binding.recyclerGrid)
            .setTitleView(binding.tvTitle)
            .setHorizontalSpacing(spacing)
            .setListener(this::onItemClickListener)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || !data.isCollection()
            || !TextUtils.equals(data.getLayoutName(), GridCardBean.LAYOUT_NAME)) {
            return;
        }
        if (card == null) {
            initCard();
        }
        @SuppressWarnings("unchecked")
        List<GridCardBean> beans = (List<GridCardBean>) data.getData();
        card.setData(beans, data.getCardTitle());
    }

    private void onItemClickListener(Context context, GridCardBean bean) {
        if (!(context instanceof AppCompatActivity)) {
            return;
        }
        if (bean.getItemType() == GridCardBean.TYPE_IMAGE) {
            if (viewModelRef == null) {
                return;
            }
            SecondViewModel viewModel = viewModelRef.get();
            if (viewModel == null) {
                return;
            }
            viewModel.saveThemeIndex(bean);

            Intent intent = new Intent(context, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("restart_theme", true);
            context.startActivity(intent);
            ((AppCompatActivity) context).finish();
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
        if (card != null) {
            card.release();
            card = null;
        }
        if (viewModelRef != null) {
            viewModelRef.clear();
            viewModelRef = null;
        }
        super.cleanup();
    }
}