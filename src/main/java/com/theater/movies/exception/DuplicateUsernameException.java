package com.theater.movies.exception;

public class DuplicateUsernameException extends RuntimeException {
    public DuplicateUsernameException (String message) {
        super(message);
    }
}