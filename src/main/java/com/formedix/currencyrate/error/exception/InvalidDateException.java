package com.formedix.currencyrate.error.exception;

import com.formedix.currencyrate.error.ErrorCode;

public class InvalidDateException extends FormedixException {
    public InvalidDateException(String message) {
        super(message, ErrorCode.INVALID_DATE_RANGE_ERROR);
    }
}
