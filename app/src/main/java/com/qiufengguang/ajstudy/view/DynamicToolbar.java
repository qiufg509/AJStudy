package com.qiufengguang.ajstudy.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.databinding.LayoutTitleBarBaseBinding;

/**
 * 标题控件
 *
 * @author qiufengguang
 * @since 2026/2/7 19:36
 */
public class DynamicToolbar extends ConstraintLayout {

    /**
     * 模式枚举
     */
    public enum Mode {
        /**
         * 仅返回按钮
         */
        BACK_ONLY,
        /**
         * 仅标题
         */
        TITLE_ONLY,
        /**
         * 返回+标题
         */
        BACK_TITLE,
        /**
         * 返回+标题+分享
         */
        BACK_TITLE_SHARE
    }

    private LayoutTitleBarBaseBinding binding;

    // 当前模式
    private Mode currentMode;

    // 点击监听器
    private OnBackClickListener onBackClickListener;
    private OnShareClickListener onShareClickListener;

    // 内容区域边距（4dp）
    private int contentPadding;

    // 计算出的实际边距
    private int totalMargin;

    public interface OnBackClickListener {
        void onBackClick();
    }

    public interface OnShareClickListener {
        void onShareClick();
    }

    public DynamicToolbar(@NonNull Context context) {
        super(context);
        init(context, null);
    }

