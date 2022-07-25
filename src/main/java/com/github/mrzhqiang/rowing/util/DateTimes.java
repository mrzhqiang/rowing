package com.github.mrzhqiang.rowing.util;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Maps;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.function.Function;

/**
 * 日期时间工具。
 */
public final class DateTimes {
    private DateTimes() {
        // no instances
    }

    /**
     * 基础的日期时间格式化模式。
     */
    public static final String BASIC_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 基础的日期时间格式化器。
     */
    public static final DateTimeFormatter BASIC_FORMATTER
            = DateTimeFormatter.ofPattern(BASIC_PATTERN);
    /**
     * 简化的日期格式化模式。
     */
    public static final String SIMPLE_DATE_PATTERN = "MM-dd";
    /**
     * 简化的日期格式化器。
     */
    public static final DateTimeFormatter SIMPLE_DATE_FORMATTER
            = DateTimeFormatter.ofPattern(SIMPLE_DATE_PATTERN);
    /**
     * 简化的时间格式化模式。
     */
    public static final String SIMPLE_TIME_PATTERN = "HH:mm";
    /**
     * 简化的时间格式化器。
     */
    public static final DateTimeFormatter SIMPLE_TIME_FORMATTER
            = DateTimeFormatter.ofPattern(SIMPLE_TIME_PATTERN);
    /**
     * 还有多少秒的 KEY 值。
     */
    public static final String KEY_HAVE_SECONDS = "have_seconds";
    /**
     * 还有多少分钟的 KEY 值。
     */
    public static final String KEY_HAVE_MINUTES = "have_minutes";
    /**
     * 还有多少小时的 KEY 值。
     */
    public static final String KEY_HAVE_HOURS = "have_hours";
    /**
     * 还有多少天的 KEY 值。
     */
    public static final String KEY_HAVE_DAYS = "have_days";
    /**
     * 未知的 KEY 值。
     */
    public static final String KEY_UNKNOWN = "unknown";
    /**
     * 刚刚的 KEY 值。
     */
    public static final String KEY_JUST_NOW = "just_now";
    /**
     * 一分钟前的 KEY 值。
     */
    public static final String KEY_MINUTES_AGO = "minutes_ago";
    /**
     * 一小时前的 KEY 值。
     */
    public static final String KEY_HOURS_AGO = "hours_ago";
    /**
     * 今天的 KEY 值。
     */
    public static final String KEY_TODAY = "today";
    /**
     * 昨天的 KEY 值。
     */
    public static final String KEY_YESTERDAY = "yesterday";

    /* for test */static final String DEF_HAVE_TIME_SECONDS = "还有 %s 秒";
    /* for test */static final String DEF_HAVE_TIME_MINUTES = "还有 %s 分钟";
    /* for test */static final String DEF_HAVE_TIME_HOURS = "还有 %s 小时";
    /* for test */static final String DEF_HAVE_TIME_DAYS = "还有 %s 天";
    /* for test */static final String DEF_UNKNOWN = "未知";
    /* for test */static final String DEF_JUST_NOW = "刚刚";
    /* for test */static final String DEF_MINUTES_AGO = "%s 分钟前";
    /* for test */static final String DEF_HOURS_AGO = "%s 小时前";
    /* for test */static final String DEF_TODAY = "今天 %s";
    /* for test */static final String DEF_YESTERDAY = "昨天 %s";

    private static final Map<String, String> DEF_MESSAGE_CACHED = Maps.newHashMap();

    static {
        DEF_MESSAGE_CACHED.put(KEY_HAVE_SECONDS, DEF_HAVE_TIME_SECONDS);
        DEF_MESSAGE_CACHED.put(KEY_HAVE_MINUTES, DEF_HAVE_TIME_MINUTES);
        DEF_MESSAGE_CACHED.put(KEY_HAVE_HOURS, DEF_HAVE_TIME_HOURS);
        DEF_MESSAGE_CACHED.put(KEY_HAVE_DAYS, DEF_HAVE_TIME_DAYS);
        DEF_MESSAGE_CACHED.put(KEY_UNKNOWN, DEF_UNKNOWN);
        DEF_MESSAGE_CACHED.put(KEY_JUST_NOW, DEF_JUST_NOW);
        DEF_MESSAGE_CACHED.put(KEY_MINUTES_AGO, DEF_MINUTES_AGO);
        DEF_MESSAGE_CACHED.put(KEY_HOURS_AGO, DEF_HOURS_AGO);
        DEF_MESSAGE_CACHED.put(KEY_TODAY, DEF_TODAY);
        DEF_MESSAGE_CACHED.put(KEY_YESTERDAY, DEF_YESTERDAY);
    }

