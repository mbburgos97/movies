package com.theater.movies.model;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class MovieListResponse {

    private List<Movie> movies;

    private Long count;

    private String next;

    private String previous;
}
