package com.qiufengguang.ajstudy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Log;

/**
 * 图片工具
 *
 * @author qiufengguang
 * @since 2026/2/3 14:49
 */
public class ImageUtil {
    private static final String TAG = "ImageUtil";

    /**
     * 加载图片并缩放到指定大小，保持原图比例
     *
     * @param context    上下文
     * @param inDensity  屏幕密度
     * @param iconId     图片资源id
     * @param targetSize 目标大小（最长边的尺寸）
     * @return 缩放后的Bitmap
     */
    public static Bitmap loadBitmap(Context context, int inDensity, int iconId, int targetSize) {
        if (targetSize <= 0) {
            return null;
        }
        try {
            // 获取图片原始尺寸
            int inSampleSize = calcInSampleSize(context, iconId, targetSize);

            // 根据采样率解码图片
            BitmapFactory.Options decodeOptions = new BitmapFactory.Options();
            decodeOptions.inSampleSize = inSampleSize;
            decodeOptions.inScaled = true;
            decodeOptions.inDensity = inDensity;
            decodeOptions.inTargetDensity = inDensity;
            decodeOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            decodeOptions.inMutable = false;

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), iconId, decodeOptions);

            if (bitmap == null) {
                Log.w(TAG, "Failed to decode bitmap for resource id: " + iconId);
                return null;
            }

            // 按原图比例缩放到targetSize（最长边）
            return scaleBitmapToTargetSize(bitmap, targetSize, true);

        } catch (OutOfMemoryError e) {
            Log.e(TAG, "OutOfMemoryError while loading bitmap for resource id: " + iconId, e);
            // 尝试清理内存
            System.gc();
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Exception while loading bitmap for resource id: " + iconId, e);
            return null;
        }
    }

    /**
     * 将Bitmap按比例缩放到指定大小
     *
     * @param source     源Bitmap
     * @param targetSize 目标大小（最长边的尺寸）
     * @param recycleSrc 是否回收源Bitmap
     * @return 缩放后的Bitmap
     */
    public static Bitmap scaleBitmapToTargetSize(Bitmap source, int targetSize, boolean recycleSrc) {
        if (source == null || source.isRecycled() || targetSize <= 0) {
            return null;
        }

        int srcWidth = source.getWidth();
        int srcHeight = source.getHeight();

        if (srcWidth <= 0 || srcHeight <= 0) {
            return source;
        }

        // 计算缩放比例，保持宽高比
        float scale;
        if (srcWidth >= srcHeight) {
            // 宽度是较长边
            scale = (float) targetSize / srcWidth;
        } else {
            // 高度是较长边
            scale = (float) targetSize / srcHeight;
        }
        // 如果缩放比例接近1，不需要缩放
        if (Math.abs(scale - 1.0f) < 0.01f) {
            return source;
        }
        try {
            // 使用Matrix进行缩放
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale);

            // 创建缩放后的Bitmap
            Bitmap scaledBitmap = Bitmap.createBitmap(source, 0, 0, srcWidth, srcHeight, matrix, true);
            if (recycleSrc && scaledBitmap != source) {
                source.recycle();
            }
            return scaledBitmap;
        } catch (OutOfMemoryError e) {
            Log.e(TAG, "OutOfMemoryError while scaling bitmap", e);
            System.gc();
            return source; // 返回原始Bitmap
        } catch (Exception e) {
            Log.e(TAG, "Exception while scaling bitmap", e);
            return source; // 返回原始Bitmap
        }
    }


    /**
     * 计算采样率
     *
     * @param context    上下文
     * @param iconId     图片资源
     * @param targetSize 目标大小
     * @return 采样率
     */
    private static int calcInSampleSize(Context context, int iconId, int targetSize) {
        // 仅解码图片尺寸，不分配内存
        BitmapFactory.Options sizeOptions = new BitmapFactory.Options();
        sizeOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), iconId, sizeOptions);
        int originalWidth = sizeOptions.outWidth;
        int originalHeight = sizeOptions.outHeight;

        int inSampleSize = 1;
        if (originalWidth > 0 && originalHeight > 0 && targetSize > 0) {
            // 计算目标尺寸（增加20%余量，确保缩放质量）
            int targetSizeWithMargin = (int) (targetSize * 1.2f);

            // 计算采样率
            int widthRatio = originalWidth / targetSizeWithMargin;
            int heightRatio = originalHeight / targetSizeWithMargin;
            inSampleSize = Math.max(widthRatio, heightRatio);

            // 确保inSampleSize至少为1且是2的幂
            if (inSampleSize > 1) {
                inSampleSize = findBestSampleSize(inSampleSize);
            }
            inSampleSize = Math.max(1, inSampleSize);
        }
        return inSampleSize;
    }

    /**
     * 找到最接近的2的幂作为采样率
     */
    private static int findBestSampleSize(int targetSize) {
        int power = 1;
        while (power * 2 <= targetSize) {
            power *= 2;
        }
        return power;
    }
}
