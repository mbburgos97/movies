package com.theater.movies.repository;

import com.theater.movies.entity.ArtistEntity;
import com.theater.movies.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;

public interface ArtistRepository extends PagingAndSortingRepository<ArtistEntity, Long> {

    @Query("SELECT a FROM artists a WHERE (:name is null or a.name LIKE :name) " +
            "AND (:createdAt is null or a.createdAt <= :createdAt) AND (:status is null or a.status = :status)")
    Page<ArtistEntity> findAllByNameAndCreatedAtGreaterThanEqualAndStatus(@Param("name") String name, @Param("createdAt") OffsetDateTime createdAt,
                                                                          @Param("status") Status status, Pageable pageable);

    @Query("SELECT a FROM artists a WHERE (:name is null or a.name LIKE :name) " +
            "AND (:createdAt is null or a.createdAt >= :createdAt) AND (:status is null or a.status = :status)")
    Page<ArtistEntity> findAllByNameAndCreatedAtLessThanEqualAndStatus(@Param("name") String name, @Param("createdAt") OffsetDateTime createdAt,
                                                                       @Param("status") Status status, Pageable pageable);

}
