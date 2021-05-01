package com.theater.movies.repository;

import com.theater.movies.entity.MovieEntity;
import com.theater.movies.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.time.LocalDateTime;

public interface MovieRepository extends PagingAndSortingRepository<MovieEntity, Long> {
    Page<MovieEntity> findAllByTitleLike(Pageable pageable, String title);

    Page<MovieEntity> findAllByStatus(Pageable pageable, Status status);

    Page<MovieEntity> findAllByOngoing(Pageable pageable, boolean ongoing);

    Page<MovieEntity> findAllByCreatedAtLessThanEqual(Pageable pageable, LocalDateTime createdAt);

    Page<MovieEntity> findAllByCreatedAtGreaterThanEqual(Pageable pageable, LocalDateTime createdAt);
}
