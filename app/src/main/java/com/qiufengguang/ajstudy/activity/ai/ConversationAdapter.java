package com.qiufengguang.ajstudy.activity.ai;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.data.model.Conversation;

import java.util.List;
import java.util.Objects;

/**
 * 历史会话适配器
 * [性能优化]：使用 ListAdapter + DiffUtil 实现增量刷新，彻底告别 notifyDataSetChanged。
 * submitList 会在后台线程计算差异，实现平滑的增量更新动画并提升列表性能。
 *
 * @author qiufengguang
 * @since 2026/5/4 16:07
 */
public class ConversationAdapter extends ListAdapter<Conversation, ConversationAdapter.ChatHistoryViewHolder> {
    private OnItemClickListener listener;

    public ConversationAdapter() {
        super(new DiffUtil.ItemCallback<>() {
            @Override
            public boolean areItemsTheSame(@NonNull Conversation oldItem, @NonNull Conversation newItem) {
                // 比较 ID 判定是否为同一个项
                return oldItem.getId() == newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Conversation oldItem, @NonNull Conversation newItem) {
                // 比较具体内容判定是否需要刷新局部 UI
                return Objects.equals(oldItem.getTitle(), newItem.getTitle()) &&
                        oldItem.getUpdatedAt() == newItem.getUpdatedAt();
            }
        });
    }

    /**
     * 设置数据：直接调用 submitList，内部会自动执行 Diff 算法并进行异步更新。
     */
    public void setData(List<Conversation> newData) {
        submitList(newData);
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ChatHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ai_conversation, parent, false);
        return new ChatHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatHistoryViewHolder holder, int position) {
        Conversation item = getItem(position);
        if (item != null) {
            holder.tvTitle.setText(item.getTitle());
            holder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(item);
                }
            });
        }
    }

    public static class ChatHistoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        public ChatHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(Conversation conversation);
    }
}
