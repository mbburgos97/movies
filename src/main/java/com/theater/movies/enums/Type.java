package com.theater.movies.enums;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Type {
    ONGOING("ongoing"),
    PREVIOUS("previous");

    @JsonValue
    private final String type;

    public static Type fromString(String type) {
        if (type == null) return null;
        if (type.equals(ONGOING.getType())) return ONGOING;
        return PREVIOUS;
    }
}
