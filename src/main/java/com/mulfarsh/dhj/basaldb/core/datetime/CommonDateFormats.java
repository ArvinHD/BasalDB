package com.mulfarsh.dhj.basaldb.core.datetime;

import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

public interface CommonDateFormats {

    public interface Pattern {
        String ISO_LOCAL_DATE_TIME_PATTERN = "yyyy-MM-dd'T'HH:mm:ss";
        String ISO_LOCAL_DATE_PATTERN = "yyyy-MM-dd";
        String ISO_LOCAL_TIME_PATTERN = "HH:mm:ss";
        String YYYY_MM_DD_HH_MM_SS_PATTERN = "yyyy-MM-dd HH:mm:ss";
        String YYYY_MM_DD_PATTERN = "yyyy-MM-dd";
        String HH_MM_SS_PATTERN = "HH:mm:ss";
        String YYYY_MM_DD_SLASH_HH_MM_SS_PATTERN = "yyyy/MM/dd HH:mm:ss";
        String YYYY_MM_DD_SLASH_PATTERN = "yyyy/MM/dd";
        String MM_DD_YYYY_HH_MM_SS_PATTERN = "MM/dd/yyyy HH:mm:ss";
        String MM_DD_YYYY_PATTERN = "MM/dd/yyyy";
        String DD_MM_YYYY_PATTERN = "dd-MM-yyyy";
        String DD_MM_YYYY_HH_MM_SS_PATTERN = "dd-MM-yyyy HH:mm:ss";
        String DD_MM_YYYY_SLASH_PATTERN = "dd/MM/yyyy";
        String DD_MM_YYYY_SLASH_HH_MM_SS_PATTERN = "dd/MM/yyyy HH:mm:ss";
        String EEE_DD_MMM_YYYY_HH_MM_SS_Z_PATTERN = "EEE, dd MMM yyyy HH:mm:ss z";
        String EEEE_MMMM_DD_YYYY_PATTERN = "EEEE, MMMM dd, yyyy";
        String YYYYMMDD_PATTERN = "yyyyMMdd";
        String YYYYMMDDHHMMSS_PATTERN = "yyyyMMddHHmmss";
        String HHMMSS_PATTERN = "HHmmss";
        String EEE_DD_MMM_YYYY_PATTERN = "EEE, dd MMM yyyy";
    }

