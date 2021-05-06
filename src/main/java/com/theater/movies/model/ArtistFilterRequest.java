package com.theater.movies.model;

import com.theater.movies.enums.Status;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ArtistFilterRequest extends PageableRequest {

    private String name;

    private Status status;

    private Boolean isBefore;

    private String createdAt;
}
