package com.theater.movies.exception;

public class BadArgumentException extends RuntimeException {
    public BadArgumentException (String message) {
        super(message);
    }
}
