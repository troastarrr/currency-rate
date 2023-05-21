package com.formedix.currencyrate.error.handler;

import com.formedix.currencyrate.error.domain.Error;
import com.formedix.currencyrate.error.domain.ErrorCode;
import com.formedix.currencyrate.error.exception.CsvFileException;
import com.formedix.currencyrate.error.exception.CsvParsingException;
import com.formedix.currencyrate.error.exception.CurrencyRateNotFoundException;
import com.formedix.currencyrate.error.exception.InvalidDateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

/**
 * Global exception handler for handling currency rate related exceptions.
 */
@ControllerAdvice
@Slf4j
public class CurrencyRateExceptionHandler {

    /**
     * Handles the exception when a currency rate is not found.
     *
     * @param e the exception indicating currency rate not found
     *
     * @return the response entity with error details
     */
    @ExceptionHandler
    public ResponseEntity<Error> handleCurrencyRateNotFoundException(CurrencyRateNotFoundException e) {
        log.warn("Currency rate not found error: `{}`", e.getMessage());
        Error response = new Error()
                .errorCode(e.getErrorCode())
                .errorMessages(List.of(e.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles the exception when validation errors occur during method argument validation.
     *
     * @param e the exception indicating validation errors
     *
     * @return the response entity with error details
     */
    @ExceptionHandler
    public ResponseEntity<Error> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        log.warn("Validation error occurred: `{}`", e.getMessage());
        Error response = new Error()
                .errorCode(ErrorCode.VALIDATION_ERROR)
                .errorMessages(List.of(e.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the exception when handling csv file
     *
     * @param e the exception indicating validation errors
     *
     * @return the response entity with error details
     */
    @ExceptionHandler
    public ResponseEntity<Error> handleCsvFileException(CsvFileException e) {
        log.warn("CSV file error occurred: `{}`", e.getMessage());
        Error response = new Error()
                .errorCode(e.getErrorCode())
                .errorMessages(List.of(e.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the exception when parsing csv file
     *
     * @param e the exception indicating validation errors
     *
     * @return the response entity with error details
     */
    @ExceptionHandler
    public ResponseEntity<Error> handleCsvParsingException(CsvParsingException e) {
        log.warn("CSV file error occurred: `{}`", e.getMessage());
        Error response = new Error()
                .errorCode(e.getErrorCode())
                .errorMessages(List.of(e.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles the exception when an invalid date range is provided.
     *
     * @param e the exception indicating an invalid date range
     *
     * @return the response entity with error details
     */
    @ExceptionHandler
    public ResponseEntity<Error> handleInvalidDateException(InvalidDateException e) {
        log.warn("Invalid date range error: `{}`", e.getMessage());
        Error response = new Error()
                .errorCode(e.getErrorCode())
                .errorMessages(List.of(e.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles generic exceptions that occur during currency rate processing.
     *
     * @param e the generic exception
     *
     * @return the response entity with error details
     */
    @ExceptionHandler
    public ResponseEntity<Error> handleGenericException(Exception e) {
        log.warn("Internal server error occurred: `{}`", e.getMessage());
        Error response = new Error()
                .errorCode(ErrorCode.SERVER_ERROR)
                .errorMessages(List.of(e.getMessage()));
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}