package com.theater.movies.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

import java.time.OffsetDateTime;

@SuperBuilder
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class CommonResponse {

    protected HttpStatus status;

    protected OffsetDateTime timestamp;

    protected String message;
}
