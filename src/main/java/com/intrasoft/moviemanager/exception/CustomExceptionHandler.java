package com.intrasoft.moviemanager.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles all validation exception to a model and
 * present them at the response of the user
 */
@ControllerAdvice
@Slf4j
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles Exceptions that produced by validation Errors in DTO models
     *
     * @param ex
     * @param headers
     * @param status
     * @param request
     * @return errors in form
     * {
     * fieldError : Error message
     * }
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers, HttpStatus status,
            WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        //Loop through errors to create an Object for the response entity
        ex.getBindingResult().getAllErrors().forEach((error) -> {

            String fieldName = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            errors.put(fieldName, message);
        });

        // Log the errors at the console
        log.error("Validation Error: " + errors);

        // Return a new response entity With the error hash
        return new ResponseEntity<Object>(errors, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles error that produced when a resource that asked is not available
     *
     * @param noSuchFileException
     * @param webRequest
     * @return
     */
    @ExceptionHandler(value = {NoSuchFileException.class})
    protected ResponseEntity<Object> handleIOException(
            NoSuchFileException noSuchFileException,
            WebRequest webRequest
    ) {
        // We get the error message that occured inside the controller and print it at the response
        String bodyOfResponse = noSuchFileException.getMessage();

        // Log the errors at the console
        log.error("Item not found error: " + bodyOfResponse);

        return new ResponseEntity<Object>(bodyOfResponse, HttpStatus.NOT_FOUND);
    }
}
