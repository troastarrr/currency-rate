package com.formedix.currencyrate.error.exception;

import com.formedix.currencyrate.error.ErrorCode;

public class CsvParsingException extends FormedixException {
    public CsvParsingException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public CsvParsingException(String message, Throwable e, ErrorCode errorCode) {
        super(message, e, errorCode);
    }
}
