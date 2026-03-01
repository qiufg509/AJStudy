package com.qiufengguang.ajstudy.data.model;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

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

    private String content;

    private String color;

    private String imageUrl;

    private Bitmap bitmap;

    @NonNull
    public String getContent() {
        return content;
    }

    public void setContent(@NonNull String content) {
        this.content = content;
    }

    public int getColor() {
        if (color == null || TextUtils.isEmpty(color)) {
            return Color.BLUE;
        }
        try {
            return Color.parseColor(color);
        } catch (Exception e) {
            Log.w(TAG, "setColor: Unknown color.");
        }
        return Color.BLUE;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
