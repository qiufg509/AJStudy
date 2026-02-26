package com.qiufengguang.ajstudy.card.normal;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.model.NormalCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardNormalBinding;
import com.qiufengguang.ajstudy.router.AppNavigator;
import com.qiufengguang.ajstudy.router.Router;

/**
 * 普通卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/29 19:40
 */
public class NormalCardHolder extends BaseViewHolder<CardNormalBinding> {

    private NormalCard card;

    public NormalCardHolder(@NonNull CardNormalBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new NormalCard.Builder()
            .setBinding(binding)
            .setListener(this::onItemClickListener)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != NormalCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        NormalCardBean bean = (NormalCardBean) data.getData();
        card.setData(bean);
    }

    private void onItemClickListener(Context context, NormalCardBean bean) {
        String uri = bean.getUri();
        if (TextUtils.equals(Router.URI.PAGE_ARTICLE_LIST, uri)) {
            AppNavigator.getInstance().startArticleActivity(context, bean.getNavigateTo(), bean.getTitle());
            return;
        }
        AppNavigator.getInstance().startDetailActivity(context, uri, bean.getTitle());
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