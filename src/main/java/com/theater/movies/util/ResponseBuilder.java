package com.theater.movies.util;

import com.theater.movies.model.Response;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.time.OffsetDateTime;

@UtilityClass
public class ResponseBuilder {

    private static final String SUCCESS = "Success";

    public static Response buildResponse(Object responseBody) {
        return Response.builder()
                .data(responseBody)
                .message(SUCCESS)
                .timestamp(OffsetDateTime.now())
                .status(HttpStatus.OK)
                .build();
    }

    public static String buildServerUri(HttpServletRequest request, Long offset, Long limit) {
        return ServletUriComponentsBuilder.fromRequest(request)
                .scheme(null)
                .port(null)
                .host(null)
                .replaceQueryParam("offset",
                        offset)
                .replaceQueryParam("limit",
                        limit)
                .build().toUriString();
    }
}
