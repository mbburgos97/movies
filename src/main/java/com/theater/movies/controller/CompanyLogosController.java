package com.theater.movies.controller;

import com.theater.movies.enums.Status;
import com.theater.movies.model.CommonResponse;
import com.theater.movies.model.CompanyLogo;
import com.theater.movies.model.PageableRequest;
import com.theater.movies.service.CompanyLogoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class CompanyLogosController {

    private final CompanyLogoService companyLogoService;

    @PostMapping("/logo")
    public CommonResponse createCompanyLogo(@RequestParam("website_link") String websiteLink,
                                            @RequestParam("image") MultipartFile image,
                                            HttpServletRequest request) {
        return companyLogoService.createCompanyLogo(CompanyLogo.builder()
                .image(image)
                .websiteLink(websiteLink)
                .build(), request);
    }

    @GetMapping("/logo/{id}")
    public CommonResponse getCompanyLogo(@PathVariable("id") Long id) {
        return companyLogoService.getCompanyLogo(id);
    }

    @DeleteMapping("/logo/{id}")
    public CommonResponse deleteCompanyLogo(@PathVariable Long id) {
        return companyLogoService.deleteCompanyLogo(id);
    }

    @GetMapping("/logos")
    public CommonResponse getCompanyLogos(@RequestParam(value = "offset", required = false) Long offset,
                                     @RequestParam(value = "limit", required = false) Integer limit,
                                     HttpServletRequest request) {
        return companyLogoService.getCompanyLogos(PageableRequest.builder()
                .limit(limit)
                .offset(offset)
                .build(), request);
    }

    @PutMapping(value = "/logo/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE} )
    public CommonResponse updateArtist(@PathVariable("id") Long id,
                                       @RequestParam(value = "website_link", required = false) String websiteLink,
                                       @RequestParam(value = "image", required = false) MultipartFile image,
                                       HttpServletRequest request) {
        return companyLogoService.updateCompanyLogo(CompanyLogo.builder()
                .id(id)
                .image(image)
                .websiteLink(websiteLink)
                .build(), request);
    }

    @PutMapping("/logo/{id}/status")
    public CommonResponse updateArtistStatus(@PathVariable Long id, @RequestParam("status") Integer status,
                                             HttpServletRequest request) {

        return companyLogoService.updateCompanyLogoStatus(id, Status.fromInteger(status), request);
    }
}
