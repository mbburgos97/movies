package com.theater.movies.entity;

import com.theater.movies.enums.LoginStatus;
import com.theater.movies.enums.Status;
import lombok.*;

import javax.persistence.*;
import java.time.OffsetDateTime;

@Entity
@Builder
@Table(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Setter
@ToString
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String createdBy;

    private OffsetDateTime createdAt;

    private String updatedBy;

    private OffsetDateTime updatedAt;

    private String memo;

    @Column(name = "login_status")
    private LoginStatus loginStatus;

    @Column(name = "status")
    private Status status;
}
