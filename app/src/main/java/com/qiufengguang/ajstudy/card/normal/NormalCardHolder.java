package com.qiufengguang.ajstudy.card.normal;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.activity.detail.DetailActivity;
import com.qiufengguang.ajstudy.activity.markdown.MarkdownActivity;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.NormalCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardNormalBinding;

/**
 * 普通卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/29 19:40
 */
public class NormalCardHolder extends BaseViewHolder<CardNormalBinding> {

    private NormalCard cardWrapper;

    public NormalCardHolder(@NonNull CardNormalBinding binding) {
        super(binding);
    }

    @Override
    public void initCardWrapper() {
        if (cardWrapper != null) {
            return;
        }
        cardWrapper = new NormalCard.Builder()
            .setBinding(binding)
            .setListener(this::onItemClickListener)
            .create();
        cardWrapper.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || !TextUtils.equals(data.getLayoutName(), NormalCardBean.LAYOUT_NAME)) {
            return;
        }
        if (cardWrapper == null) {
            initCardWrapper();
        }
        NormalCardBean bean = (NormalCardBean) data.getData();
        cardWrapper.setData(bean);
    }

    private void onItemClickListener(Context context, NormalCardBean bean) {
        String titleStr = bean.getTitle();
        String targetPage = bean.getTargetPage();
        if (!TextUtils.isEmpty(targetPage) && !TextUtils.isEmpty(titleStr)) {
            Intent intent = new Intent(context, MarkdownActivity.class);
            String titleContent = titleStr.replaceAll("\\.(md|txt|json|xml)$", "");
            intent.putExtra("title", titleContent);
            intent.putExtra("filePath",
                targetPage + "/" + titleStr);
            context.startActivity(intent);
            return;
        }
        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra("detail_index", bean.getId());
        context.startActivity(intent);
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