package com.theater.movies.repository;

import com.theater.movies.entity.MovieEntity;
import com.theater.movies.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface MovieRepository extends PagingAndSortingRepository<MovieEntity, Long> {

    @Query("SELECT m FROM movies m WHERE (:title is null or m.title LIKE :title) AND (:type is null or m.type = :type) " +
            "AND (:createdAt is null or m.createdAt <= :createdAt) AND m.status = :status")
    Page<MovieEntity> findAllByTitleAndTypeAndCreatedAtGreaterThanEqualAndStatus(@Param("title") String title, @Param("type") String type,
                                                                                 @Param("createdAt") LocalDateTime createdAt, @Param("status") Status status,
                                                                                 Pageable pageable);

    @Query("SELECT m FROM movies m WHERE (:title is null or m.title LIKE :title) AND (:type is null or m.type = :type) " +
            "AND (:createdAt is null or m.createdAt >= :createdAt) AND m.status = :status")
    Page<MovieEntity> findAllByTitleAndTypeAndCreatedAtLessThanEqualAndStatus(@Param("title") String title, @Param("type") String type,
                                                                              @Param("createdAt") LocalDateTime createdAt, @Param("status") Status status,
                                                                              Pageable pageable);
}
