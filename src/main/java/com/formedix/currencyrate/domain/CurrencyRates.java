package com.formedix.currencyrate.domain;

import lombok.Data;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
public class CurrencyRates {
    private List<CurrencyRate> rates;
}
