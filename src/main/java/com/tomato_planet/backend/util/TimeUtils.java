package com.tomato_planet.backend.util;

import org.apache.commons.lang3.time.DateUtils;
import org.springframework.web.bind.annotation.PutMapping;

import java.time.*;
import java.time.temporal.WeekFields;
import java.util.Date;

/**
 * 时间工具类
 *
 * @author jianping5
 * @createDate 2022/11/8 20:23
 */

public class TimeUtils {

    public static boolean isSameDay(LocalDateTime localDateTime, LocalDateTime now) {
        Date nowDate = convertToDate(now);
        Date date = convertToDate(localDateTime);
        return DateUtils.isSameDay(nowDate, date);
    }

    public static boolean isSameWeek(LocalDateTime localDateTime, LocalDateTime now) {
        WeekFields weekFields = WeekFields.ISO;
        // 按年第几周
        int week = localDateTime.get(weekFields.weekOfWeekBasedYear());
        int nowWeek = now.get(weekFields.weekOfWeekBasedYear());
        return localDateTime.getYear() == now.getYear() && week == nowWeek;
    }

    public static boolean isSameMonth(LocalDateTime localDateTime, LocalDateTime now) {
        int year = localDateTime.getYear();
        Month month = localDateTime.getMonth();
        int nowYear = now.getYear();
        Month nowMonth = now.getMonth();
        return year == nowYear && month == nowMonth;
    }

    public static boolean isSameYear(LocalDateTime localDateTime, LocalDateTime now) {
        int year = localDateTime.getYear();
        int nowYear = now.getYear();
        return year == nowYear;
    }

    public static Date convertToDate(LocalDateTime localDateTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime =localDateTime.atZone(zoneId);
        Instant instant = zonedDateTime.toInstant();
        Date date = Date.from(instant);
        return date;
    }


}

