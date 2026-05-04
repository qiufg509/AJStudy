package com.qiufengguang.ajstudy.fragment.base;

import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;
import com.qiufengguang.ajstudy.data.base.CollectionLayoutData;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.data.base.SingleLayoutData;
import com.qiufengguang.ajstudy.data.model.BannerBean;

import java.util.List;
import java.util.Objects;

/**
 * 页面数据差异化更新回调
 *
 * @author qiufengguang
 * @since 2026/3/9 23:03
 */
public class DiffItemCallback extends DiffUtil.ItemCallback<LayoutData<?>> {

    @Override
    public boolean areItemsTheSame(@NonNull LayoutData<?> oldItem, @NonNull LayoutData<?> newItem) {
        // 布局类型不同，肯定不是同一项
        if (oldItem.getLayoutId() != newItem.getLayoutId()) {
            return false;
        }

        String oldId = extractItemId(oldItem);
        String newId = extractItemId(newItem);

        // 只有双方都能提取到非空 ID 时才比较，否则视为不同项
        return oldId != null && oldId.equals(newId);
    }

    @Nullable
    private String extractItemId(LayoutData<?> item) {
        // 优先使用 LayoutData 自身的 detailId（如“更多”链接）
        String id = item.getDetailId();
        if (!TextUtils.isEmpty(id)) {
            return id;
        }

        // 从数据体中提取 detailId
        Object data = item.getData();
        if (data == null) {
            return null;
        }
        if (item instanceof SingleLayoutData) {
            if (data instanceof BaseCardBean) {
                return ((BaseCardBean) data).getDetailId();
            }
        } else if (item instanceof CollectionLayoutData) {
            if (data instanceof List<?> list) {
                if (!list.isEmpty() && list.get(0) instanceof BaseCardBean bean) {
                    return bean.getDetailId();
                }
            }
        }
        return null;
    }

    @Override
    public boolean areContentsTheSame(@NonNull LayoutData<?> oldItem, @NonNull LayoutData<?> newItem) {
        // 依赖子类正确实现 equals 方法（需比较核心数据）
        return oldItem.equals(newItem);
    }

    @Nullable
    @Override
    public Object getChangePayload(@NonNull LayoutData<?> oldItem, @NonNull LayoutData<?> newItem) {
        Bundle diff = new Bundle();
        if (!Objects.equals(oldItem.getName(), newItem.getName())) {
            diff.putString("name", newItem.getName());
        }

        if (!oldItem.isCollection() && !newItem.isCollection()) {
            BaseCardBean oldData = (BaseCardBean) oldItem.getData();
            BaseCardBean newData = (BaseCardBean) newItem.getData();
            compareSingleData(diff, oldData, newData);
        } else if (oldItem.isCollection() && newItem.isCollection()) {
            @SuppressWarnings("unchecked")
            List<BaseCardBean> oldDataList = (List<BaseCardBean>) oldItem.getData();
            @SuppressWarnings("unchecked")
            List<BaseCardBean> newDataList = (List<BaseCardBean>) newItem.getData();
            if (!Objects.equals(oldDataList, newDataList)) {
                diff.putBoolean("dataChanged", true);
            }
        }
        return diff.isEmpty() ? null : diff;
    }

    private void compareSingleData(Bundle diff, BaseCardBean oldBean, BaseCardBean newBean) {
        if (oldBean == null || newBean == null) {
            return;
        }

        if (!Objects.equals(oldBean.getTitle(), newBean.getTitle())) {
            diff.putString("title", newBean.getTitle());
        }
        // 子类特有字段（实际应用中可扩展）
        if (oldBean instanceof BannerBean oldBanner
            && newBean instanceof BannerBean newBanner) {
            if (!Objects.equals(oldBanner.getImageUrl(), newBanner.getImageUrl())) {
                diff.putString("imageUrl", newBanner.getImageUrl());
            }
        }
    }
}