    /**
     * 通过瞬间转为本地时间再格式化为基础的时间字符串格式。
     *
     * @param instant 瞬间。
     * @return 基础的时间字符串格式。
     */
    public static String localFormat(Instant instant) {
        Preconditions.checkNotNull(instant, "instant == null");
        return BASIC_FORMATTER.format(LocalDateTime.ofInstant(instant, ZoneId.systemDefault()));
    }

    /**
     * 显示两个瞬间的多久情况。
     * <p>
     * 如果基线时间在目标时间之前，那么显示还有多久；如果基线时间在目标时间之后，那么显示多久之前。
     * <p>
     * 之所以不用 Duration 类，是因为它仅支持 time 范围，要用 date 范围还得切换到 Period 类，并且它们的 toString 可读性都不友好。
     * <p>
     * 默认的显示规则如下：
     * <p>
     * baseline < target is have time
     * <p>
     * ChronoUnit.SECONDS -- 还有 N 秒
     * <p>
     * ChronoUnit.MINUTES -- 还有 N 分钟
     * <p>
     * ChronoUnit.HOURS   -- 还有 N 小时
     * <p>
     * ChronoUnit.DAYS    -- 还有 N 天
     * <p>
     * baseline >= target is time ago
     * <p>
     * 今天 & 1 分钟以内：刚刚
     * <p>
     * 今天 & 1 小时以内： N 分钟前
     * <p>
     * 1 小时以外 && 半天以内： N 小时前
     * <p>
     * 今天 && 半天以外： 今天 HH:mm
     * <p>
     * 昨天：昨天 HH:mm
     * <p>
     * 今年： MM-dd
     * <p>
     * 其他： yyyy-MM-dd
     */
    public static String howLong(Instant baseline, Instant target) {
        return howLong(baseline, target, DEF_MESSAGE_CACHED::get);
    }

    /**
     * 显示两个瞬间的多久情况。
     * <p>
     * 显示规则由传递的 messageApply 决定，参考 UNTIL_MESSAGE_CACHED 初始化时添加的常量。
     */
    public static String howLong(Instant baseline, Instant target, Function<String, String> messageApply) {
        Preconditions.checkNotNull(baseline, "baseline == null");
        Preconditions.checkNotNull(target, "target == null");
        Preconditions.checkNotNull(messageApply, "messageApply == null");

        // baseline < target == have time
        if (baseline.isBefore(target)) {
            return haveTime(baseline, target, messageApply);
        }
        // baseline >= target == time ago
        return timeAgo(baseline, target, messageApply);
    }

    /**
     * 根据基线时间和目标时间比较的结果，显示还有多久。
     * <p>
     * 注意：如果基线时间在目标时间之后，则显示 KEY_UNKNOWN 消息。
     */
    public static String haveTime(Instant baseline, Instant target) {
        return haveTime(baseline, target, DEF_MESSAGE_CACHED::get);
    }

    /**
     * 根据基线时间和目标时间比较的结果，显示还有多久。
     * <p>
     * 注意：如果基线时间在目标时间之后，则显示 KEY_UNKNOWN 消息。
     * <p>
     * 显示规则由传递的 messageApply 决定。
     */
    public static String haveTime(Instant baseline, Instant target, Function<String, String> messageApply) {
        Preconditions.checkNotNull(baseline, "baseline == null");
        Preconditions.checkNotNull(target, "target == null");
        Preconditions.checkNotNull(messageApply, "messageApply == null");

        if (baseline.isAfter(target)) {
            // baseline > target == unknown
            return messageApply.apply(KEY_UNKNOWN);
        }

        long days = baseline.until(target, ChronoUnit.DAYS);
        if (days > 0) {
            return Strings.lenientFormat(messageApply.apply(KEY_HAVE_DAYS), days);
        }

        long hours = baseline.until(target, ChronoUnit.HOURS);
        if (hours > 0) {
            return Strings.lenientFormat(messageApply.apply(KEY_HAVE_HOURS), hours);
        }

        long minutes = baseline.until(target, ChronoUnit.MINUTES);
        if (minutes > 0) {
            return Strings.lenientFormat(messageApply.apply(KEY_HAVE_MINUTES), minutes);
        }

        long seconds = baseline.until(target, ChronoUnit.SECONDS);
        return Strings.lenientFormat(messageApply.apply(KEY_HAVE_SECONDS), seconds);
    }

