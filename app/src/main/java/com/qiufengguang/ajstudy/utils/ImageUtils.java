package com.qiufengguang.ajstudy.utils;

import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.qiufengguang.ajstudy.R;
import com.qiufengguang.ajstudy.global.GlobalApp;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * 全局图像处理工具类
 * [性能专家重构]：复用 RequestOptions 实例，减少 Glide 配置开销，降低内存抖动
 */
public class ImageUtils {

    private static RequestOptions optionsM;
    private static RequestOptions optionsL;
    private static RequestOptions optionsTopM;
    private static RequestOptions optionsTopL;

    /**
     * 获取通用的全圆角配置（radius_m）
     */
    public static RequestOptions getOptionsM() {
        if (optionsM == null) {
            int radius = GlobalApp.getContext().getResources().getDimensionPixelSize(R.dimen.radius_m);
            optionsM = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder_image_20_8)
                .error(R.drawable.placeholder_image_20_8)
                .transform(new CenterCrop(), new RoundedCorners(radius));
        }
        return optionsM;
    }

    /**
     * 获取通用的全圆角配置（radius_l）
     */
    public static RequestOptions getOptionsL() {
        if (optionsL == null) {
            int radius = GlobalApp.getContext().getResources().getDimensionPixelSize(R.dimen.radius_l);
            optionsL = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder_image_1_1_l)
                .error(R.drawable.placeholder_image_1_1_l)
                .transform(new CenterCrop(), new RoundedCorners(radius));
        }
        return optionsL;
    }

    /**
     * 获取顶部圆角配置（radius_m）
     */
    public static RequestOptions getOptionsTopM() {
        if (optionsTopM == null) {
            int radius = GlobalApp.getContext().getResources().getDimensionPixelSize(R.dimen.radius_m);
            optionsTopM = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder_image_20_8)
                .error(R.drawable.placeholder_image_20_8)
                .transform(new CenterCrop(), new RoundedCornersTransformation(radius, 0,
                    RoundedCornersTransformation.CornerType.TOP));
        }
        return optionsTopM;
    }

    /**
     * 获取顶部圆角配置（radius_l）
     */
    public static RequestOptions getOptionsTopL() {
        if (optionsTopL == null) {
            int radius = GlobalApp.getContext().getResources().getDimensionPixelSize(R.dimen.radius_l);
            optionsTopL = new RequestOptions()
                .centerCrop()
                .placeholder(R.drawable.placeholder_image_1_1_l)
                .error(R.drawable.placeholder_image_1_1_l)
                .transform(new CenterCrop(), new RoundedCornersTransformation(radius, 0,
                    RoundedCornersTransformation.CornerType.TOP));
        }
        return optionsTopL;
    }

    private ImageUtils() {
        throw new UnsupportedOperationException("Utility class");
    }
}
