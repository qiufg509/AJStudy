package com.qiufengguang.ajstudy.card.grid;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.GridDecoration;
import com.qiufengguang.ajstudy.card.base.OnItemClickListener;
import com.qiufengguang.ajstudy.data.model.GridCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardGridBinding;
import com.qiufengguang.ajstudy.fragment.second.SecondFragment;
import com.qiufengguang.ajstudy.router.AppNavigator;
import com.qiufengguang.ajstudy.router.Router;
import com.qiufengguang.ajstudy.utils.ThemeUtils;

import java.util.List;

/**
 * 格网卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/19 14:03
 */
public class GridViewHolder extends BaseViewHolder<CardGridBinding> {

    private GridCard card;

    public GridViewHolder(@NonNull CardGridBinding binding) {
        super(binding);
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
            .setSpacingBuilder(
                new GridDecoration.Builder().horizontalSpacing(spacing)
            )
            .setListener(new OnItemClickListener<>() {
                @Override
                public void onItemClick(Context context, GridCardBean data) {
                    onItemClickListener(context, data);
                }

                @Override
                public void onItemClick(Context context, int position, GridCardBean data) {
                    onItemClickListener(context, position, data);
                }
            })
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || !data.isCollection()
            || data.getLayoutId() != GridCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        @SuppressWarnings("unchecked")
        List<GridCardBean> beans = (List<GridCardBean>) data.getData();
        card.setData(beans, data.getCardTitle());
    }

    private void onItemClickListener(Context context, int position, GridCardBean bean) {
        if (!(context instanceof AppCompatActivity)) {
            return;
        }
        if (bean.getItemType() != GridCardBean.TYPE_IMAGE) {
            return;
        }
        ThemeUtils.setSelectedThemeIndex(position);

        AppNavigator.getInstance().restart(context, Router.EXTRA_RESTART);
        ((AppCompatActivity) context).finish();
    }

    private void onItemClickListener(Context context, GridCardBean bean) {
        if (bean.getItemType() != GridCardBean.TYPE_TEXT) {
            return;
        }
        Bundle args = new Bundle();
        String uri = bean.getUri();
        args.putString(Router.EXTRA_URI, uri);
        args.putString(Router.EXTRA_TITLE, bean.getTitle());
        args.putString(SecondFragment.ARG_NAVIGATE_TO, bean.getNavigateTo());
        AppNavigator.getInstance().startSecondActivity(context, args);
    }

    @Override
    public void cleanup() {
        if (card != null) {
            card.release();
            card = null;
        }
        super.cleanup();
    }
}