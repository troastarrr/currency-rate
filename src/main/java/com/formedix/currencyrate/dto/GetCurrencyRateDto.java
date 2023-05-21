package com.formedix.currencyrate.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class GetCurrencyRateDto {
    private LocalDate date;
    private Map<String, BigDecimal> currencies;
}
