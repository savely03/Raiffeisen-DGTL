package com.savely.socksapp.exception;

import org.springframework.http.HttpStatus;

public class SockNotFoundException extends BaseSockException {
    public SockNotFoundException() {
        super(HttpStatus.NOT_FOUND);
    }

    @Override
    public String getMessage() {
        return "Socks не найдены";
    }
}
