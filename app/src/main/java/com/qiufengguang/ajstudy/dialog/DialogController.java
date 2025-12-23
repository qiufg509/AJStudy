package com.qiufengguang.ajstudy.dialog;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.utils.DisplayMetricsHelper;

import java.lang.ref.WeakReference;

/**
 * 对话框属性控制类
 *
 * @author qiufengguang
 * @since 2025/12/22 17:36
 */
public class DialogController {

    private int layoutRes;
    private int dialogWidth;
    private int dialogHeight;
    private float dimAmount = 0.2f;
    private int gravity = Gravity.CENTER;
    private boolean isCancelableOutside = false;
    private boolean cancelable = true;
    private int animRes;
    private View dialogView;
    private IDialog.OnClickListener mPositiveButtonListener;
    private IDialog.OnClickListener mNegativeButtonListener;
    private final WeakReference<IDialog> mDialog;
    /**
     * 默认标题
     */
    private String titleStr;
    /**
     * 默认内容
     */
    private String contentStr;
    /**
     * 右边按钮文字
     */
    private String positiveStr;
    /**
     * 左边按钮文字
     */
    private String negativeStr;
    /**
     * 显示左边按钮
     */
    private boolean showBtnLeft;
    /**
     * 显示右边按钮
     */
    private boolean showBtnRight;

    /**
     * 确定按钮、右边按钮（id定位为dialog_btn_ok）
     */
    private Button btnOk;
    /**
     * 取消按钮、左边按钮（id定位为dialog_btn_cancel）
     */
    private Button btnCancel;

    DialogController(IDialog dialog) {
        mDialog = new WeakReference<>(dialog);
    }

    int getAnimRes() {
        return animRes;
    }

    int getLayoutRes() {
        return layoutRes;
    }

    void setLayoutRes(int layoutRes) {
        this.layoutRes = layoutRes;
    }

    int getDialogWidth() {
        return dialogWidth;
    }

    int getDialogHeight() {
        return dialogHeight;
    }

    float getDimAmount() {
        return dimAmount;
    }

    public int getGravity() {
        return gravity;
    }

    boolean isCancelableOutside() {
        return isCancelableOutside;
    }

    boolean isCancelable() {
        return cancelable;
    }

    public void setDialogView(View dialogView) {
        this.dialogView = dialogView;
    }

    View getDialogView() {
        return dialogView;
    }

    public void setChildView(View view) {
        setDialogView(view);
        dealDefaultDialog(mPositiveButtonListener, mNegativeButtonListener,
            contentStr, showBtnLeft, showBtnRight);
    }

