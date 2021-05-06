package com.theater.movies.exception;

public class UserInactiveException extends RuntimeException {
    public UserInactiveException (String message) {
        super(message);
    }
}
