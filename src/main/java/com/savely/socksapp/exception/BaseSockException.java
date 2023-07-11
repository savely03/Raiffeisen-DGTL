package com.savely.socksapp.exception;

import org.springframework.http.HttpStatus;

public abstract class BaseSockException extends RuntimeException {

    private final HttpStatus httpStatus;

    public BaseSockException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
