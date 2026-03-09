package com.qiufengguang.ajstudy.card.serverip;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;

import com.qiufengguang.ajstudy.card.base.BaseViewHolder;
import com.qiufengguang.ajstudy.card.base.Card;
import com.qiufengguang.ajstudy.card.base.CardCreator;
import com.qiufengguang.ajstudy.databinding.CardServerIpBinding;
import com.qiufengguang.ajstudy.global.Constant;
import com.qiufengguang.ajstudy.utils.SpUtils;

import java.lang.ref.WeakReference;
import java.util.regex.Pattern;

/**
 * 服务器ip配置卡片
 *
 * @author qiufengguang
 * @since 2026/3/9 16:27
 */
public class ServerIpCard extends Card {
    /**
     * 卡片唯一id
     */
    public static final int LAYOUT_ID = 21;

    private static final String IPV4_REGEX =
        "^((25[0-5]|2[0-4]\\d|[01]?\\d\\d?)\\.){3}(25[0-5]|2[0-4]\\d|[01]?\\d\\d?)$";
    private static final Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);

    private WeakReference<CardServerIpBinding> bindingRef;


    private ServerIpCard() {
    }

    public void setData(String ip) {
        if (bindingRef == null) {
            return;
        }
        CardServerIpBinding binding = bindingRef.get();
        if (binding == null) {
            return;
        }
        binding.etServerIp.setText(ip);
        binding.btnSaveConfig.setOnClickListener(v -> {
            String serverIp = binding.etServerIp.getText().toString();
            if (ip == null || ip.trim().isEmpty()) {
                Toast.makeText(v.getContext().getApplicationContext(), "请输入有效的ip", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!IPV4_PATTERN.matcher(ip.trim()).matches()) {
                Toast.makeText(v.getContext().getApplicationContext(), "请输入有效的ip", Toast.LENGTH_SHORT).show();
                return;
            }
            SpUtils.getInstance().putString(Constant.Sp.KEY_SERVER_IP, serverIp);
            Toast.makeText(v.getContext().getApplicationContext(), "已保存，重启应用后生效。", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * 释放资源
     * 页面onDestroyView时调用
     */
    public void release() {
        if (bindingRef != null) {
            CardServerIpBinding binding = bindingRef.get();
            if (binding != null) {
                binding.btnSaveConfig.setOnClickListener(null);
                bindingRef.clear();
            }
            bindingRef = null;
        }
    }

    public static class Creator implements CardCreator {
        @Override
        public BaseViewHolder<?> create(@NonNull ViewGroup parent, LifecycleOwner lifecycleOwner) {
            CardServerIpBinding binding = CardServerIpBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
            return new ServerIpCardHolder(binding);
        }
    }

    public static class Builder {
        private CardServerIpBinding binding;

        /**
         * 设置卡片布局viewbinding
         *
         * @param binding CardServerIpBinding
         * @ Builder
         */
        public ServerIpCard.Builder setBinding(CardServerIpBinding binding) {
            this.binding = binding;
            return this;
        }

        public ServerIpCard create() {
            if (this.binding == null) {
                throw new UnsupportedOperationException(
                    "binding is null, call setBinding first.");
            }
            ServerIpCard wrapper = new ServerIpCard();
            wrapper.bindingRef = new WeakReference<>(binding);
            return wrapper;
        }
    }
}

