package com.theater.movies.service;

import com.theater.movies.entity.MovieEntity;
import com.theater.movies.entity.OffsetBasedPageRequest;
import com.theater.movies.enums.Status;
import com.theater.movies.exception.BadArgumentException;
import com.theater.movies.exception.MovieNotFoundException;
import com.theater.movies.model.*;
import com.theater.movies.repository.MovieRepository;
import com.theater.movies.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.theater.movies.util.DateUtil.parseStringDate;
import static com.theater.movies.util.FileUtil.saveFile;
import static com.theater.movies.util.PageRequestUtil.buildSort;
import static com.theater.movies.util.PageRequestUtil.checkPageableRequestIfValid;
import static com.theater.movies.util.ResponseBuilder.buildServerUri;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Log4j2
public class MovieService {
    private final MovieRepository movieRepository;

    public Response getMovies(MovieListRequest movieListRequest,
                              HttpServletRequest request) {
        checkPageableRequestIfValid(movieListRequest);

        var pagedMovies = getPagedMovieEntity(movieListRequest);
        var totalElements = pagedMovies.getTotalElements();

        return ResponseBuilder.buildResponse(MovieListResponse.builder()
                .movies(StreamSupport.stream(pagedMovies
                        .spliterator(), false)
                        .map(this::toModel)
                        .collect(Collectors.toList()))
                .count(totalElements)
                .next(buildNextUri(request, pagedMovies, movieListRequest.getLimit()))
                .previous(buildPreviousUri(request, pagedMovies, movieListRequest))
                .build());
    }

    public Response getMovie(Long id) {
        return ResponseBuilder.buildResponse(toModel(getMovieById(id)));
    }

    public Response createMovie(Movie movie, HttpServletRequest request) {
        if (!movie.getType() && movie.getConfidential()) {
            throw new BadArgumentException("Movie cannot be confidential if it is not ongoing.");
        }

        return ResponseBuilder.buildResponse(toModel(movieRepository.save(MovieEntity.builder()
                .title(movie.getTitle())
                .videoUrl(saveFile(movie.getVideo()))
                .imageUrl(saveFile(movie.getImage()))
                .content(movie.getContent())
                .director(movie.getDirector())
                .imdbScore(movie.getImdbScore())
                .returnRate(Math.toIntExact(movie.getInvestment() * 100 / movie.getReturnValue()))
                .returnValue(movie.getReturnValue())
                .payback(movie.getPayback())
                .ongoing(movie.getType())
                .year(movie.getYear())
                .investment(movie.getInvestment())
                .status(Status.ACTIVE)
                .createdAt(LocalDateTime.now())
                .createdBy(request.getUserPrincipal().getName())
                .awards(String.join(",", movie.getAwards()))
                .actors(movie.getActors())
                .build())));
    }

    public CommonResponse deleteMovie(Long id) {
        var movieEntity = getMovieById(id);

        movieRepository.deleteById(movieEntity.getId());
        return CommonResponse.builder()
                .timestamp(LocalDateTime.now())
                .message("Successfully deleted movie.")
                .status(HttpStatus.OK)
                .build();
    }

    public Response updateMovie(Movie movie, HttpServletRequest request) {
        var movieEntity = getMovieById(movie.getId());

        var type = ofNullable(movie.getType()).orElse(false);
        var confidential = ofNullable(movie.getConfidential()).orElse(false);

        if (!type && confidential) {
            throw new BadArgumentException("Movie cannot be confidential if it is not ongoing.");
        }

        Optional.of(movie.getTitle())
                .ifPresent(movieEntity::setTitle);
        Optional.of(movie.getImage())
                .ifPresent(image -> movieEntity.setImageUrl(saveFile(image)));
        Optional.of(movie.getVideo())
                .ifPresent(video -> movieEntity.setVideoUrl(saveFile(video)));
        Optional.of(movie.getAwards())
                .ifPresent(awards -> movieEntity.setAwards(String.join(",", awards)));
        Optional.of(movie.getActors())
                .ifPresent(movieEntity::setActors);
        Optional.of(movie.getDirector())
                .ifPresent(movieEntity::setDirector);
        Optional.of(movie.getInvestment())
                .ifPresent(movieEntity::setInvestment);
        Optional.of(movie.getPayback())
                .ifPresent(movieEntity::setPayback);
        Optional.of(movie.getReturnValue())
                .ifPresent(movieEntity::setReturnValue);
        Optional.of(movie.getImdbScore())
                .ifPresent(movieEntity::setImdbScore);
        Optional.of(movie.getYear())
                .ifPresent(movieEntity::setYear);
        Optional.of(movie.getType())
                .ifPresent(movieEntity::setOngoing);
        Optional.of(movie.getConfidential())
                .ifPresent(movieEntity::setConfidential);
        Optional.of(movie.getContent())
                .ifPresent(movieEntity::setContent);
        movieEntity.setUpdatedAt(LocalDateTime.now());
        movieEntity.setUpdatedBy(request.getUserPrincipal().getName());
        movieEntity.setReturnRate(Math.toIntExact(movieEntity.getInvestment() * 100 / movieEntity.getReturnValue()));
        return ResponseBuilder.buildResponse(toModel(movieRepository.save(movieEntity)));
    }

