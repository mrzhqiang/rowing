package com.github.mrzhqiang.rowing.util;

import com.google.common.base.Strings;
import org.assertj.core.util.Maps;
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class DateTimesTest {

    private Instant baseline;

    @Before
    public void init() {
        baseline = Instant.now();
        // 确保 midnight 时刻也符合预期
        //baseline = Instant.now().truncatedTo(ChronoUnit.DAYS);
        //baseline = Instant.now().truncatedTo(ChronoUnit.DAYS).plusSeconds(1);
        //baseline = Instant.now().truncatedTo(ChronoUnit.DAYS).minusSeconds(1);
        //baseline = Instant.now().truncatedTo(ChronoUnit.DAYS).plusMillis(1);
        //baseline = Instant.now().truncatedTo(ChronoUnit.DAYS).minusMillis(1);
        //baseline = Instant.now().truncatedTo(ChronoUnit.DAYS).plusNanos(1);
        //baseline = Instant.now().truncatedTo(ChronoUnit.DAYS).minusNanos(1);
    }

    @Test(expected = NullPointerException.class)
    public void howLongNPE() {
        DateTimes.howLong(null, null);
    }

    @Test
    public void howLong() {
        // just now
        String justNow = DateTimes.howLong(baseline, baseline);
        assertEquals(DateTimes.DEF_JUST_NOW, justNow);

        // just now for message
        String justNowMessage = DateTimes.howLong(baseline, baseline,
                key -> Maps.newHashMap(DateTimes.KEY_JUST_NOW, "就在刚刚").get(key));
        assertEquals("就在刚刚", justNowMessage);
    }

    @Test
    public void howLongSeconds() {
        int seconds = 10;
        Instant target = baseline.plusSeconds(seconds);
        String actual = DateTimes.howLong(baseline, target);
        String expected = Strings.lenientFormat(DateTimes.DEF_HAVE_TIME_SECONDS, seconds);
        assertEquals(expected, actual);

        String haveTimeMessage = DateTimes.howLong(baseline, target,
                key -> Maps.newHashMap(DateTimes.KEY_HAVE_SECONDS, "have %s seconds").get(key));
        assertEquals("have 10 seconds", haveTimeMessage);
    }

    @Test
    public void howLongMinutes() {
        int minutes = 1;
        Instant target = baseline.plus(minutes, ChronoUnit.MINUTES);
        String actual = DateTimes.howLong(baseline, target);
        String expected = Strings.lenientFormat(DateTimes.DEF_HAVE_TIME_MINUTES, minutes);
        assertEquals(expected, actual);

        String haveTimeMessage = DateTimes.howLong(baseline, target,
                key -> Maps.newHashMap(DateTimes.KEY_HAVE_MINUTES, "have %s minutes").get(key));
        assertEquals("have 1 minutes", haveTimeMessage);

        Instant midnight = LocalDateTime.ofInstant(baseline, ZoneOffset.UTC)
                .with(LocalTime.MIDNIGHT)
                .toInstant(ZoneOffset.UTC);
        if (midnight.until(baseline, ChronoUnit.MINUTES) < minutes) {
            return;
        }

        target = baseline.minus(minutes, ChronoUnit.MINUTES);
        actual = DateTimes.howLong(baseline, target);
        expected = Strings.lenientFormat(DateTimes.DEF_MINUTES_AGO, minutes);
        assertEquals(expected, actual);

        String timeAgoMessage = DateTimes.howLong(baseline, target,
                key -> Maps.newHashMap(DateTimes.KEY_MINUTES_AGO, "%s minutes ago").get(key));
        assertEquals("1 minutes ago", timeAgoMessage);

    }

    @Test
    public void howLongHours() {
        int hours = 1;
        Instant target = baseline.plus(hours, ChronoUnit.HOURS);
        String actual = DateTimes.howLong(baseline, target);
        String expected = Strings.lenientFormat(DateTimes.DEF_HAVE_TIME_HOURS, hours);
        assertEquals(expected, actual);

        String haveTimeMessage = DateTimes.howLong(baseline, target,
                key -> Maps.newHashMap(DateTimes.KEY_HAVE_HOURS, "have %s hours").get(key));
        assertEquals("have 1 hours", haveTimeMessage);

        Instant midnight = LocalDateTime.ofInstant(baseline, ZoneOffset.UTC)
                .with(LocalTime.MIDNIGHT)
                .toInstant(ZoneOffset.UTC);
        if (midnight.until(baseline, ChronoUnit.HOURS) < hours) {
            return;
        }

        target = baseline.minus(hours, ChronoUnit.HOURS);
        actual = DateTimes.howLong(baseline, target);
        expected = Strings.lenientFormat(DateTimes.DEF_HOURS_AGO, hours);
        assertEquals(expected, actual);

        String timeAgoMessage = DateTimes.howLong(baseline, target,
                key -> Maps.newHashMap(DateTimes.KEY_HOURS_AGO, "%s hours ago").get(key));
        assertEquals("1 hours ago", timeAgoMessage);
    }

    @Test
    public void howLongDays() {
        int days = 1;
        Instant target = baseline.plus(days, ChronoUnit.DAYS);
        String actual = DateTimes.howLong(baseline, target);
        String expected = Strings.lenientFormat(DateTimes.DEF_HAVE_TIME_DAYS, days);
        assertEquals(expected, actual);

        String haveTimeMessage = DateTimes.howLong(baseline, target,
                key -> Maps.newHashMap(DateTimes.KEY_HAVE_DAYS, "have %s days").get(key));
        assertEquals("have 1 days", haveTimeMessage);

        Instant midnight = LocalDateTime.ofInstant(baseline, ZoneOffset.UTC)
                .with(LocalTime.MIDNIGHT)
                .toInstant(ZoneOffset.UTC);
        if (midnight.until(baseline, ChronoUnit.HOURS) >= 12) {
            target = baseline.minus(days, ChronoUnit.HALF_DAYS);
            actual = DateTimes.howLong(baseline, target);
            expected = Strings.lenientFormat(DateTimes.DEF_TODAY,
                    DateTimes.SIMPLE_TIME_FORMATTER.format(LocalDateTime.ofInstant(target, ZoneId.systemDefault())));
            assertEquals(expected, actual);

            String timeAgoMessage = DateTimes.howLong(baseline, target,
                    key -> Maps.newHashMap(DateTimes.KEY_TODAY, "today %s").get(key));
            String today = DateTimes.SIMPLE_TIME_FORMATTER.format(LocalDateTime.ofInstant(target, ZoneId.systemDefault()));
            assertEquals("today " + today, timeAgoMessage);
        }

        target = baseline.minus(days, ChronoUnit.DAYS);
        actual = DateTimes.howLong(baseline, target);
        expected = Strings.lenientFormat(DateTimes.DEF_YESTERDAY,
                DateTimes.SIMPLE_TIME_FORMATTER.format(LocalDateTime.ofInstant(target, ZoneId.systemDefault())));
        assertEquals(expected, actual);

        String timeAgoMessage = DateTimes.howLong(baseline, target,
                key -> Maps.newHashMap(DateTimes.KEY_YESTERDAY, "yesterday %s").get(key));
        String yesterday = DateTimes.SIMPLE_TIME_FORMATTER.format(LocalDateTime.ofInstant(target, ZoneId.systemDefault()));
        assertEquals("yesterday " + yesterday, timeAgoMessage);
    }

    @Test
    public void howLongThisYear() {
        Instant thisYearMidNight = LocalDateTime.ofInstant(baseline, ZoneOffset.UTC)
                .withDayOfYear(1)
                .with(LocalTime.MIDNIGHT)
                .toInstant(ZoneOffset.UTC);

        // skipped today and yesterday
        if (thisYearMidNight.until(baseline, ChronoUnit.DAYS) > 2) {
            String actual = DateTimes.howLong(baseline, thisYearMidNight);
            String expected = DateTimes.SIMPLE_DATE_FORMATTER.format(
                    LocalDateTime.ofInstant(thisYearMidNight, ZoneId.systemDefault()));
            assertEquals(expected, actual);
        }
    }

    @Test
    public void howLongNormal() {
        Instant otherYearMidNight = LocalDateTime.ofInstant(baseline, ZoneOffset.UTC)
                .minusYears(1)
                .withDayOfYear(1)
                .with(LocalTime.MIDNIGHT)
                .toInstant(ZoneOffset.UTC);
        String actual = DateTimes.howLong(baseline, otherYearMidNight);
        assertEquals(DateTimeFormatter.ISO_LOCAL_DATE.format(
                LocalDateTime.ofInstant(otherYearMidNight, ZoneId.systemDefault())), actual);
    }
}