package com.example.CreateOrder.exceptions;


import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Locale;

@ControllerAdvice
public class OrderExceptionHandler {

    @Autowired
    private MessageSource messageSource;

    private String getLocalizedMessage(String messageKey, HttpServletRequest request) {
        Locale locale = request.getLocale();
        return messageSource.getMessage(messageKey, null, locale);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleInvalidArguments(MethodArgumentNotValidException ex) {
        return new ResponseEntity<>("Invalid input parameters: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity
                .badRequest()
                .body(ex.getMessage());
    }
    
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }


   
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<String> handlePathNotFound(NoHandlerFoundException ex) {
        return new ResponseEntity<>("Path not found: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleServerError(Exception ex) {
        return new ResponseEntity<>("Server error: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}