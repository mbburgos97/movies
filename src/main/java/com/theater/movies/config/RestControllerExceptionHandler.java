package com.theater.movies.config;

import com.theater.movies.exception.ArtistNotFoundException;
import com.theater.movies.exception.BadArgumentException;
import com.theater.movies.exception.DuplicateUsernameException;
import com.theater.movies.exception.MovieNotFoundException;
import com.theater.movies.model.CommonResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;
import java.time.LocalDateTime;

@ControllerAdvice
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = {DuplicateUsernameException.class, MovieNotFoundException.class,
            UsernameNotFoundException.class, BadArgumentException.class, SQLIntegrityConstraintViolationException.class,
            ArtistNotFoundException.class})
    public CommonResponse handleDuplicate(Exception ex) {
        return CommonResponse.builder()
                .timestamp(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST)
                .message(ex.getMessage())
                .build();
    }
}
