package com.qiufengguang.ajstudy.dialog.manager;

import com.qiufengguang.ajstudy.dialog.Dialog;

/**
 * 对话框包装类
 * 管理多个dialog 按照dialog的优先级依次弹出
 *
 * @author qiufengguang
 * @since 2025/12/22 17:25
 */
public class DialogWrapper {

    private Dialog.Builder dialog;

    public DialogWrapper(Dialog.Builder dialog) {
        this.dialog = dialog;
    }

    public Dialog.Builder getDialog() {
        return dialog;
    }

    public void setDialog(Dialog.Builder dialog) {
        this.dialog = dialog;
    }

}
