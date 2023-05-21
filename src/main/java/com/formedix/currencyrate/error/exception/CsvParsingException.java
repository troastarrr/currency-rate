package com.formedix.currencyrate.error.exception;

public class CsvParsingException extends FormedixException {
    public CsvParsingException(String message) {
        super(message);
    }

    public CsvParsingException(String message, Throwable e) {
        super(message, e);
    }
}
