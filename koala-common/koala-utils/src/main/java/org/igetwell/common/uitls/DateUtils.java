package org.igetwell.common.uitls;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;
import java.util.Date;

public class DateUtils {

    private static final String DEFAULT_DATE_PATTERN="yyyy-MM-dd";
    private static final String DEFAULT_TIME_PATTERN="HH:mm:ss";
    private static final String DEFAULT_DATETIME_PATTERN="yyyy-MM-dd HH:mm:ss";

    /**
     * 获得当前时间
     * @return
     */
    public static LocalDateTime now() {
        return LocalDateTime.now();
    }

    /**
     * 获得当前时间
     * @return
     */
    public static LocalDate getDate() {
        return LocalDate.now();
    }

    /**
     * 获得当前时间
     * @return
     */
    public static LocalTime getTime() {
        return LocalTime.now();
    }

    /**
     * 获得当前时间戳
     * @return
     */
    public static long getTimestamp() {
        return Instant.now().toEpochMilli();
    }


    /** String ---> LocalDate */
    public static LocalDate parseToLocalDate(String date) {
        LocalDate localDate = parseToLocalDate(date, DEFAULT_DATE_PATTERN);
        return localDate;
    }

    public static LocalDate parseToLocalDate(String date, String pattern) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(pattern));
        return localDate;
    }

    /** String ---> LocalDateTime */
    public static LocalDateTime parseToLocalDateTime(String dateTime) {
        LocalDateTime localDatetime = parseToLocalDateTime(dateTime, DEFAULT_DATETIME_PATTERN);
        return localDatetime;
    }

    public static LocalDateTime parseToLocalDateTime(String dateTime, String pattern) {
        LocalDateTime localDatetime = LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern(pattern));
        return localDatetime;
    }

    /** String ---> LocalTime */
    public static LocalTime parseToLocalTime(String time) {
        LocalTime localTime = parseToLocalTime(time, DEFAULT_TIME_PATTERN);
        return localTime;
    }

    public static LocalTime parseToLocalTime(String time,String pattern) {
        LocalTime localTime = LocalTime.parse(time, DateTimeFormatter.ofPattern(pattern));
        return localTime;
    }

    /** LocalTime / LocalDate / LocalDateTime ---> String */
    public static String format(Temporal temporal, String pattern) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
        String date = dateFormatter.format(temporal);
        return date;
    }

    /**  LocalDate / LocalDateTime ---> String("yyyy-MM-dd") */
    public static String formatLocalDate(Temporal temporal){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATE_PATTERN);
        String date = dateFormatter.format(temporal);
        return date;
    }

    /** LocalTime / LocalDateTime ---> String ("HH:mm:ss") */
    public static String formatLocalTime(Temporal temporal){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_TIME_PATTERN);
        String time = dateFormatter.format(temporal);
        return time;
    }

    /** LocalDate / LocalDateTime ---> String ("yyyy-MM-dd HH:mm:ss")*/
    public static String formatLocalDateTime(Temporal temporal){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(DEFAULT_DATETIME_PATTERN);
        String dateTime = dateFormatter.format(temporal);
        return dateTime;
    }

    /** LocalDate / LocalDateTime ---> Date*/
    public static Date localDate2Date(LocalDateTime localDate){
//        ZonedDateTime zonedDateTime = localDate.atZone(ZoneId.systemDefault());
//        Instant instant1 = zonedDateTime.toInstant();
//        Date from = Date.from(instant1);
//        return  from;
        LocalDateTime localDateTime = LocalDateTime.now();
        ZoneId zone = ZoneId.systemDefault();
        Instant instant = localDateTime.atZone(zone).toInstant();
        Date date = Date.from(instant);
        return date;
    }

    /** Date ---> LocalDate / LocalDateTime*/
    public static LocalDateTime date2LocalDate(Date date){
        Instant instant = date.toInstant();
        ZoneId zone = ZoneId.systemDefault();
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, zone);
        return localDateTime;
    }

    public static void main(String[] args) {
        System.err.println("时间1："+ LocalDate.now());
        System.err.println("时间2："+ LocalTime.now());
        System.err.println("时间3："+ LocalDateTime.now());

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.err.println(dateTimeFormatter.format(LocalDateTime.now()));

        System.err.println(LocalDateTime.now().plusMinutes(30));
        System.err.println(LocalDateTime.now().plusMonths(1));
        System.err.println(LocalDate.now().plusDays(1));
        System.err.println(LocalDateTime.now().minusHours(1));

        LocalDateTime localDate = date2LocalDate(new Date());

        System.err.println(localDate.toString());
    }
}
