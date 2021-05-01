package com.theater.movies.model;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class MovieListRequest extends PageableRequest {

    private String filterBy;

    private String filter;

    private String sortBy;

    private Boolean isAscending;

    private Boolean isBefore;
}
