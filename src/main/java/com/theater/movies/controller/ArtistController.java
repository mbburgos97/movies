package com.theater.movies.controller;

import com.theater.movies.enums.Status;
import com.theater.movies.model.Artist;
import com.theater.movies.model.CommonResponse;
import com.theater.movies.service.ArtistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ArtistController {

    private final ArtistService artistService;

    @GetMapping("/artists")
    public CommonResponse getArtists() {
        return artistService.getArtists();
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
                                       @RequestParam("name") String name,
                                       @RequestParam("image") MultipartFile image,
                                       @RequestParam("description") String description,
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
