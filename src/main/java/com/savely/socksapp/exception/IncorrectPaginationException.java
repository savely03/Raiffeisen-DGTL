package com.savely.socksapp.exception;

import org.springframework.http.HttpStatus;

public class IncorrectPaginationException extends BaseSockException {
    public IncorrectPaginationException() {
        super(HttpStatus.BAD_REQUEST);
    }

    @Override
    public String getMessage() {
        return "Страница и размер должны быть > 0";
    }
}
