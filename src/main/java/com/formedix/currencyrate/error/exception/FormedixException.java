package com.formedix.currencyrate.error.exception;

import lombok.Getter;

import java.util.UUID;

public class FormedixException extends RuntimeException {
    @Getter
    private final String uuid;

    public FormedixException(String message) {
        super(message);
        this.uuid = UUID.randomUUID().toString();
    }

    public FormedixException(String message, Throwable e) {
        super(message, e);
        this.uuid = UUID.randomUUID().toString();
    }
}