    public DynamicToolbar(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DynamicToolbar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(@NonNull Context context, @Nullable AttributeSet attrs) {
        // 加载边距值
        contentPadding = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_xs);
        totalMargin = getResources().getDimensionPixelSize(R.dimen.activity_horizontal_margin_l);

        // 使用ViewBinding加载布局
        binding = LayoutTitleBarBaseBinding.inflate(LayoutInflater.from(context), this);

        // 设置初始约束（确保在ViewBinding之后立即设置）
        setupInitialConstraints();

        // 设置默认点击事件
        binding.barBack.setOnClickListener(v -> {
            if (onBackClickListener != null) {
                onBackClickListener.onBackClick();
            }
        });

        binding.barShare.setOnClickListener(v -> {
            if (onShareClickListener != null) {
                onShareClickListener.onShareClick();
            }
        });

        if (attrs != null) {
            try (TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DynamicToolbar)) {
                int modeValue = typedArray.getInt(R.styleable.DynamicToolbar_mode, 0);
                setMode(Mode.values()[modeValue]);
            }
        } else {
            setMode(Mode.TITLE_ONLY);
        }
    }

    /**
     * 设置初始约束，确保所有视图都有正确的约束
     */
    private void setupInitialConstraints() {
        // 设置返回按钮约束：距离父布局开始边有contentPadding，垂直居中
        ConstraintLayout.LayoutParams backParams = (ConstraintLayout.LayoutParams) binding.barBack.getLayoutParams();
        backParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        backParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        backParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        backParams.setMarginStart(contentPadding);
        binding.barBack.setLayoutParams(backParams);

        // 设置分享按钮约束：距离父布局结束边有contentPadding，垂直居中
        ConstraintLayout.LayoutParams shareParams = (ConstraintLayout.LayoutParams) binding.barShare.getLayoutParams();
        shareParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        shareParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        shareParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        shareParams.setMarginEnd(contentPadding);
        binding.barShare.setLayoutParams(shareParams);

        // 设置标题初始约束（会被模式设置覆盖），垂直居中
        ConstraintLayout.LayoutParams titleParams = (ConstraintLayout.LayoutParams) binding.barTitle.getLayoutParams();
        titleParams.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        titleParams.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        titleParams.topToTop = ConstraintLayout.LayoutParams.PARENT_ID;
        titleParams.bottomToBottom = ConstraintLayout.LayoutParams.PARENT_ID;
        binding.barTitle.setLayoutParams(titleParams);
    }

    /**
     * 设置Toolbar模式
     */
    public void setMode(@NonNull Mode mode) {
        if (this.currentMode == mode) {
            return; // 模式相同，无需更新
        }

        this.currentMode = mode;
        applyMode();
    }

    /**
     * 获取当前模式
     */
    @NonNull
    public Mode getMode() {
        return currentMode;
    }

    /**
     * 应用当前模式
     */
    private void applyMode() {
        switch (currentMode) {
            case BACK_ONLY:
                applyBackOnlyMode();
                break;
            case TITLE_ONLY:
                applyTitleOnlyMode();
                break;
            case BACK_TITLE:
                applyBackTitleMode();
                break;
            case BACK_TITLE_SHARE:
                applyBackTitleShareMode();
                break;
        }
    }

    /**
     * 仅返回按钮模式
     */
    private void applyBackOnlyMode() {
        // 设置按钮可见性
        binding.barBack.setVisibility(VISIBLE);
        binding.barTitle.setVisibility(GONE);
        binding.barShare.setVisibility(GONE);

        // 在仅返回按钮模式下，保持返回按钮在左侧
        // 不需要额外设置，因为setupInitialConstraints已经设置好了
    }

    /**
     * 仅标题模式
     */
    private void applyTitleOnlyMode() {
        // 设置按钮可见性
        binding.barBack.setVisibility(GONE);
        binding.barTitle.setVisibility(VISIBLE);
        binding.barShare.setVisibility(GONE);

        // 更新标题约束：距离父布局两边都有totalMargin
        updateTitleConstraints(
            ConstraintLayout.LayoutParams.PARENT_ID,
            ConstraintLayout.LayoutParams.PARENT_ID,
            totalMargin,
            totalMargin
        );
    }

    /**
     * 返回+标题模式
     */
    private void applyBackTitleMode() {
        // 设置按钮可见性
        binding.barBack.setVisibility(VISIBLE);
        binding.barTitle.setVisibility(VISIBLE);
        binding.barShare.setVisibility(GONE);

        // 更新标题约束：左侧连接到返回按钮，右侧有totalMargin
        updateTitleConstraints(
            R.id.bar_back,
            ConstraintLayout.LayoutParams.PARENT_ID,
            0,
            totalMargin
        );
    }

    /**
     * 返回+标题+分享模式 - 修复短标题靠右问题
     */
    private void applyBackTitleShareMode() {
        // 设置按钮可见性
        binding.barBack.setVisibility(VISIBLE);
        binding.barTitle.setVisibility(VISIBLE);
        binding.barShare.setVisibility(VISIBLE);

        // 更新标题约束：左侧连接到返回按钮，右侧连接到分享按钮
        // 添加水平偏斜（bias）为0，确保标题默认靠左对齐
        updateTitleConstraintsWithBias(
            R.id.bar_back,
            R.id.bar_share
            // 水平偏斜为0，表示靠左
        );
    }

    /**
     * 更新标题的约束条件（简洁版）
     */
    private void updateTitleConstraints(
        int startViewId,
        int endViewId,
        int startMargin,
        int endMargin
    ) {
        // 如果标题不可见，不需要更新约束
        if (binding.barTitle.getVisibility() != VISIBLE) {
            return;
        }

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.barTitle.getLayoutParams();

        // 清除现有水平约束
        params.startToStart = ConstraintLayout.LayoutParams.UNSET;
        params.startToEnd = ConstraintLayout.LayoutParams.UNSET;
        params.endToStart = ConstraintLayout.LayoutParams.UNSET;
        params.endToEnd = ConstraintLayout.LayoutParams.UNSET;

        // 设置起始边约束
        if (startViewId == ConstraintLayout.LayoutParams.PARENT_ID) {
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        } else {
            params.startToEnd = startViewId;
        }

        // 设置结束边约束
        if (endViewId == ConstraintLayout.LayoutParams.PARENT_ID) {
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        } else {
            params.endToStart = endViewId;
        }

        // 设置边距
        params.setMarginStart(startMargin);
        params.setMarginEnd(endMargin);

        // 清除水平偏斜（对于非BACK_TITLE_SHARE模式）
        params.horizontalBias = 0.5f; // 默认居中

        binding.barTitle.setLayoutParams(params);
    }

    /**
     * 更新标题的约束条件（带水平偏斜版本）
     */
    private void updateTitleConstraintsWithBias(int startViewId, int endViewId) {
        // 如果标题不可见，不需要更新约束
        if (binding.barTitle.getVisibility() != VISIBLE) {
            return;
        }

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) binding.barTitle.getLayoutParams();

        // 清除现有水平约束
        params.startToStart = ConstraintLayout.LayoutParams.UNSET;
        params.startToEnd = ConstraintLayout.LayoutParams.UNSET;
        params.endToStart = ConstraintLayout.LayoutParams.UNSET;
        params.endToEnd = ConstraintLayout.LayoutParams.UNSET;

        // 设置起始边约束
        if (startViewId == ConstraintLayout.LayoutParams.PARENT_ID) {
            params.startToStart = ConstraintLayout.LayoutParams.PARENT_ID;
        } else {
            params.startToEnd = startViewId;
        }

        // 设置结束边约束
        if (endViewId == ConstraintLayout.LayoutParams.PARENT_ID) {
            params.endToEnd = ConstraintLayout.LayoutParams.PARENT_ID;
        } else {
            params.endToStart = endViewId;
        }

        // 设置边距
        params.setMarginStart(0);
        params.setMarginEnd(0);

        // 设置水平偏斜（0.0=靠左，0.5=居中，1.0=靠右）
        params.horizontalBias = (float) 0.0;

        binding.barTitle.setLayoutParams(params);
    }

    /**
     * 设置标题文本
     */
    public void setTitle(String title) {
        if (currentMode == Mode.BACK_ONLY) {
            return;
        }
        binding.barTitle.setText(title);
    }

    /**
     * 设置标题文本
     */
    public void setTitle(int resId) {
        binding.barTitle.setText(resId);
    }

    /**
     * 获取标题文本
     */
    public String getTitle() {
        return binding.barTitle.getText().toString();
    }

    /**
     * 设置返回按钮监听器
     */
    public void setOnBackClickListener(OnBackClickListener listener) {
        this.onBackClickListener = listener;
    }

    /**
     * 设置分享按钮监听器
     */
    public void setOnShareClickListener(OnShareClickListener listener) {
        this.onShareClickListener = listener;
    }

    /**
     * 设置返回按钮可见性（手动控制）
     */
    public void setBackButtonVisible(boolean visible) {
        binding.barBack.setVisibility(visible ? VISIBLE : GONE);
        binding.barTitle.setVisibility(VISIBLE); // 确保标题可见
        updateConstraintsForCurrentMode();
    }

    /**
     * 设置分享按钮可见性（手动控制）
     */
    public void setShareButtonVisible(boolean visible) {
        binding.barShare.setVisibility(visible ? VISIBLE : GONE);
        binding.barTitle.setVisibility(VISIBLE); // 确保标题可见
        updateConstraintsForCurrentMode();
    }

    /**
     * 设置标题可见性（手动控制）
     */
    public void setTitleVisible(boolean visible) {
        binding.barTitle.setVisibility(visible ? VISIBLE : GONE);
        updateConstraintsForCurrentMode();
    }

    /**
     * 根据当前按钮可见性更新约束
     */
    private void updateConstraintsForCurrentMode() {
        boolean backVisible = binding.barBack.getVisibility() == VISIBLE;
        boolean titleVisible = binding.barTitle.getVisibility() == VISIBLE;
        boolean shareVisible = binding.barShare.getVisibility() == VISIBLE;

        // 如果标题不可见，不需要更新标题约束
        if (!titleVisible) {
            return;
        }

        if (!backVisible && !shareVisible) {
            // 没有按钮，仅标题
            updateTitleConstraints(
                ConstraintLayout.LayoutParams.PARENT_ID,
                ConstraintLayout.LayoutParams.PARENT_ID,
                totalMargin,
                totalMargin
            );
        } else if (backVisible && !shareVisible) {
            // 只有返回按钮
            updateTitleConstraints(
                R.id.bar_back,
                ConstraintLayout.LayoutParams.PARENT_ID,
                0,
                totalMargin
            );
        } else if (!backVisible) {
            // 只有分享按钮
            updateTitleConstraints(
                ConstraintLayout.LayoutParams.PARENT_ID,
                R.id.bar_share,
                totalMargin,
                0
            );
        } else {
            // 两个按钮都有 - 使用带偏斜的版本，标题靠左
            updateTitleConstraintsWithBias(
                R.id.bar_back,
                R.id.bar_share
                // 靠左对齐
            );
        }
    }

    /**
     * 获取返回按钮
     */
    public ImageButton getBackButton() {
        return binding.barBack;
    }

    /**
     * 获取分享按钮
     */
    public ImageButton getShareButton() {
        return binding.barShare;
    }

    /**
     * 获取标题视图
     */
    public TextView getTitleView() {
        return binding.barTitle;
    }
}