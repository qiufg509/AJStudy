package com.qiufengguang.ajstudy.card.largegraphicgrid;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.card.largegraphic.LargeGraphicCard;
import com.qiufengguang.ajstudy.card.largegraphic.LargeGraphicCardHolder;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.base.SingleLayoutData;
import com.qiufengguang.ajstudy.databinding.CardLargeGraphicBinding;

import java.util.List;

/**
 * 大图文格网卡适配器
 *
 * @author qiufengguang
 * @since 2026/1/24 23:30
 */
public class LargeGraphicGridCardAdapter extends RecyclerView.Adapter<LargeGraphicCardHolder> {

    private List<LargeGraphicCardBean> beans;

    public LargeGraphicGridCardAdapter(@Nullable List<LargeGraphicCardBean> beans) {
        this.beans = beans;
    }

    public void setData(List<LargeGraphicCardBean> beans) {
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

    @Override
    public int getItemCount() {
        return beans == null ? 0 : beans.size();
    }

    @NonNull
    @Override
    public LargeGraphicCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardLargeGraphicBinding binding = CardLargeGraphicBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new LargeGraphicCardHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull LargeGraphicCardHolder holder, int position) {
        LargeGraphicCardBean bean = beans.get(position);
        SingleLayoutData<LargeGraphicCardBean> layoutData =
            LayoutDataFactory.createSingle(LargeGraphicCard.LAYOUT_ID, bean);
        holder.bind(layoutData);
    }
}