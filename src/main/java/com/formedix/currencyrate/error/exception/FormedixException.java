package com.formedix.currencyrate.error.exception;

import com.formedix.currencyrate.error.domain.ErrorCode;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;


public class FormedixException extends RuntimeException {
    @Getter
    private final String uuid;

    @Getter
    @Setter
    private ErrorCode errorCode;

    public FormedixException(String message) {
        super(message);
        this.uuid = UUID.randomUUID().toString();
    }

    public FormedixException(String message, Throwable e) {
        super(message, e);
        this.uuid = UUID.randomUUID().toString();
    }
}
