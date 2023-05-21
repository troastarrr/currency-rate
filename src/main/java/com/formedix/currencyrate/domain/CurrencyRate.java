package com.formedix.currencyrate.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class CurrencyRate {
    private LocalDate date;
    private Map<String, BigDecimal> currencies;
}
