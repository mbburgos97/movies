package com.theater.movies.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class Response extends CommonResponse {

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private final Object data;
}
