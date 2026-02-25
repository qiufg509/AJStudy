package com.qiufengguang.ajstudy.card.article;

import android.content.Context;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.ArticleCardBean;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardArticleBinding;
import com.qiufengguang.ajstudy.router.AppNavigator;

/**
 * 文章卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/2/7 0:42
 */
public class ArticleCardHolder extends BaseViewHolder<CardArticleBinding> {

    private ArticleCard card;

    public ArticleCardHolder(@NonNull CardArticleBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new ArticleCard.Builder()
            .setBinding(binding)
            .setListener(this::onItemClickListener)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || data.getLayoutId() != ArticleCard.LAYOUT_ID) {
            return;
        }
        if (card == null) {
            initCard();
        }
        ArticleCardBean bean = (ArticleCardBean) data.getData();
        card.setData(bean);
    }

    private void onItemClickListener(Context context, ArticleCardBean bean) {
        AppNavigator.getInstance().startArticleActivity(
            context, bean.getUri(), "《春》");
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