package com.theater.movies.entity;

import com.theater.movies.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity(name = "company_logos")
@Builder
@Table(name = "company_logos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyLogoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String imageUrl;

    private String websiteLink;

    private OffsetDateTime createdAt;

    private String createdBy;

    private OffsetDateTime updatedAt;

    private String updatedBy;

    private Status status;
}
