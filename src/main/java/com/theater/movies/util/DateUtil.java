package com.theater.movies.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateUtil {

    public static LocalDateTime parseStringDate(String date, Boolean isBefore) {
        LocalDate ld = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (isBefore) {
            return LocalDateTime.of(ld, LocalDateTime.MAX.toLocalTime());
        }
        return LocalDateTime.of(ld, LocalDateTime.MIN.toLocalTime());
    }
}
