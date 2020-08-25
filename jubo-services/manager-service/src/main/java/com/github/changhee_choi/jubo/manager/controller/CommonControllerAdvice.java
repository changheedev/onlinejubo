package com.github.changhee_choi.jubo.manager.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.persistence.EntityNotFoundException;
import javax.validation.ValidationException;
import java.nio.file.AccessDeniedException;

/**
 * @author Changhee Choi
 * @since 21/07/2020
 */
@Slf4j
@RestControllerAdvice
public class CommonControllerAdvice {

    @ExceptionHandler(value = {ValidationException.class})
    public ResponseEntity validationExceptionHandler(Exception e) {
        log.debug(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public ResponseEntity entityNotFoundExceptionHandler(Exception e) {
        log.debug(e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity accessDeniedExceptionHandler(Exception e) {
        log.debug(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}
