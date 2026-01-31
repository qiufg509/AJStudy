package com.qiufengguang.ajstudy.card.user;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.activity.login.LoginActivity;
import com.qiufengguang.ajstudy.activity.main.MainActivity;
import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.LoginAction;
import com.qiufengguang.ajstudy.data.User;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardSimpleUserBinding;
import com.qiufengguang.ajstudy.global.GlobalApp;

/**
 * 简单用户卡片的ViewHolder
 *
 * @author qiufengguang
 * @since 2026/1/31 1:24
 */
public class SimpleUserCardHolder extends BaseViewHolder<CardSimpleUserBinding> {

    private SimpleUserCard card;

    public SimpleUserCardHolder(@NonNull CardSimpleUserBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new SimpleUserCard.Builder()
            .setBinding(binding)
            .setListener(SimpleUserCardHolder.this::onItemClick)
            .create();
        card.show();
    }

    @Override
    public void bind(LayoutData<?> data) {
        if (data == null || data.getData() == null || data.isCollection()
            || !TextUtils.equals(data.getLayoutName(), User.LAYOUT_NAME)) {
            return;
        }
        if (card == null) {
            initCard();
        }
        User user = (User) data.getData();
        card.setData(user);
    }

    private void onItemClick(Context context, User user) {
        if (user == null || user.isInvalid()) {
            Intent intent = new Intent(context, LoginActivity.class);
            intent.putExtra(LoginAction.ORIGINAL_PAGE, MainActivity.class.getName());
            intent.putExtra(LoginAction.DESTINATION_ID, R.id.navigation_me);
            context.startActivity(intent);
        } else {
            Toast.makeText(GlobalApp.getContext(), "用户详情页待完善", Toast.LENGTH_SHORT).show();
        }
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