package com.theater.movies.util;

import lombok.experimental.UtilityClass;

import java.util.Optional;

@UtilityClass
public class StringUtil {

    public static String addPercentToString(String parameter) {
        return Optional.ofNullable(parameter).map(param -> param + "%").orElse(null);
    }
}
