package com.formedix.currencyrate.error.exception;

import com.formedix.currencyrate.error.domain.ErrorCode;

public class CurrencyRateNotFoundException extends FormedixException {
    public CurrencyRateNotFoundException(String message) {
        super(message, ErrorCode.CURRENCY_RATE_NOT_FOUND);
    }
}
