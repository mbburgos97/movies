package com.theater.movies.entity;

import com.theater.movies.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity(name = "artists")
@Builder
@Table(name = "artists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imageUrl;

    private String name;

    private String description;

    private Status status;

    private OffsetDateTime createdAt;

    private String createdBy;

    private OffsetDateTime updatedAt;

    private String updatedBy;
}
