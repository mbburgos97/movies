package com.theater.movies.util;

import com.theater.movies.exception.BadArgumentException;
import com.theater.movies.model.MovieListRequest;
import com.theater.movies.model.PageableRequest;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;

import java.util.Optional;

@UtilityClass
public class PageRequestUtil {

    public static boolean checkPageableRequestIfValid(PageableRequest pageableRequest) {
        if (pageableRequest.getOffset() < 0  || pageableRequest.getLimit() < 0) {
            throw new BadArgumentException("Cannot assign negative value to offset or limit.");
        }
        return true;
    }

    public static Sort buildSort(MovieListRequest pageableRequest) {
        var optionalDirection = Optional.ofNullable(pageableRequest.getIsAscending());

        if (optionalDirection.isPresent()) {
            if (optionalDirection.get()) {
                return Sort.by(Sort.Direction.ASC, pageableRequest.getSortBy());
            }
            return Sort.by(Sort.Direction.DESC, pageableRequest.getSortBy());
        }
        return Sort.unsorted();
    }
}