    /**
     * 根据基线时间和目标时间比较的结果，显示多久以前。
     * <p>
     * 注意：如果基线时间在目标时间之前，则显示 KEY_UNKNOWN 消息。
     */
    public static String timeAgo(Instant baseline, Instant target) {
        return timeAgo(baseline, target, DEF_MESSAGE_CACHED::get);
    }

    /**
     * 根据基线时间和目标时间比较的结果，显示多久以前。
     * <p>
     * 注意：如果基线时间在目标时间之前，则显示 KEY_UNKNOWN 消息。
     * <p>
     * 显示规则由传递的 messageApply 决定。
     */
    public static String timeAgo(Instant baseline, Instant target, Function<String, String> messageApply) {
        Preconditions.checkNotNull(baseline, "baseline == null");
        Preconditions.checkNotNull(target, "target == null");
        Preconditions.checkNotNull(messageApply, "messageApply == null");

        // baseline < target == past --> baseline --> target --> future
        if (baseline.isBefore(target)) {
            return messageApply.apply(KEY_UNKNOWN);
        }

        // baseline midnight -- [00:00:00]
        Instant baselineMidnight = baseline.truncatedTo(ChronoUnit.DAYS);
        // baseline midnight <= target <= baseline == today
        if (baselineMidnight.isBefore(target) || baselineMidnight.equals(target)) {
            // target until baseline < 1 minutes == just baseline
            long minutes = target.until(baseline, ChronoUnit.MINUTES);
            if (minutes == 0) {
                return messageApply.apply(KEY_JUST_NOW);
            }

            // target until baseline < 1 hours == N minutes ago
            long hours = target.until(baseline, ChronoUnit.HOURS);
            if (hours == 0) {
                String minutesAgoTemplate = messageApply.apply(KEY_MINUTES_AGO);
                return Strings.lenientFormat(minutesAgoTemplate, minutes);
            }

            // target until baseline < 1/2 day == N hours ago
            long halfDays = target.until(baseline, ChronoUnit.HALF_DAYS);
            if (halfDays == 0) {
                String hoursAgoTemplate = messageApply.apply(KEY_HOURS_AGO);
                return Strings.lenientFormat(hoursAgoTemplate, hours);
            }

            String todayTemplate = messageApply.apply(KEY_TODAY);
            String timeOfDays = SIMPLE_TIME_FORMATTER.format(LocalDateTime.ofInstant(target, ZoneId.systemDefault()));
            return Strings.lenientFormat(todayTemplate, timeOfDays);
        }

        // baseline - 1 day midnight
        Instant baselineMinusDayMidnight = baseline.minus(1, ChronoUnit.DAYS)
                .truncatedTo(ChronoUnit.DAYS);
        // baseline - 1 day midnight <= target < baseline midnight == yesterday
        if (baselineMinusDayMidnight.isBefore(target) || baselineMinusDayMidnight.equals(target)) {
            String yesterdayTemplate = messageApply.apply(KEY_YESTERDAY);
            String timeOfDays = SIMPLE_TIME_FORMATTER.format(LocalDateTime.ofInstant(target, ZoneId.systemDefault()));
            return Strings.lenientFormat(yesterdayTemplate, timeOfDays);
        }

        //  midnight of first day at this year
        Instant thisYear = LocalDateTime.ofInstant(baseline, ZoneOffset.UTC)
                .withDayOfYear(1)
                .with(LocalTime.MIDNIGHT)
                .toInstant(ZoneOffset.UTC);
        // this year <= target <= yesterday midnight == this year
        if (thisYear.isBefore(target) || thisYear.equals(target)) {
            return SIMPLE_DATE_FORMATTER.format(LocalDateTime.ofInstant(target, ZoneId.systemDefault()));
        }

        return DateTimeFormatter.ISO_LOCAL_DATE.format(LocalDateTime.ofInstant(target, ZoneId.systemDefault()));
    }
}
