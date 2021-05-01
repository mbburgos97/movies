package com.theater.movies.entity;

import com.theater.movies.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Builder
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MovieEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String title;

    private String imageUrl;

    private String videoUrl;

    private String content;

    private LocalDateTime createdAt;

    private String createdBy;

    private LocalDateTime updatedAt;

    private String updatedBy;

    private Status status;

    private Integer year;

    private String director;

    private String actors;

    private String awards;

    private BigDecimal imdbScore;

    private Long investment;

    private Long returnValue;

    private Integer returnRate;

    private Integer payback;

    @Column(name = "is_ongoing")
    private boolean ongoing;

    @Column(name = "is_confidential")
    private boolean confidential;
}
