package com.theater.movies.service;

import com.theater.movies.entity.ArtistEntity;
import com.theater.movies.enums.Status;
import com.theater.movies.exception.ArtistNotFoundException;
import com.theater.movies.model.Artist;
import com.theater.movies.model.CommonResponse;
import com.theater.movies.model.Response;
import com.theater.movies.repository.ArtistRepository;
import com.theater.movies.util.ResponseBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.theater.movies.util.FileUtil.saveFile;

@Service
@RequiredArgsConstructor
public class ArtistService {

    private final ArtistRepository artistRepository;

    public Response getArtists() {
        return ResponseBuilder.buildResponse(StreamSupport.stream(artistRepository.findAll().spliterator(), false)
                .map(this::toModel)
                .collect(Collectors.toList()));
    }

    public Response getArtist(Long id) {
        return ResponseBuilder.buildResponse(toModel(getArtistById(id)));
    }

    public Response createArtist(Artist artist, HttpServletRequest request) {
        return ResponseBuilder.buildResponse(toModel(artistRepository.save(ArtistEntity.builder()
                .imageUrl(saveFile(artist.getImage()))
                .description(artist.getDescription())
                .name(artist.getName())
                .status(Status.ACTIVE)
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
