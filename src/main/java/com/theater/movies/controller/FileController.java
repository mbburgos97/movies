package com.theater.movies.controller;

import com.theater.movies.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
public class FileController {

    private final FileService fileService;

    @GetMapping("/image/{filename:.+}")
    @ResponseBody
    public ResponseEntity<byte[]> getImage(@PathVariable String filename) {

        var fileContent = fileService.getImage(filename);
        var headers = new HttpHeaders();

        headers.setContentType(MediaType.IMAGE_PNG);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=" + filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(fileContent, headers, HttpStatus.OK);
    }

    @GetMapping("/video/{filename:.+}")
    public Mono<ResponseEntity<byte[]>> streamVideo(@RequestHeader(value = "Range", required = false) String httpRangeList,
                                                    @PathVariable String filename) {
        return Mono.just(fileService.prepareContent(filename, httpRangeList));
    }
}
