package com.savely.socksapp.exception;

import org.springframework.http.HttpStatus;

public class IncorrectOperationException extends BaseSockException {
    public IncorrectOperationException() {
        super(HttpStatus.BAD_REQUEST);
    }

    @Override
    public String getMessage() {
        return "Данной операции не существует";
    }

}
