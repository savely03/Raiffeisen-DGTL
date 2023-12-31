package com.savely.socksapp.exception;

import org.springframework.http.HttpStatus;

public class IncorrectQuantityException extends BaseException {
    public IncorrectQuantityException() {
        super(HttpStatus.BAD_REQUEST);
    }

    @Override
    public String getMessage() {
        return "Переданное количество больше существующего";
    }
}
