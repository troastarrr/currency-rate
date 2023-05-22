package com.formedix.currencyrate.error.exception;

import com.formedix.currencyrate.error.ErrorCode;
import lombok.Getter;

import java.util.UUID;


public class FormedixException extends RuntimeException {
    @Getter
    private final String uuid;

    @Getter
    private final ErrorCode errorCode;

    public FormedixException(String message, ErrorCode errorCode) {
        super(message);
        this.uuid = UUID.randomUUID().toString();
        this.errorCode = errorCode;
    }

    public FormedixException(String message, Throwable e, ErrorCode errorCode) {
        super(message, e);
        this.uuid = UUID.randomUUID().toString();
        this.errorCode = errorCode;
    }
}
