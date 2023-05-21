package com.formedix.currencyrate.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class ConvertCurrencyDto {
    private String sourceCurrency;
    private String targetCurrency;
    private LocalDate date;
    private BigDecimal convertedAmount;
}