    public Response updateMovieStatus(Long id, Status status, HttpServletRequest request) {
        var movieEntity = getMovieById(id);

        Optional.of(status)
                .ifPresent(movieEntity::setStatus);
        movieEntity.setUpdatedAt(LocalDateTime.now());
        movieEntity.setUpdatedBy(request.getUserPrincipal().getName());

        return ResponseBuilder.buildResponse(toModel(movieRepository.save(movieEntity)));
    }

    private String buildPreviousUri(HttpServletRequest request, Page<MovieEntity> pagedMovies, MovieListRequest movieListRequest) {
        var currentOffset = movieListRequest.getOffset();
        if (pagedMovies.hasPrevious()) {
            return buildServerUri(request, pagedMovies.previousPageable().getOffset(), Long.valueOf(movieListRequest.getLimit()));
        } else if (currentOffset != 0) {
            return buildServerUri(request, 0L, currentOffset);
        }
        return null;
    }

    private String buildNextUri(HttpServletRequest request, Page<MovieEntity> pagedMovies, Integer limit) {
        if (pagedMovies.hasNext()) {
            return buildServerUri(request, pagedMovies.nextPageable().getOffset(), Long.valueOf(limit));
        }
        return null;
    }

    private Page<MovieEntity> getPagedMovieEntity(MovieListRequest movieListRequest) {
        switch (movieListRequest.getFilterBy()) {
            case "title": return movieRepository.findAllByTitleLike(
                    new OffsetBasedPageRequest(movieListRequest.getOffset(), movieListRequest.getLimit(),
                            buildSort(movieListRequest)),
                    movieListRequest.getFilter() + "%");
            case "status": return movieRepository.findAllByStatus(
                    new OffsetBasedPageRequest(movieListRequest.getOffset(), movieListRequest.getLimit(),
                            buildSort(movieListRequest)),
                    Status.fromInteger(Integer.valueOf(movieListRequest.getFilter())));

            case "type": return movieRepository.findAllByOngoing(
                    new OffsetBasedPageRequest(movieListRequest.getOffset(), movieListRequest.getLimit(),
                            buildSort(movieListRequest)),
                    Boolean.parseBoolean(movieListRequest.getFilter()));
            case "created_at":
                var date = parseStringDate(movieListRequest.getFilter(), movieListRequest.getIsBefore());

                if (movieListRequest.getIsBefore()) {
                    return movieRepository.findAllByCreatedAtLessThanEqual(
                            new OffsetBasedPageRequest(movieListRequest.getOffset(), movieListRequest.getLimit(),
                                    buildSort(movieListRequest)), date);
                }
                return movieRepository.findAllByCreatedAtGreaterThanEqual(
                        new OffsetBasedPageRequest(movieListRequest.getOffset(), movieListRequest.getLimit(),
                                buildSort(movieListRequest)), date);

            default: return movieRepository.findAll(new OffsetBasedPageRequest(movieListRequest.getOffset(), movieListRequest.getLimit(),
                    buildSort(movieListRequest)));
        }
    }

    private List<String> parseAwards(String awards) {
        if (awards.equals("0")) {
            return Collections.emptyList();
        }
        return Arrays.asList(awards.split(",", -1));
    }

    private MovieEntity getMovieById(Long id) {
        return movieRepository.findById(id)
                .orElseThrow(() -> new MovieNotFoundException("Movie with id " + id + " not found."));
    }

    private Movie toModel(MovieEntity movieEntity) {
        return Movie.builder()
                .id(movieEntity.getId())
                .title(movieEntity.getTitle())
                .imageUrl(movieEntity.getImageUrl())
                .videoUrl(movieEntity.getVideoUrl())
                .content(movieEntity.getContent())
                .awards(parseAwards(movieEntity.getAwards()))
                .payback(movieEntity.getPayback())
                .returnValue(movieEntity.getReturnValue())
                .returnRate(concatPercent(movieEntity.getReturnRate()))
                .investment(movieEntity.getInvestment())
                .imdbScore(movieEntity.getImdbScore())
                .year(movieEntity.getYear())
                .type(movieEntity.isOngoing())
                .confidential(movieEntity.isConfidential())
                .actors(movieEntity.getActors())
                .createdAt(movieEntity.getCreatedAt())
                .createdBy(movieEntity.getCreatedBy())
                .updatedAt(movieEntity.getUpdatedAt())
                .updatedBy(movieEntity.getUpdatedBy())
                .director(movieEntity.getDirector())
                .status(movieEntity.getStatus())
                .build();
    }

    private String concatPercent(Integer returnRate) {
        return ofNullable(returnRate).map(rate -> rate + "%")
                .orElse(null);
    }
}
