package com.qiufengguang.ajstudy.utils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 * 时间格式化工具类，提供友好的时间显示
 * 线程安全，高性能设计
 *
 * @author qiufengguang
 * @since 2026/2/7 1:08
 */
public final class TimeFormatter {

    // 使用预定义的格式化器，避免重复创建（线程安全）
    private static final DateTimeFormatter MONTH_DAY_FORMATTER =
        DateTimeFormatter.ofPattern("MM月dd日");
    private static final DateTimeFormatter YEAR_MONTH_DAY_FORMATTER =
        DateTimeFormatter.ofPattern("yyyy年MM月dd日");

    // 时间阈值常量（毫秒）
    private static final long ONE_MINUTE = TimeUnit.MINUTES.toMillis(1);
    private static final long ONE_HOUR = TimeUnit.HOURS.toMillis(1);
    private static final long ONE_DAY = TimeUnit.DAYS.toMillis(1);
    private static final long ONE_WEEK = TimeUnit.DAYS.toMillis(7);
    private static final long ONE_MONTH = TimeUnit.DAYS.toMillis(30); // 近似值

    // 私有构造器防止实例化
    private TimeFormatter() {
        throw new AssertionError("No TimeFormatter instances for you!");
    }

    /**
     * 将时间戳格式化为友好的时间字符串
     *
     * @param timestamp 毫秒时间戳
     * @return 格式化的时间字符串
     */
    public static String formatTimeAgo(long timestamp) {
        if (timestamp <= 0) {
            return "刚刚";
        }

        long now = System.currentTimeMillis();
        long diff = now - timestamp;

        // 处理未来时间（不应该发生，但防御性编程）
        if (diff < 0) {
            return formatFutureTime(timestamp);
        }

        // 使用 if-else 链而不是 switch，因为阈值是范围比较
        if (diff < ONE_MINUTE) {
            return "刚刚";
        } else if (diff < ONE_HOUR) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            return minutes + "分钟前";
        } else if (diff < ONE_DAY) {
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            return hours + "小时前";
        } else if (diff < ONE_WEEK) {
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            return days + "天前";
        } else if (diff < ONE_MONTH) {
            long weeks = TimeUnit.MILLISECONDS.toDays(diff) / 7;
            return weeks + "周前";
        } else {
            // 使用 java.time API 处理月和年
            return formatDate(timestamp, now);
        }
    }

    /**
     * 格式化日期（超过一个月的情况）
     */
    private static String formatDate(long timestamp, long now) {
        // 转换为 Instant 以获取 LocalDateTime
        Instant timestampInstant = Instant.ofEpochMilli(timestamp);
        Instant nowInstant = Instant.ofEpochMilli(now);

        // 使用系统默认时区
        LocalDateTime timestampDateTime = LocalDateTime.ofInstant(
            timestampInstant, ZoneId.systemDefault());
        LocalDateTime nowDateTime = LocalDateTime.ofInstant(
            nowInstant, ZoneId.systemDefault());

        // 检查是否超过一年
        long yearsBetween = ChronoUnit.YEARS.between(
            timestampDateTime.toLocalDate(),
            nowDateTime.toLocalDate()
        );

        if (yearsBetween >= 1) {
            // 超过一年，显示完整年月日
            return YEAR_MONTH_DAY_FORMATTER.format(timestampDateTime);
        } else {
            // 一年内，显示月日
            return MONTH_DAY_FORMATTER.format(timestampDateTime);
        }
    }

    /**
     * 处理未来时间的情况
     */
    private static String formatFutureTime(long timestamp) {
        long now = System.currentTimeMillis();
        long diff = timestamp - now;

        if (diff < ONE_MINUTE) {
            return "即将";
        } else if (diff < ONE_HOUR) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            return minutes + "分钟后";
        } else if (diff < ONE_DAY) {
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            return hours + "小时后";
        } else {
            // 使用 java.time API 格式化未来日期
            Instant futureInstant = Instant.ofEpochMilli(timestamp);
            LocalDateTime futureDateTime = LocalDateTime.ofInstant(
                futureInstant, ZoneId.systemDefault());
            Instant nowInstant = Instant.ofEpochMilli(now);
            LocalDateTime nowDateTime = LocalDateTime.ofInstant(
                nowInstant, ZoneId.systemDefault());

            long yearsBetween = ChronoUnit.YEARS.between(
                nowDateTime.toLocalDate(),
                futureDateTime.toLocalDate()
            );

            if (yearsBetween >= 1) {
                return YEAR_MONTH_DAY_FORMATTER.format(futureDateTime);
            } else {
                return MONTH_DAY_FORMATTER.format(futureDateTime);
            }
        }
    }

    /**
     * 批量格式化时间戳（性能优化版本）
     * 适用于需要格式化多个时间戳的场景
     *
     * @param timestamps 毫秒时间戳数组
     * @return 格式化后的时间字符串数组
     */
    public static String[] formatTimeAgoBatch(long[] timestamps) {
        if (timestamps == null || timestamps.length == 0) {
            return new String[0];
        }

        String[] results = new String[timestamps.length];
        long now = System.currentTimeMillis();

        for (int i = 0; i < timestamps.length; i++) {
            results[i] = formatTimeAgo(timestamps[i], now);
        }

        return results;
    }

    /**
     * 内部使用的格式化方法，避免重复获取当前时间
     */
    private static String formatTimeAgo(long timestamp, long now) {
        if (timestamp <= 0) {
            return "刚刚";
        }

        long diff = now - timestamp;

        if (diff < 0) {
            return formatFutureTime(timestamp, now);
        }

        if (diff < ONE_MINUTE) {
            return "刚刚";
        } else if (diff < ONE_HOUR) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            return minutes + "分钟前";
        } else if (diff < ONE_DAY) {
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            return hours + "小时前";
        } else if (diff < ONE_WEEK) {
            long days = TimeUnit.MILLISECONDS.toDays(diff);
            return days + "天前";
        } else if (diff < ONE_MONTH) {
            long weeks = TimeUnit.MILLISECONDS.toDays(diff) / 7;
            return weeks + "周前";
        } else {
            return formatDate(timestamp, now);
        }
    }

    private static String formatFutureTime(long timestamp, long now) {
        long diff = timestamp - now;

        if (diff < ONE_MINUTE) {
            return "即将";
        } else if (diff < ONE_HOUR) {
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            return minutes + "分钟后";
        } else if (diff < ONE_DAY) {
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            return hours + "小时后";
        } else {
            Instant futureInstant = Instant.ofEpochMilli(timestamp);
            LocalDateTime futureDateTime = LocalDateTime.ofInstant(
                futureInstant, ZoneId.systemDefault());
            Instant nowInstant = Instant.ofEpochMilli(now);
            LocalDateTime nowDateTime = LocalDateTime.ofInstant(
                nowInstant, ZoneId.systemDefault());

            long yearsBetween = ChronoUnit.YEARS.between(
                nowDateTime.toLocalDate(),
                futureDateTime.toLocalDate()
            );

            if (yearsBetween >= 1) {
                return YEAR_MONTH_DAY_FORMATTER.format(futureDateTime);
            } else {
                return MONTH_DAY_FORMATTER.format(futureDateTime);
            }
        }
    }
}