    private void dealDefaultDialog(IDialog.OnClickListener positiveBtnListener,
                                   IDialog.OnClickListener negativeBtnListener, String contentStr,
                                   boolean showBtnLeft, boolean showBtnRight) {
        if (dialogView == null) {
            return;
        }
        this.mNegativeButtonListener = negativeBtnListener;
        this.mPositiveButtonListener = positiveBtnListener;
        btnOk = dialogView.findViewById(R.id.dialog_btn_ok);
        btnCancel = dialogView.findViewById(R.id.dialog_btn_cancel);

        if (showBtnRight && showBtnLeft) {
            //左右两个按钮都存在
            if (btnOk != null) {
                btnOk.setVisibility(View.VISIBLE);
                btnOk.setText(Html.fromHtml(TextUtils.isEmpty(positiveStr)
                    ? "确定" : positiveStr, Html.FROM_HTML_MODE_LEGACY));
                btnOk.setOnClickListener(mButtonHandler);
            }
            if (btnCancel != null) {
                btnCancel.setVisibility(View.VISIBLE);
                btnCancel.setText(Html.fromHtml(TextUtils.isEmpty(negativeStr)
                    ? "取消" : negativeStr, Html.FROM_HTML_MODE_LEGACY));
                btnCancel.setOnClickListener(mButtonHandler);
            }
            View divider = dialogView.findViewById(R.id.divider_vertical);
            if (divider != null) {
                divider.setVisibility(View.VISIBLE);
            }
        } else if (showBtnRight) {
            //只有右边的按钮
            if (btnOk != null) {
                btnOk.setVisibility(View.VISIBLE);
                btnOk.setBackgroundResource(R.drawable.dialog_default_single_btn_selector);
                btnOk.setText(Html.fromHtml(TextUtils.isEmpty(positiveStr)
                    ? "确定" : positiveStr, Html.FROM_HTML_MODE_LEGACY));
                btnOk.setOnClickListener(mButtonHandler);
            }
        } else if (showBtnLeft) {
            //只有左边的按钮
            if (btnCancel != null) {
                btnCancel.setVisibility(View.VISIBLE);
                btnCancel.setBackgroundResource(R.drawable.dialog_default_single_btn_selector);
                btnCancel.setText(Html.fromHtml(TextUtils.isEmpty(negativeStr)
                    ? "取消" : negativeStr, Html.FROM_HTML_MODE_LEGACY));
                btnCancel.setOnClickListener(mButtonHandler);
            }
        }

        TextView tvTitle = dialogView.findViewById(R.id.dialog_title);
        if (tvTitle != null) {
            tvTitle.setVisibility(TextUtils.isEmpty(titleStr) ? View.GONE : View.VISIBLE);
            tvTitle.setText(Html.fromHtml(!TextUtils.isEmpty(titleStr)
                ? titleStr : "对话框", Html.FROM_HTML_MODE_LEGACY));
            if (TextUtils.isEmpty(contentStr) && mDialog.get() != null
                && mDialog.get().getContext() != null) {
                tvTitle.setMinHeight(DisplayMetricsHelper.dp2px(mDialog.get().getContext(), 100));
                tvTitle.setGravity(Gravity.CENTER);
                tvTitle.setPadding(0, 10, 0, 0);
            }
        }
        final TextView tvContent = dialogView.findViewById(R.id.dialog_content);
        if (tvContent != null) {
            tvContent.setVisibility(TextUtils.isEmpty(contentStr) ? View.GONE : View.VISIBLE);
            tvContent.setText(contentStr);
            tvContent.getViewTreeObserver().addOnPreDrawListener(() -> {
                int lineCount = tvContent.getLineCount();
                if (lineCount >= 3) {
                    //超过三行居左显示
                    tvContent.setGravity(Gravity.START);
                } else {
                    //默认居中
                    tvContent.setGravity(Gravity.CENTER_HORIZONTAL);
                    if (TextUtils.isEmpty(titleStr)) {
                        tvContent.setPadding(0, 50, 0, 50);
                    }
                }

                if (TextUtils.isEmpty(titleStr)) {
                    //没有title，只有content
                    tvContent.setTextSize(18);
                    if (mDialog.get() == null || mDialog.get().getContext() == null
                        || mDialog.get().getContext().getResources() == null) {
                        return true;
                    }
                    tvContent.setTextColor(ContextCompat.getColor(
                        mDialog.get().getContext(), R.color.ajstudy_color_text_secondary));
                }
                return true;
            });

        }

    }

    private final View.OnClickListener mButtonHandler = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (view == btnCancel) {
                if (mDialog.get() == null) {
                    return;
                }
                if (mNegativeButtonListener != null) {
                    mNegativeButtonListener.onClick(mDialog.get());
                }
            } else if (view == btnOk) {
                if (mDialog.get() == null) {
                    return;
                }
                if (mPositiveButtonListener != null) {
                    mPositiveButtonListener.onClick(mDialog.get());
                }
            }
        }
    };

    public static class Params {
        FragmentManager fragmentManager;
        int layoutRes;
        int dialogWidth;
        int dialogHeight = WindowManager.LayoutParams.WRAP_CONTENT;
        float dimAmount = 0.4f;

        public int gravity = Gravity.CENTER;
        boolean isCancelableOutside = false;
        boolean cancelable = true;
        View dialogView;
        Context context;
        IDialog.OnClickListener positiveBtnListener;
        IDialog.OnClickListener negativeBtnListener;
        String titleStr;
        String contentStr;
        String positiveStr;
        String negativeStr;
        boolean showBtnLeft, showBtnRight;
        int animRes = R.style.DialogAnimFade;

        void apply(DialogController controller) {
            controller.dimAmount = dimAmount;
            controller.gravity = gravity;
            controller.isCancelableOutside = isCancelableOutside;
            controller.cancelable = cancelable;
            controller.animRes = animRes;
            controller.titleStr = titleStr;
            controller.contentStr = contentStr;
            controller.positiveStr = positiveStr;
            controller.negativeStr = negativeStr;
            controller.showBtnLeft = showBtnLeft;
            controller.showBtnRight = showBtnRight;
            controller.mPositiveButtonListener = positiveBtnListener;
            controller.mNegativeButtonListener = negativeBtnListener;
            if (layoutRes != 0) {
                controller.setLayoutRes(layoutRes);
            } else if (dialogView != null) {
                controller.dialogView = dialogView;
            } else {
                throw new IllegalArgumentException("Dialog View can't be null");
            }
            if (dialogWidth > 0) {
                controller.dialogWidth = dialogWidth;
            }
            if (dialogHeight > 0) {
                controller.dialogHeight = dialogHeight;
            }
        }
    }
}
