package com.qiufengguang.ajstudy.card.serverip;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.data.base.LayoutData;
import com.qiufengguang.ajstudy.databinding.CardServerIpBinding;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.utils.SpUtils;

/**
 * 服务器ip配置卡片
 *
 * @author qiufengguang
 * @since 2026/3/9 16:26
 */
public class ServerIpCardHolder extends BaseViewHolder<CardServerIpBinding> {

    private ServerIpCard card;

    public ServerIpCardHolder(@NonNull CardServerIpBinding binding) {
        super(binding);
    }

    @Override
    public void initCard() {
        if (card != null) {
            return;
        }
        card = new ServerIpCard.Builder()
            .setBinding(binding)
            .create();
    }

    @Override
    public boolean bind(LayoutData<?> data, LifecycleOwner lifecycleOwner) {
        if (data == null || data.isCollection() || data.getLayoutId() != ServerIpCard.LAYOUT_ID) {
            return false;
        }
        if (card == null) {
            initCard();
        }
        String serverIp = SpUtils.getInstance().getString(Constant.Sp.KEY_SERVER_IP, "");
        card.setData(serverIp);
        return true;
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