    public interface Formatter {
        DateTimeFormatter ISO_LOCAL_DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern(Pattern.ISO_LOCAL_DATE_TIME_PATTERN);
        DateTimeFormatter ISO_LOCAL_DATE_FORMATTER = DateTimeFormatter.ofPattern(Pattern.ISO_LOCAL_DATE_PATTERN);
        DateTimeFormatter ISO_LOCAL_TIME_FORMATTER = DateTimeFormatter.ofPattern(Pattern.ISO_LOCAL_TIME_PATTERN);
        DateTimeFormatter YYYY_MM_DD_HH_MM_SS_FORMATTER = DateTimeFormatter.ofPattern(Pattern.YYYY_MM_DD_HH_MM_SS_PATTERN);
        DateTimeFormatter YYYY_MM_DD_FORMATTER = DateTimeFormatter.ofPattern(Pattern.YYYY_MM_DD_PATTERN);
        DateTimeFormatter HH_MM_SS_FORMATTER = DateTimeFormatter.ofPattern(Pattern.HH_MM_SS_PATTERN);
        DateTimeFormatter YYYY_MM_DD_SLASH_HH_MM_SS_FORMATTER = DateTimeFormatter.ofPattern(Pattern.YYYY_MM_DD_SLASH_HH_MM_SS_PATTERN);
        DateTimeFormatter YYYY_MM_DD_SLASH_FORMATTER = DateTimeFormatter.ofPattern(Pattern.YYYY_MM_DD_SLASH_PATTERN);
        DateTimeFormatter MM_DD_YYYY_HH_MM_SS_FORMATTER = DateTimeFormatter.ofPattern(Pattern.MM_DD_YYYY_HH_MM_SS_PATTERN);
        DateTimeFormatter MM_DD_YYYY_FORMATTER = DateTimeFormatter.ofPattern(Pattern.MM_DD_YYYY_PATTERN);
        DateTimeFormatter DD_MM_YYYY_FORMATTER = DateTimeFormatter.ofPattern(Pattern.DD_MM_YYYY_PATTERN);
        DateTimeFormatter DD_MM_YYYY_HH_MM_SS_FORMATTER = DateTimeFormatter.ofPattern(Pattern.DD_MM_YYYY_HH_MM_SS_PATTERN);
        DateTimeFormatter DD_MM_YYYY_SLASH_FORMATTER = DateTimeFormatter.ofPattern(Pattern.DD_MM_YYYY_SLASH_PATTERN);
        DateTimeFormatter DD_MM_YYYY_SLASH_HH_MM_SS_FORMATTER = DateTimeFormatter.ofPattern(Pattern.DD_MM_YYYY_SLASH_HH_MM_SS_PATTERN);
        DateTimeFormatter EEE_DD_MMM_YYYY_HH_MM_SS_Z_FORMATTER = DateTimeFormatter.ofPattern(Pattern.EEE_DD_MMM_YYYY_HH_MM_SS_Z_PATTERN);
        DateTimeFormatter EEEE_MMMM_DD_YYYY_FORMATTER = DateTimeFormatter.ofPattern(Pattern.EEEE_MMMM_DD_YYYY_PATTERN);
        DateTimeFormatter YYYYMMDD_FORMATTER = DateTimeFormatter.ofPattern(Pattern.YYYYMMDD_PATTERN);
        DateTimeFormatter YYYYMMDDHHMMSS_FORMATTER = DateTimeFormatter.ofPattern(Pattern.YYYYMMDDHHMMSS_PATTERN);
        DateTimeFormatter HHMMSS_FORMATTER = DateTimeFormatter.ofPattern(Pattern.HHMMSS_PATTERN);
        DateTimeFormatter EEE_DD_MMM_YYYY_FORMATTER = DateTimeFormatter.ofPattern(Pattern.EEE_DD_MMM_YYYY_PATTERN);
    }



    List<DateTimeFormatter> FORMATTERS = Arrays.asList(
            Formatter.ISO_LOCAL_DATE_TIME_FORMATTER,
            Formatter.ISO_LOCAL_DATE_FORMATTER,
            Formatter.ISO_LOCAL_TIME_FORMATTER,
            Formatter.YYYY_MM_DD_HH_MM_SS_FORMATTER,
            Formatter.YYYY_MM_DD_FORMATTER,
            Formatter.HH_MM_SS_FORMATTER,
            Formatter.YYYY_MM_DD_SLASH_HH_MM_SS_FORMATTER,
            Formatter.YYYY_MM_DD_SLASH_FORMATTER,
            Formatter.MM_DD_YYYY_HH_MM_SS_FORMATTER,
            Formatter.MM_DD_YYYY_FORMATTER,
            Formatter.DD_MM_YYYY_FORMATTER,
            Formatter.DD_MM_YYYY_HH_MM_SS_FORMATTER,
            Formatter.DD_MM_YYYY_SLASH_FORMATTER,
            Formatter.DD_MM_YYYY_SLASH_HH_MM_SS_FORMATTER,
            Formatter.EEE_DD_MMM_YYYY_HH_MM_SS_Z_FORMATTER,
            Formatter.EEEE_MMMM_DD_YYYY_FORMATTER,
            Formatter.YYYYMMDD_FORMATTER,
            Formatter.YYYYMMDDHHMMSS_FORMATTER,
            Formatter.HHMMSS_FORMATTER,
            Formatter.EEE_DD_MMM_YYYY_FORMATTER
    );
}