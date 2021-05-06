package com.theater.movies.model;

import com.theater.movies.enums.Status;
import com.theater.movies.enums.Type;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class MovieFilterRequest extends PageableRequest {

    private Status status;

    private String title;

    private Type type;

    private String sortBy;

    private Boolean isAscending;

    private Boolean isBefore;

    private String createdAt;
}
