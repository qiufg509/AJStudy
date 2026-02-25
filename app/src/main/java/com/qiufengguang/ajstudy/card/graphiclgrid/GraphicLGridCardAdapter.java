package com.qiufengguang.ajstudy.card.graphiclgrid;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.card.graphicl.GraphicCardL;
import com.qiufengguang.ajstudy.card.graphicl.GraphicCardLHolder;
import com.qiufengguang.ajstudy.data.LargeGraphicCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutDataFactory;
import com.qiufengguang.ajstudy.data.base.SingleLayoutData;
import com.qiufengguang.ajstudy.databinding.CardGraphicLBinding;

import java.util.List;

/**
 * 大图文格网卡适配器
 *
 * @author qiufengguang
 * @since 2026/1/24 23:30
 */
public class GraphicLGridCardAdapter extends RecyclerView.Adapter<GraphicCardLHolder> {

    private List<LargeGraphicCardBean> beans;

    public GraphicLGridCardAdapter(@Nullable List<LargeGraphicCardBean> beans) {
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
    public GraphicCardLHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CardGraphicLBinding binding = CardGraphicLBinding.inflate(
            LayoutInflater.from(parent.getContext()), parent, false);
        return new GraphicCardLHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GraphicCardLHolder holder, int position) {
        LargeGraphicCardBean bean = beans.get(position);
        SingleLayoutData<LargeGraphicCardBean> layoutData =
            LayoutDataFactory.createSingle(GraphicCardL.LAYOUT_ID, bean);
        holder.bind(layoutData);
    }
}