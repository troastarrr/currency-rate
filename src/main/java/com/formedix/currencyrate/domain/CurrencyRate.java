package com.formedix.currencyrate.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public record CurrencyRate(LocalDate date, Map<String, BigDecimal> currencies) {
}
