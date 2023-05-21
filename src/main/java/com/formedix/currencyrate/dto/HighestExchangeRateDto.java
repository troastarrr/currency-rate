package com.formedix.currencyrate.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class HighestExchangeRateDto {
    private LocalDate startDate;
    private LocalDate endDate;
    private String currency;
    private BigDecimal highestExchangeRate;
}
