package com.koolearn.guonei.common;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: chenzhongyong
 * Date: 2019/1/30
 * Time: 14:29
 */
public class JodaDateTimeUtil {

    private static final String STANDARD_FORMAT_STR = "yyyy-MM-dd HH:mm:ss";

    private static final String DATE_FORMAT_STR = "yyyy-MM-dd";

    private static final DateTimeFormatter FULL_FORMAT = DateTimeFormat.forPattern(STANDARD_FORMAT_STR);

    private static final DateTimeFormatter SHORT_FORMAT = DateTimeFormat.forPattern(DATE_FORMAT_STR);

    public static Date str2Date(String timeStr) {
        DateTime parse = DateTime.parse(timeStr, FULL_FORMAT);
        return parse.toDate();
    }

    public static Date str2ShortDate(String timeStr) {
        DateTime parse = DateTime.parse(timeStr, SHORT_FORMAT);
        return parse.toDate();
    }

    public static Date str2formatDate(String timeStr,String format) {
        DateTime parse = DateTime.parse(timeStr, DateTimeFormat.forPattern(format));
        return parse.toDate();
    }

    public static String getDateStr(Date date) {
        DateTime dateTime = new DateTime(date);
        return dateTime.toString(STANDARD_FORMAT_STR);
    }

    public static String getShowDate(Date toDate) {
        DateTime dateTime = new DateTime(toDate);
        return dateTime.toString(DATE_FORMAT_STR);
    }

    public static List<Date> getThisWeekMonDayAndSunDay() {
        DateTime startDateTime = new DateTime(new Date());
        //获取开始日期当周的星期一
        DateTime stWeekMonday = startDateTime.withDayOfWeek(1);
        //获取结束日期当周的星期天
        DateTime edWeekSunday = startDateTime.withDayOfWeek(7);

        List<Date> thisWeekList = new ArrayList<>();
        thisWeekList.add(stWeekMonday.toDate());
        thisWeekList.add(edWeekSunday.toDate());

        return thisWeekList;
    }

    public static void main(String[] args) {
        List<Date> thisWeekMonDayAndSunDay = getThisWeekMonDayAndSunDay();

        System.out.println(thisWeekMonDayAndSunDay);
    }
}
