package com.qiufengguang.ajstudy.data.model;

import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.ColorInt;

import com.qiufengguang.ajstudy.data.base.BaseCardBean;

import java.util.Objects;

/**
 * 专题头卡数据
 *
 * @author qiufengguang
 * @since 2026/3/7 19:01
 */
public class TopicHeaderCardBean extends BaseCardBean {
    private static final String TAG = "TopicHeaderCardBean";

    private String subtitle;

    private String iconUrl;

    private String background;

    private String brief;

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    @ColorInt
    public int getBackground() {
        if (background == null || TextUtils.isEmpty(background)) {
            return 0xE04B5E;
        }
        try {
            return Color.parseColor(background);
        } catch (Exception e) {
            Log.w(TAG, "setColor: Unknown color.");
        }
        return 0xE04B5E;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }
        TopicHeaderCardBean that = (TopicHeaderCardBean) o;
        return Objects.equals(subtitle, that.subtitle)
            && Objects.equals(iconUrl, that.iconUrl)
            && Objects.equals(background, that.background)
            && Objects.equals(brief, that.brief);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtitle, iconUrl, background, brief);
    }
}
