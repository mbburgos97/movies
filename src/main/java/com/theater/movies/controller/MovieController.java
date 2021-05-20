package com.theater.movies.controller;

import com.theater.movies.enums.Status;
import com.theater.movies.enums.Type;
import com.theater.movies.model.CommonResponse;
import com.theater.movies.model.Movie;
import com.theater.movies.model.MovieFilterRequest;
import com.theater.movies.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class MovieController {

    private final MovieService movieService;

    @GetMapping("/movies")
    public CommonResponse getMovies(@RequestParam(value = "offset", required = false) Long offset,
                                    @RequestParam(value = "limit", required = false) Integer limit,
                                    @RequestParam(value = "status", required = false) Integer status,
                                    @RequestParam(value = "title", required = false) String title,
                                    @RequestParam(value = "type", required = false) String type,
                                    @RequestParam(value = "created_at", required = false) String createdAt,
                                    @RequestParam(value = "sort_by", required = false) String sortBy,
                                    @RequestParam(value = "is_ascending", required = false) Boolean isAscending,
                                    @RequestParam(value = "is_before", required = false) Boolean isBefore,
                                    HttpServletRequest request) {
        return movieService.getMovies(MovieFilterRequest.builder()
                .status(Status.fromInteger(status))
                .title(title)
                .type(Type.fromString(type))
                .isAscending(isAscending)
                .isBefore(isBefore)
                .sortBy(sortBy)
                .limit(limit)
                .offset(offset)
                .createdAt(createdAt)
                .build(), request);
    }

    @GetMapping("/movie/{id}")
    public CommonResponse getMovie(@PathVariable Long id) {
        return movieService.getMovie(id);
    }

    @PostMapping(value = "/movie", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE} )
    public CommonResponse createMovie(@RequestParam("title") String title,
                                      @RequestParam("video") MultipartFile video,
                                      @RequestParam("image") MultipartFile image,
                                      @RequestParam("content") String content,
                                      @RequestParam("year") Integer year,
                                      @RequestParam("director") String director,
                                      @RequestParam("actors") String actors,
                                      @RequestParam("type") String type,
                                      @RequestParam("confidential") Boolean confidential,
                                      @RequestParam("awards") List<String> awards,
                                      @RequestParam("imdb_score") BigDecimal imdbScore,
                                      @RequestParam("investment") Long investment,
                                      @RequestParam("return") Long returnValue,
                                      @RequestParam("payback") Integer payback,
                                      HttpServletRequest request) {

        return movieService.createMovie(Movie.builder()
                .title(title)
                .video(video)
                .image(image)
                .content(content)
                .year(year)
                .type(Type.fromString(type))
                .confidential(confidential)
                .awards(awards)
                .director(director)
                .actors(actors)
                .imdbScore(imdbScore)
                .investment(investment)
                .returnValue(returnValue)
                .payback(payback)
                .build(), request);
    }

    @DeleteMapping("/movie/{id}")
    public CommonResponse deleteMovie(@PathVariable Long id) {
        return movieService.deleteMovie(id);
    }

    @PutMapping("/movie/{id}")
    public CommonResponse updateMovie(@PathVariable Long id, @RequestParam(value = "title", required = false) String title,
                                      @RequestParam(value = "video", required = false) MultipartFile video,
                                      @RequestParam(value = "image", required = false) MultipartFile image,
                                      @RequestParam(value = "content", required = false) String content,
                                      @RequestParam(value = "year", required = false) Integer year,
                                      @RequestParam(value = "director", required = false) String director,
                                      @RequestParam(value = "actors", required = false) String actors,
                                      @RequestParam(value = "type", required = false) String type,
                                      @RequestParam(value = "confidential", required = false) Boolean confidential,
                                      @RequestParam(value = "awards", required = false) List<String> awards,
                                      @RequestParam(value = "imdb_score", required = false) BigDecimal imdbScore,
                                      @RequestParam(value = "investment", required = false) Long investment,
                                      @RequestParam(value = "return", required = false) Long returnValue,
                                      @RequestParam(value = "payback", required = false) Integer payback,
                                      HttpServletRequest request) {

        return movieService.updateMovie(Movie.builder()
                .id(id)
                .title(title)
                .video(video)
                .image(image)
                .content(content)
                .year(year)
                .type(Type.fromString(type))
                .confidential(confidential)
                .awards(awards)
                .director(director)
                .actors(actors)
                .imdbScore(imdbScore)
                .investment(investment)
                .returnValue(returnValue)
                .payback(payback)
                .build(), request);
    }

    @PutMapping("/movie/{id}/status")
    public CommonResponse updateMovieStatus(@PathVariable Long id, @RequestParam("status") Integer status,
                                      HttpServletRequest request) {

        return movieService.updateMovieStatus(id, Status.fromInteger(status), request);
    }
}
