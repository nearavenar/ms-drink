package com.aravena.msrepouser.handler;


import feign.FeignException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.thymeleaf.exceptions.TemplateInputException;

import java.time.LocalDateTime;

@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler{
    @ExceptionHandler(FeignException.NotFound.class)
    public ResponseEntity<Error> handlerFeignNotFoundException(FeignException.NotFound ex, HttpServletRequest request) {
        Error error = new Error();
        error.setStatus(HttpStatus.NOT_FOUND.value());
        error.setError( "Not Found");
        error.setMessage("User not found");
        error.setPath(request.getRequestURI());
        error.setTimestamp(LocalDateTime.now());
        log.error("FeignException:", ex);
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TemplateInputException.class)
    public ResponseEntity<Error> handlerFeignTemplateInputException(TemplateInputException ex, HttpServletRequest request) {
        Error error = new Error();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setError(ex.getTemplateName());
        error.setMessage(ex.getMessage());
        error.setPath(request.getRequestURI());
        error.setTimestamp(LocalDateTime.now());
        log.error("TemplateInputException:", ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Error> handlerGeneralException(Exception ex, HttpServletRequest request) {
        Error error = new Error();
        error.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.setError(ex.getMessage());
        error.setMessage("Internal Server Error");
        error.setPath(request.getRequestURI());
        error.setTimestamp(LocalDateTime.now());
        log.error("Error:", ex);
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
