package com.theater.movies.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class ListResponse {

    private List<Object> results;

    private Long count;

    private String next;

    private String previous;
}
