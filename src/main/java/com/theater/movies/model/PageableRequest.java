package com.theater.movies.model;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.Optional;

@SuperBuilder
public class PageableRequest {

    private Integer limit;

    @Setter
    private Long offset;

    public Integer getLimit() {
        return Optional.ofNullable(limit).orElse(20);
    }

    public Long getOffset() {
        return Optional.ofNullable(offset).orElse(0L);
    }
}
