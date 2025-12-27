package com.qiufengguang.ajstudy.utils;

import java.util.Locale;

/**
 * 文件大小格式化工具类
 *
 * @author qiufengguang
 * @since 2025/12/11 23:06
 */
public class FileSizeFormatter {
    private static final long KB = 1024L;
    private static final long MB = KB * 1024L;
    private static final long GB = MB * 1024L;

    private FileSizeFormatter() {
        throw new UnsupportedOperationException("Cannot instantiate utility class");
    }

    public static String format(long sizeInBytes) {
        if (sizeInBytes <= 0) {
            return "0B";
        }

        if (sizeInBytes >= GB) {
            return String.format(Locale.getDefault(), "%.1fGB", (double) sizeInBytes / GB);
        } else if (sizeInBytes >= MB) {
            return String.format(Locale.getDefault(), "%.1fMB", (double) sizeInBytes / MB);
        } else if (sizeInBytes >= KB) {
            return String.format(Locale.getDefault(), "%.1fKB", (double) sizeInBytes / KB);
        } else {
            return sizeInBytes + "B";
        }
    }
}