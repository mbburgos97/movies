package com.theater.movies.controller;

import com.theater.movies.enums.Status;
import com.theater.movies.model.Artist;
import com.theater.movies.model.ArtistFilterRequest;
import com.theater.movies.model.CommonResponse;
import com.theater.movies.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

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

    @PostMapping(value = "/artist", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE} )
    public CommonResponse createArtist(@RequestParam("name") String name,
                                       @RequestParam("image") MultipartFile image,
                                       @RequestParam("description") String description,
                                       HttpServletRequest request) {
        return artistService.createArtist(Artist.builder()
                .name(name)
                .image(image)
                .description(description)
                .build(), request);
    }

    @PutMapping(value = "/artist/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE} )
    public CommonResponse updateArtist(@PathVariable("id") Long id,
                                       @RequestParam(value = "name", required = false) String name,
                                       @RequestParam(value = "image", required = false) MultipartFile image,
                                       @RequestParam(value = "description", required = false) String description,
                                       HttpServletRequest request) {
        return artistService.updateArtist(Artist.builder()
                .id(id)
                .name(name)
                .image(image)
                .description(description)
                .build(), request);
    }

    @PutMapping("/artist/{id}/status")
    public CommonResponse updateArtistStatus(@PathVariable Long id, @RequestParam("status") Integer status,
                                            HttpServletRequest request) {

        return artistService.updateArtistStatus(id, Status.fromInteger(status), request);
    }

    @DeleteMapping("/artist/{id}")
    public CommonResponse deleteArtist(@PathVariable Long id) {
        return artistService.deleteArtist(id);
    }

}
