package com.koolearn.guonei.common;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;
import java.util.Locale;

/**
 * Created with IntelliJ IDEA.
 *
 * 时间工具（jdk1.8 java.time包下的方法，没用simpledateFormat，jodatime，dateformat等库）
 *
 * @author: chenzhongyong
 * Date: 2018/9/3
 * Time: 13:51
 */
public class DateTimeUtil {

    /**
     * 获取start end之间的日期
     * @param start
     * @param end
     * @return
     */
    public static String getTimeSpan(Date start, Date end) {
        if (start == null || end == null) {
            return null;
        }
        //fix poi处理日期差一秒的bug
        LocalDateTime startLdt = DateTimeUtil.date2localDateTime(start);
        LocalDateTime endLdt = DateTimeUtil.date2localDateTime(end);
        if (startLdt.getSecond() == 59) {
            startLdt = startLdt.plusSeconds(1);
            start = DateTimeUtil.localDateTime2Date(startLdt);
        }
        if (endLdt.getSecond() == 59) {
            endLdt = endLdt.plusSeconds(1);
            end = DateTimeUtil.localDateTime2Date(endLdt);
        }

        String startTimeStr = DateTimeUtil.getTime(start);
        String endTimeStr = DateTimeUtil.getTime(end);
        return String.format("%s-%s", startTimeStr, endTimeStr);
    }

    /**
     * 字符串转日期
     * @param timeStr
     * @return
     */
    public static Date str2Date(String timeStr) {
        DateTimeFormatter nomalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime localDateTime = LocalDateTime.parse(timeStr, nomalFormat);
        return localDateTime2Date(localDateTime);
    }

    public static String getThisWeekSpanStr() {
        LocalDate now = LocalDate.now();
        LocalDate nextWeekMonday = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate thisWeekMonday = nextWeekMonday.plus(-1, ChronoUnit.WEEKS);

        Date thisMonday = DateTimeUtil.localDate2Date(thisWeekMonday);
        Date nextMonday = DateTimeUtil.localDate2Date(nextWeekMonday);

        String thisWeekSpan = String.format("%s - %s", getDateStr(thisMonday), getDateStr(nextMonday));

        return thisWeekSpan;
    }

    public static String getNextWeekSpanStr() {
        LocalDate now = LocalDate.now();
        LocalDate nextWeekMonday = now.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        LocalDate nextNextWeekMonday = nextWeekMonday.plus(1, ChronoUnit.WEEKS);

        Date nextMonday = DateTimeUtil.localDate2Date(nextWeekMonday);
        Date nextNextMonday = DateTimeUtil.localDate2Date(nextNextWeekMonday);

        String thisWeekSpan = String.format("%s - %s", getDateStr(nextMonday), getDateStr(nextNextMonday));

        return thisWeekSpan;
    }


    public static Date localDate2Date(LocalDate localDate) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDate.atStartOfDay(zoneId);
        return Date.from(zdt.toInstant());
    }

    public static Date localDateTime2Date(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zdt = localDateTime.atZone(zoneId);
        return Date.from(zdt.toInstant());
    }

    public static LocalDateTime date2localDateTime(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        return instant.atZone(zoneId).toLocalDateTime();
    }

    public static LocalDate date2localDate(Date date) {
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        return instant.atZone(zoneId).toLocalDate();
    }

    public static String getDateStr(Date date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter nomalFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();

        return localDate.format(nomalFormat);
    }

    public static String getTime(Date date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter nomalFormat = DateTimeFormatter.ofPattern("HH:mm");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();

        return localDate.format(nomalFormat);
    }

    public static String getDate(Date date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter nomalFormat = DateTimeFormatter.ofPattern("MM月dd日");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();

        return localDate.format(nomalFormat);
    }

    public static String getYearDate(Date date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter nomalFormat = DateTimeFormatter.ofPattern("yyyy年MM月dd日");
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();

        return localDate.format(nomalFormat);
    }

    public static String getWeekDay(Date date) {
        if (date == null) {
            return "";
        }
        DateTimeFormatter nomalFormat = DateTimeFormatter.ofPattern("E", Locale.SIMPLIFIED_CHINESE);
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        // atZone()方法返回在指定时区从此Instant生成的ZonedDateTime。
        LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();

        String weekDay = localDate.format(nomalFormat);

        weekDay = weekDay.replaceAll("星期", "周");

        return weekDay;
    }
}
