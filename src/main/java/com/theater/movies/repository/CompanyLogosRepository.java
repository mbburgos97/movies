package com.theater.movies.repository;

import com.theater.movies.entity.CompanyLogoEntity;
import com.theater.movies.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CompanyLogosRepository extends PagingAndSortingRepository<CompanyLogoEntity, Long> {

    Page<CompanyLogoEntity> findAllByStatus(Status status, Pageable pageable);
}
