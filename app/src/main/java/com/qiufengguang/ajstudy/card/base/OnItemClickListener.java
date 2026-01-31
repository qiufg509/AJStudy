package com.qiufengguang.ajstudy.card.base;

import android.content.Context;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

/**
 * 点击回调
 *
 * @author qiufengguang
 * @since 2026/1/31 11:06
 */
public interface OnItemClickListener<T extends BaseCardBean> {
    void onItemClick(Context context, T data);

    default void onItemClick(Context context, int position, T data) {
        onItemClick(context, data);
    }

    default void onCheckChange(Context context, T data, boolean isChecked) {
    }
}
