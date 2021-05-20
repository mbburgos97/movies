package com.theater.movies.service;

import com.theater.movies.entity.CompanyLogoEntity;
import com.theater.movies.entity.OffsetBasedPageRequest;
import com.theater.movies.enums.Status;
import com.theater.movies.exception.LogoNotFoundException;
import com.theater.movies.model.*;
import com.theater.movies.repository.CompanyLogosRepository;
import com.theater.movies.util.FileUtil;
import com.theater.movies.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.theater.movies.enums.FileType.IMAGE;
import static com.theater.movies.enums.Status.ACTIVE;
import static com.theater.movies.util.FileUtil.saveFile;
import static com.theater.movies.util.PageRequestUtil.*;

@Service
@RequiredArgsConstructor
public class CompanyLogoService {

    private final CompanyLogosRepository companyLogosRepository;

    public Response createCompanyLogo(CompanyLogo companyLogo, HttpServletRequest request) {
        return ResponseBuilder.buildResponse(toModel(companyLogosRepository.save(CompanyLogoEntity.builder()
                .imageUrl(saveFile(companyLogo.getImage()))
                .websiteLink(companyLogo.getWebsiteLink())
                .status(ACTIVE)
                .createdAt(OffsetDateTime.now())
                .createdBy(request.getUserPrincipal().getName())
                .build())));
    }

    public Response getCompanyLogo(Long id) {
        return ResponseBuilder.buildResponse(toModel(getCompanyLogoById(id)));
    }

    public Response getAllActiveCompanyLogos(PageableRequest pageableRequest, HttpServletRequest request) {
        checkPageableRequestIfValid(pageableRequest);

        var companyLogos = companyLogosRepository.findAllByStatus(ACTIVE,
                new OffsetBasedPageRequest(pageableRequest.getOffset(), pageableRequest.getLimit()));

        return ResponseBuilder.buildResponse(ListResponse.builder()
                .results(StreamSupport.stream(companyLogos
                        .spliterator(), false)
                        .map(this::toModel)
                        .collect(Collectors.toList()))
                .count(companyLogos.getTotalElements())
                .next(buildNextUri(request, companyLogos, pageableRequest.getLimit()))
                .previous(buildPreviousUri(request, companyLogos, pageableRequest))
                .build());
    }

    public Response getCompanyLogos(PageableRequest pageableRequest, HttpServletRequest request) {
        checkPageableRequestIfValid(pageableRequest);

        var companyLogos = companyLogosRepository.findAll(
                new OffsetBasedPageRequest(pageableRequest.getOffset(), pageableRequest.getLimit()));

        return ResponseBuilder.buildResponse(ListResponse.builder()
                .results(StreamSupport.stream(companyLogos
                        .spliterator(), false)
                        .map(this::toModel)
                        .collect(Collectors.toList()))
                .count(companyLogos.getTotalElements())
                .next(buildNextUri(request, companyLogos, pageableRequest.getLimit()))
                .previous(buildPreviousUri(request, companyLogos, pageableRequest))
                .build());
    }

    public CommonResponse deleteCompanyLogo(Long id) {
        var companyLogoEntity = getCompanyLogoById(id);

        companyLogosRepository.deleteById(companyLogoEntity.getId());
        return CommonResponse.builder()
                .timestamp(OffsetDateTime.now())
                .message("Successfully deleted company logo.")
                .status(HttpStatus.OK)
                .build();
    }

    public Response updateCompanyLogo(CompanyLogo companyLogo, HttpServletRequest request) {
        var companyLogosEntity = getCompanyLogoById(companyLogo.getId());

        Optional.ofNullable(companyLogo.getWebsiteLink())
                .ifPresent(companyLogosEntity::setWebsiteLink);
        Optional.ofNullable(companyLogo.getImage())
                .ifPresent(image -> {
                    FileUtil.deleteFile(companyLogosEntity.getImageUrl());
                    companyLogosEntity.setImageUrl(saveFile(image));
                });

        companyLogosEntity.setUpdatedAt(OffsetDateTime.now());
        companyLogosEntity.setUpdatedBy(request.getUserPrincipal().getName());

        return ResponseBuilder.buildResponse(toModel(companyLogosRepository.save(companyLogosEntity)));
    }

    public Response updateCompanyLogoStatus(Long id, Status status, HttpServletRequest request) {
        var companyLogoEntity = getCompanyLogoById(id);

        Optional.of(status)
                .ifPresent(companyLogoEntity::setStatus);
        companyLogoEntity.setUpdatedAt(OffsetDateTime.now());
        companyLogoEntity.setUpdatedBy(request.getUserPrincipal().getName());

        return ResponseBuilder.buildResponse(toModel(companyLogosRepository.save(companyLogoEntity)));
    }

    private CompanyLogoEntity getCompanyLogoById(Long id) {
        return companyLogosRepository.findById(id)
                .orElseThrow(() -> new LogoNotFoundException("Logo with id " + id + " not found."));
    }

    private CompanyLogo toModel(CompanyLogoEntity companyLogoEntity) {
        return CompanyLogo.builder()
                .id(companyLogoEntity.getId())
                .websiteLink(companyLogoEntity.getWebsiteLink())
                .imageUrl(FileUtil.getFilePath(companyLogoEntity.getImageUrl(), IMAGE))
                .status(companyLogoEntity.getStatus())
                .updatedBy(companyLogoEntity.getUpdatedBy())
                .updatedAt(companyLogoEntity.getUpdatedAt())
                .createdAt(companyLogoEntity.getCreatedAt())
                .createdBy(companyLogoEntity.getCreatedBy())
                .build();
    }
}
