package com.formedix.currencyrate.error.exception;

import com.formedix.currencyrate.error.ErrorCode;

public class CsvFileException extends FormedixException {
    public CsvFileException(String message, ErrorCode errorCode, Throwable e) {
        super(message, e, errorCode);
    }

    public CsvFileException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
