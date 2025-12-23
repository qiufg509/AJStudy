package com.qiufengguang.ajstudy.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.qiufengguang.ajstudy.dialog.manager.DialogsManager;


/**
 * 对话框基类
 *
 * @author qiufengguang
 * @since 2025/12/22 17:30
 */
public abstract class BaseDialog extends DialogFragment {
    private View view = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        if (getLayoutRes() != 0) {
            //调用方通过xml获取view
            view = inflater.inflate(getLayoutRes(), container, false);
        } else if (getDialogView() != null) {
            //调用方直接传入view
            view = getDialogView();
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        Dialog dialog = getDialog();
        if (dialog == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R && dialog.getWindow() != null) {
            dialog.getWindow().getDecorView().setPadding(0, 0, 0, 0);
        }
        //如果isCancelable()是false 则会屏蔽物理返回键
        dialog.setCancelable(isCancelable());
        //如果isCancelableOutside()为false 点击屏幕外Dialog不会消失；反之会消失
        dialog.setCanceledOnTouchOutside(isCancelableOutside());
        //如果isCancelable()设置的是false 会屏蔽物理返回键
        dialog.setOnKeyListener((dialog1, keyCode, event) ->
            keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN
                && !isCancelable());
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog == null) {
            return;
        }
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        //设置背景色透明
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //设置Dialog动画效果
        if (getAnimRes() != 0) {
            window.setWindowAnimations(getAnimRes());
        }
        WindowManager.LayoutParams params = window.getAttributes();
        //设置Dialog的Width
        if (getDialogWidth() > 0) {
            params.width = getDialogWidth();
        } else {
            params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        //设置Dialog的Height
        if (getDialogHeight() > 0) {
            params.height = getDialogHeight();
        } else {
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        //设置屏幕透明度 0.0f~1.0f(完全透明~完全不透明)
        params.dimAmount = getDimAmount();
        params.gravity = getGravity();
        window.setAttributes(params);
    }

    protected View getBaseView() {
        return view;
    }

    protected abstract int getLayoutRes();

    protected abstract View getDialogView();

    protected boolean isCancelableOutside() {
        return true;
    }

    protected int getDialogWidth() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    protected int getDialogHeight() {
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    public float getDimAmount() {
        return 0.2f;
    }

    protected int getGravity() {
        return Gravity.CENTER;
    }

    protected int getAnimRes() {
        return 0;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        DialogsManager.getInstance().over();
    }
}
