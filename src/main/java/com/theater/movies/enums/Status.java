package com.theater.movies.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Status {
    INACTIVE(0),
    ACTIVE(1);

    private final Integer status;

    @JsonValue
    public Integer value() {
        return status;
    }

    public static Status fromInteger(Integer id) {
        if (id == null) return ACTIVE;
        if (id == 0) return INACTIVE;
        return ACTIVE;
    }
}
