package com.theater.movies.service;

import com.theater.movies.entity.ArtistEntity;
import com.theater.movies.entity.OffsetBasedPageRequest;
import com.theater.movies.enums.Status;
import com.theater.movies.exception.ArtistNotFoundException;
import com.theater.movies.model.*;
import com.theater.movies.repository.ArtistRepository;
import com.theater.movies.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.theater.movies.enums.Status.ACTIVE;
import static com.theater.movies.util.DateUtil.parseStringDate;
import static com.theater.movies.util.FileUtil.saveFile;
import static com.theater.movies.util.PageRequestUtil.*;
import static com.theater.movies.util.StringUtil.addPercentToString;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

    public Response getArtists(ArtistFilterRequest artistFilterRequest, HttpServletRequest request) {
        checkPageableRequestIfValid(artistFilterRequest);

        var pagedArtists = getPagedArtistEntity(artistFilterRequest);
        var totalElements = pagedArtists.getTotalElements();

        return ResponseBuilder.buildResponse(ListResponse.builder()
                .results(StreamSupport.stream(pagedArtists
                        .spliterator(), false)
                        .map(this::toModel)
                        .collect(Collectors.toList()))
                .count(totalElements)
                .next(buildNextUri(request, pagedArtists, artistFilterRequest.getLimit()))
                .previous(buildPreviousUri(request, pagedArtists, artistFilterRequest))
                .build());
    }

    public Response getArtistsWithFilter(ArtistFilterRequest artistFilterRequest, HttpServletRequest request) {
        var status = Optional.ofNullable(artistFilterRequest.getStatus()).orElse(ACTIVE);
        artistFilterRequest.setStatus(status);

        return getArtists(artistFilterRequest, request);
    }

    private Page<ArtistEntity> getPagedArtistEntity(ArtistFilterRequest artistFilterRequest) {
        var isBefore = Optional.ofNullable(artistFilterRequest.getIsBefore()).orElse(true);

        if (isBefore) {
            return artistRepository.findAllByNameAndCreatedAtGreaterThanEqualAndStatus(addPercentToString(artistFilterRequest.getName()),
                    parseStringDate(artistFilterRequest.getCreatedAt(), artistFilterRequest.getIsBefore()), artistFilterRequest.getStatus(),
                    new OffsetBasedPageRequest(artistFilterRequest.getOffset(), artistFilterRequest.getLimit()));
        } else {
            return artistRepository.findAllByNameAndCreatedAtLessThanEqualAndStatus(addPercentToString(artistFilterRequest.getName()),
                    parseStringDate(artistFilterRequest.getCreatedAt(), artistFilterRequest.getIsBefore()), artistFilterRequest.getStatus(),
                    new OffsetBasedPageRequest(artistFilterRequest.getOffset(), artistFilterRequest.getLimit()));
        }
    }

    public Response getArtist(Long id) {
        return ResponseBuilder.buildResponse(toModel(getArtistById(id)));
    }

    public Response createArtist(Artist artist, HttpServletRequest request) {
        return ResponseBuilder.buildResponse(toModel(artistRepository.save(ArtistEntity.builder()
                .imageUrl(saveFile(artist.getImage()))
                .description(artist.getDescription())
                .name(artist.getName())
                .status(ACTIVE)
                .createdAt(LocalDateTime.now())
                .createdBy(request.getUserPrincipal().getName())
                .build())));
    }

    public Response updateArtist(Artist artist, HttpServletRequest request) {
        var artistEntity = getArtistById(artist.getId());

        Optional.of(artist.getName())
                .ifPresent(artistEntity::setName);
        Optional.of(artist.getImage())
                .ifPresent(image -> artistEntity.setImageUrl(saveFile(image)));
        Optional.of(artist.getDescription())
                .ifPresent(artistEntity::setDescription);

        artistEntity.setUpdatedAt(LocalDateTime.now());
        artistEntity.setUpdatedBy(request.getUserPrincipal().getName());

        return ResponseBuilder.buildResponse(toModel(artistRepository.save(artistEntity)));
    }

    public Response updateArtistStatus(Long id, Status status, HttpServletRequest request) {
        var artistEntity = getArtistById(id);

        Optional.of(status)
                .ifPresent(artistEntity::setStatus);
        artistEntity.setUpdatedAt(LocalDateTime.now());
        artistEntity.setUpdatedBy(request.getUserPrincipal().getName());

        return ResponseBuilder.buildResponse(toModel(artistRepository.save(artistEntity)));
    }

    public CommonResponse deleteArtist(Long id) {
        var artistEntity = getArtistById(id);

        artistRepository.deleteById(artistEntity.getId());
        return CommonResponse.builder()
                .timestamp(LocalDateTime.now())
                .message("Successfully deleted artist.")
                .status(HttpStatus.OK)
                .build();
    }

    private ArtistEntity getArtistById(Long id) {
        return artistRepository.findById(id)
                .orElseThrow(() -> new ArtistNotFoundException("Artist with id " + id + " not found."));
    }

    private Artist toModel(ArtistEntity artistEntity) {
        return Artist.builder()
                .id(artistEntity.getId())
                .description(artistEntity.getDescription())
                .imageUrl(artistEntity.getImageUrl())
                .name(artistEntity.getName())
                .status(artistEntity.getStatus())
                .updatedBy(artistEntity.getUpdatedBy())
                .updatedAt(artistEntity.getUpdatedAt())
                .createdAt(artistEntity.getCreatedAt())
                .createdBy(artistEntity.getCreatedBy())
                .build();
    }
}
