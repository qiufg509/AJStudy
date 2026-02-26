package com.qiufengguang.ajstudy.data.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

/**
 * 幸运转盘卡片数据bean
 *
 * @author qiufengguang
 * @since 2026/2/1 21:56
 */
public class LuckyWheelCardBean extends BaseCardBean {
    private static final String TAG = "LuckyWheelCardBean";

    @NonNull
    private final String content;

    private int color;

    private String imageUrl;

    private @DrawableRes int iconId;

    private Bitmap bitmap;

    public LuckyWheelCardBean(@NonNull String content) {
        this.content = content;
    }

    public LuckyWheelCardBean(@NonNull String content, String color) {
        this.content = content;
        setColor(color);
    }

    public LuckyWheelCardBean(@NonNull String content, @DrawableRes int iconId, String color) {
        this.content = content;
        this.iconId = iconId;
        setColor(color);
    }

    public LuckyWheelCardBean(@NonNull String content, String imageUrl, String color) {
        this.content = content;
        this.imageUrl = imageUrl;
        setColor(color);
    }

    public @NonNull String getContent() {
        return content;
    }

    public void setColor(String color) {
        if (color == null || TextUtils.isEmpty(color)) {
            return;
        }
        try {
            this.color = Color.parseColor(color);
        } catch (Exception e) {
            Log.w(TAG, "setColor: Unknown color.");
        }
    }

    public int getColor() {
        return color;
    }

    @DrawableRes
    public int getIconId() {
        return iconId;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
