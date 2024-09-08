package com.assignment2.queuingmechanism.exceptions;

import com.assignment2.queuingmechanism.constants.Constants;
import com.assignment2.queuingmechanism.responses.CustomGenericErrorResponse;
import com.assignment2.queuingmechanism.responses.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.validation.ConstraintViolationException;
import java.net.BindException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex) {
        Map<String, Object> map = new HashMap<>();
        map.put(Constants.ERROR, ex.getClass());
        List<String> errors = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.toList());
        map.put(Constants.CONSTRAINS_VIOLATED, errors);
        return new ResponseEntity<>(map, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation2(SQLIntegrityConstraintViolationException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getClass().toString(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({HttpMessageNotReadableException.class})
    public ResponseEntity<Object> httpMessageNotReadableException(HttpMessageNotReadableException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getClass().toString(), e.getCause().toString()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegal(IllegalArgumentException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getClass().toString(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NoSuchElementException.class})
    public ResponseEntity<Object> handleNoElementException(NoSuchElementException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getClass().toString(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex.getClass().toString(), ex.getName() + Constants.SHOULD_BE_OF_TYPE + Objects.requireNonNull(ex.getRequiredType()).getName()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({HttpRequestMethodNotSupportedException.class})
    public ResponseEntity<Object> handleException1(HttpRequestMethodNotSupportedException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getClass().toString(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({NullPointerException.class})
    public ResponseEntity<Object> handleException12(NullPointerException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getClass().toString(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(CustomGenericException.class)
    public ResponseEntity<Object> handleBadRequest(CustomGenericException e) {
        return new ResponseEntity<>(new CustomGenericErrorResponse(e.getErrorCode(), e.getMessage()), Objects.requireNonNull(HttpStatus.resolve(Integer.parseInt(e.getErrorCode()))));
    }

    @ExceptionHandler({BindException.class})
    public ResponseEntity<Object> bindException(BindException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getClass().toString(), e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Object> methodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<String> listOfErrors = new ArrayList<>();
        e.getAllErrors().forEach(i -> listOfErrors.add(i.getDefaultMessage()));
        return new ResponseEntity<>(new ErrorResponse(e.getClass().getName(), listOfErrors), HttpStatus.BAD_REQUEST);
    }
}