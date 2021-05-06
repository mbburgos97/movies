package com.theater.movies.util;

import com.theater.movies.exception.BadArgumentException;
import com.theater.movies.model.MovieFilterRequest;
import com.theater.movies.model.PageableRequest;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

import static com.theater.movies.util.ResponseBuilder.buildServerUri;

@UtilityClass
public class PageRequestUtil {

    public static boolean checkPageableRequestIfValid(PageableRequest pageableRequest) {
        if (pageableRequest.getOffset() < 0  || pageableRequest.getLimit() < 0) {
            throw new BadArgumentException("Cannot assign negative value to offset or limit.");
        }
        return true;
    }

    public static Sort buildSort(MovieFilterRequest pageableRequest) {
        var optionalDirection = Optional.ofNullable(pageableRequest.getIsAscending());

        if (optionalDirection.isPresent()) {
            if (optionalDirection.get()) {
                return Sort.by(Sort.Direction.ASC, pageableRequest.getSortBy());
            }
            return Sort.by(Sort.Direction.DESC, pageableRequest.getSortBy());
        }
        return Sort.unsorted();
    }

    public static String buildPreviousUri(HttpServletRequest request, Page pagedEntities, PageableRequest pageableRequest) {
        var currentOffset = pageableRequest.getOffset();
        if (pagedEntities.hasPrevious()) {
            return buildServerUri(request, pagedEntities.previousPageable().getOffset(), Long.valueOf(pageableRequest.getLimit()));
        } else if (currentOffset != 0) {
            return buildServerUri(request, 0L, currentOffset);
        }
        return null;
    }

    public static String buildNextUri(HttpServletRequest request, Page pagedEntities, Integer limit) {
        if (pagedEntities.hasNext()) {
            return buildServerUri(request, pagedEntities.nextPageable().getOffset(), Long.valueOf(limit));
        }
        return null;
    }
}
