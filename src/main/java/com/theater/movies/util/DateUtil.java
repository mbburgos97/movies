package com.theater.movies.util;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class DateUtil {

    public static OffsetDateTime parseStringDate(String date, Boolean isBefore) {
        if (date == null) return null;

        LocalDate ld = LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        if (isBefore) {
            return OffsetDateTime.of(LocalDateTime.of(ld, LocalDateTime.MAX.toLocalTime()), ZoneOffset.UTC);
        }
        return OffsetDateTime.of(LocalDateTime.of(ld, LocalDateTime.MIN.toLocalTime()), ZoneOffset.UTC);
    }
}
