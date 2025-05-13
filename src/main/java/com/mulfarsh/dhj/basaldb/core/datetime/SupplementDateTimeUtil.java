package com.mulfarsh.dhj.basaldb.core.datetime;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Date;

public class SupplementDateTimeUtil {



    /**
     * Computes the last second of a specific month and year and returns it as a LocalDateTime object.
     *
     * @param year  the year for which the last second of the month is calculated, must not be null.
     * @param month the month (1-12) for which the last second is calculated, must not be null or outside valid range.
     * @return a LocalDateTime representing the last second of the given year and month.
     *         For example, if the input is year = 2023, month = 1, the output will correspond to "2023-01-31T23:59:59".
     */
    public static LocalDateTime getLastSecondOfMonth(Integer year, Integer month) {
        YearMonth yearMonth = YearMonth.of(year, month);
        return yearMonth.atEndOfMonth().atTime(23, 59, 59);
    }
    
    /**
     * Converts a given Date object to a LocalDateTime object representing the last second
     * of the month of the provided date.
     *
     * @param date the Date object for which the last second of the month is calculated,
     *             must not be null.
     * @return a LocalDateTime representing the last second of the month derived from
     *         the provided Date object.
     */
    public static LocalDateTime getLastSecondOfMonthByDate(Date date) {
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        return getLastSecondOfMonth(localDate.getYear(), localDate.getMonthValue());
    }
    
    /**
     * Computes the last second of the month based on the given LocalDateTime object.
     *
     * @param localDateTime the LocalDateTime object representing the specific date and time,
     *                      must not be null.
     * @return a LocalDateTime representing the last second of the month derived 
     *         from the provided LocalDateTime object.
     */
    public static LocalDateTime getLastSecondOfMonthByLocalDate(LocalDateTime localDateTime) {
        return getLastSecondOfMonth(localDateTime.getYear(), localDateTime.getMonthValue());
    }

    /**
     * Determines the last second of the month for a given date string.
     * The provided date string is parsed into a LocalDateTime, and the last second 
     * of the corresponding month is calculated and returned.
     *
     * @param dateStr the date string to be processed, must not be null or improperly formatted.
     * @return a LocalDateTime representing the last second of the month for the provided date string,
     *         or null if the date string cannot be parsed.
     */
    public static LocalDateTime getLastSecondOfMonthByDateString(String dateStr) {
        LocalDateTime localDateTime = convertToLocalDateTime(dateStr);
        return getLastSecondOfMonth(localDateTime.getYear(), localDateTime.getMonthValue());
    }

    /**
     * Computes the last second of the month from the provided date string and format.
     * Parses the date string using the given format and determines the last second 
     * of the month for the corresponding date.
     *
     * @param dateStr the date string to be parsed, must not be null or empty.
     * @param format the format of the provided date string, must not be null or invalid.
     * @return a LocalDateTime representing the last second of the month for the given date.
     *         If the date string cannot be parsed, returns null.
     */
    public static LocalDateTime getLastSecondOfMonthByDateString(String dateStr, String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
        try {
            LocalDateTime localDateTime = LocalDateTime.parse(dateStr, formatter);
            return getLastSecondOfMonth(localDateTime.getYear(), localDateTime.getMonthValue());
        } catch (DateTimeParseException e) {
            return null; // 如果解析失败则返回 null
        }
    }

    /**
     * 将各种格式的时间字符串统一转换成 LocalDateTime 对象。
     *
     * @param dateString 时间字符串。
     * @return LocalDateTime 对象，如果转换失败则返回 null。
     */
    public static LocalDateTime convertToLocalDateTime(String dateString) {
        

        for (DateTimeFormatter formatter : CommonDateFormats.FORMATTERS) {
            try {
                return LocalDateTime.parse(dateString, formatter);
            } catch (DateTimeParseException ignored) {}
        }

        return null; // 所有格式都无法解析
    }
}
