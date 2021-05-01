package com.theater.movies.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum LoginStatus {
    OFFLINE(0),
    ACTIVE(1);

    private final Integer loginStatus;

    @JsonValue
    public String value() {
        return String.valueOf(loginStatus);
    }
}
