package com.theater.movies.repository;

import com.theater.movies.entity.ArtistEntity;
import org.springframework.data.repository.CrudRepository;

public interface ArtistRepository extends CrudRepository<ArtistEntity, Long> {
}
