package com.theater.movies.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.theater.movies.enums.Status;
import com.theater.movies.enums.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
@AllArgsConstructor
@Getter
public class Movie {

    private Long id;

    private String title;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MultipartFile image;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private MultipartFile video;

    private String imageUrl;

    private String videoUrl;

    private String content;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private Type type;

    private Boolean confidential;

    private Status status;

    private Integer year;

    private String director;

    private String actors;

    private List<String> awards;

    private BigDecimal imdbScore;

    private Long investment;

    @JsonProperty("return")
    private Long returnValue;

    private String returnRate;

    private Integer payback;
}
