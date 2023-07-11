package com.savely.socksapp.handler;

import com.savely.socksapp.dto.ResponseError;
import com.savely.socksapp.exception.BaseSockException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GeneralHandler {

    @ExceptionHandler(BaseSockException.class)
    public ResponseEntity<ResponseError> handleBaseSockException(BaseSockException e) {
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ResponseError(e.getMessage()));
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<ResponseError>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getFieldErrors()
                .stream()
                .map(error -> new ResponseError("Field - " + error.getField() + ": " + error.getDefaultMessage()))
                .collect(Collectors.toList()));
    }
}
