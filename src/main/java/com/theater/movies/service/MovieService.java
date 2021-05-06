package com.theater.movies.service;

import com.theater.movies.entity.MovieEntity;
import com.theater.movies.entity.OffsetBasedPageRequest;
import com.theater.movies.enums.Status;
import com.theater.movies.enums.Type;
import com.theater.movies.exception.BadArgumentException;
import com.theater.movies.exception.MovieNotFoundException;
import com.theater.movies.model.*;
import com.theater.movies.repository.MovieRepository;
import com.theater.movies.util.ResponseBuilder;
import com.theater.movies.util.StringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.theater.movies.enums.Status.ACTIVE;
import static com.theater.movies.util.DateUtil.parseStringDate;
import static com.theater.movies.util.FileUtil.saveFile;
import static com.theater.movies.util.PageRequestUtil.*;
import static com.theater.movies.util.StringUtil.*;
import static java.util.Optional.ofNullable;

@Service
@RequiredArgsConstructor
@Log4j2
public class MovieService {
    private final MovieRepository movieRepository;

    public Response getMovies(MovieFilterRequest movieFilterRequest,
                              HttpServletRequest request) {
        checkPageableRequestIfValid(movieFilterRequest);

        var pagedMovies = getPagedMovieEntity(movieFilterRequest);
        var totalElements = pagedMovies.getTotalElements();

        return ResponseBuilder.buildResponse(ListResponse.builder()
                .results(StreamSupport.stream(pagedMovies
                        .spliterator(), false)
                        .map(this::toModel)
                        .collect(Collectors.toList()))
                .count(totalElements)
                .next(buildNextUri(request, pagedMovies, movieFilterRequest.getLimit()))
                .previous(buildPreviousUri(request, pagedMovies, movieFilterRequest))
                .build());
    }

    public Response getMovie(Long id) {
        return ResponseBuilder.buildResponse(toModel(getMovieById(id)));
    }

    public Response createMovie(Movie movie, HttpServletRequest request) {
        if (movie.getType() == Type.PREVIOUS && movie.getConfidential()) {
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
                .type(movie.getType().name())
                .year(movie.getYear())
                .investment(movie.getInvestment())
                .status(ACTIVE)
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
        var confidential = ofNullable(movie.getConfidential()).orElse(false);

        if (movie.getType() == Type.PREVIOUS && confidential) {
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
                .ifPresent(type -> movieEntity.setType(type.name()));
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



    private Page<MovieEntity> getPagedMovieEntity(MovieFilterRequest movieFilterRequest) {
        var typeString = Optional.ofNullable(movieFilterRequest.getType()).map(Type::getType).orElse(null);
        var isBefore = Optional.ofNullable(movieFilterRequest.getIsBefore()).orElse(true);

        if (isBefore) {
            return movieRepository.findAllByTitleAndTypeAndCreatedAtGreaterThanEqualAndStatus(addPercentToString(movieFilterRequest.getTitle()), typeString,
                    parseStringDate(movieFilterRequest.getCreatedAt(), movieFilterRequest.getIsBefore()), movieFilterRequest.getStatus(),
                    new OffsetBasedPageRequest(movieFilterRequest.getOffset(), movieFilterRequest.getLimit(), buildSort(movieFilterRequest)));
        } else {
            return movieRepository.findAllByTitleAndTypeAndCreatedAtLessThanEqualAndStatus(addPercentToString(movieFilterRequest.getTitle()), typeString,
                    parseStringDate(movieFilterRequest.getCreatedAt(), movieFilterRequest.getIsBefore()), movieFilterRequest.getStatus(),
                    new OffsetBasedPageRequest(movieFilterRequest.getOffset(), movieFilterRequest.getLimit(), buildSort(movieFilterRequest)));
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
                .type(Type.fromString(movieEntity.getType()))
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
