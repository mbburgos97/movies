package com.theater.movies.controller;

import com.theater.movies.enums.Status;
import com.theater.movies.enums.Type;
import com.theater.movies.model.ArtistFilterRequest;
import com.theater.movies.model.CommonResponse;
import com.theater.movies.model.MovieFilterRequest;
import com.theater.movies.service.ArtistService;
import com.theater.movies.service.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/member")
@RequiredArgsConstructor
public class PublicApiController {

    private final MovieService movieService;

    private final ArtistService artistService;

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

    @GetMapping("/artists")
    public CommonResponse getArtists(@RequestParam(value = "offset", required = false) Long offset,
                                     @RequestParam(value = "limit", required = false) Integer limit,
                                     @RequestParam(value = "status", required = false) Integer status,
                                     @RequestParam(value = "name", required = false) String name,
                                     @RequestParam(value = "created_at", required = false) String createdAt,
                                     @RequestParam(value = "is_before", required = false) Boolean isBefore,
                                     HttpServletRequest request) {
        return artistService.getArtists(ArtistFilterRequest.builder()
                .limit(limit)
                .offset(offset)
                .status(Status.fromInteger(status))
                .name(name)
                .createdAt(createdAt)
                .isBefore(isBefore)
                .build(), request);
    }

    @GetMapping("/artist/{id}")
    public CommonResponse getArtist(@PathVariable("id") Long id) {
        return artistService.getArtist(id);
    }

}
