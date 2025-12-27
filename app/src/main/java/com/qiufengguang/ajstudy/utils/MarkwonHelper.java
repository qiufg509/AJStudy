package com.qiufengguang.ajstudy.utils;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.qiufengguang.ajstudy.global.GlobalApp;

import io.noties.markwon.Markwon;
import io.noties.markwon.ext.tables.TablePlugin;
import io.noties.markwon.html.HtmlPlugin;
import io.noties.markwon.image.glide.GlideImagesPlugin;
import io.noties.markwon.linkify.LinkifyPlugin;

/**
 * Markdown文档帮助类，使用Markwon库
 *
 * @author qiufengguang
 * @since 2025/12/25 15:34
 */
public class MarkwonHelper {

    private static volatile Markwon sInstance;

    private MarkwonHelper() {
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }

    /**
     * 获取Markwon单例（如果已初始化）
     *
     * @return Markwon实例，如果未初始化则返回null
     */
    @Nullable
    public static Markwon getInstance() {
        return sInstance;
    }

    /**
     * 获取Markwon实例，如果未初始化则立即同步初始化
     * 警告：此方法可能阻塞主线程，建议在后台线程调用
     *
     * @param context 上下文
     * @return Markwon实例
     */
    @NonNull
    public static Markwon getInstanceSync(@NonNull Context context) {
        if (sInstance == null) {
            synchronized (MarkwonHelper.class) {
                if (sInstance == null) {
                    sInstance = createMarkwon(context.getApplicationContext());
                }
            }
        }
        return sInstance;
    }

    /**
     * 初始化Markwon
     */
    public static void initialize() {
        Context context = GlobalApp.getContext();
        if (context == null) {
            return;
        }
        createMarkwon(context);
    }

    /**
     * 创建Markwon实例
     */
    @NonNull
    private static Markwon createMarkwon(@NonNull Context context) {
        return Markwon.builder(context)
            .usePlugin(GlideImagesPlugin.create(context))
            .usePlugin(HtmlPlugin.create())
            .usePlugin(TablePlugin.create(context))
            .usePlugin(LinkifyPlugin.create())
            .build();
    }
}