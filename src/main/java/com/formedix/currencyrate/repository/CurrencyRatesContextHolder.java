package com.formedix.currencyrate.repository;

import com.formedix.currencyrate.domain.CurrencyRate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Holds the current currency rates in memory.
 */
@Component
public class CurrencyRatesContextHolder {

    private final AtomicReference<List<CurrencyRate>> rates = new AtomicReference<>();

    /**
     * Get the current currency rates.
     *
     * @return the current currency rates
     */
    public List<CurrencyRate> get() {
        return this.rates.get();
    }

    /**
     * Set the currency rates.
     *
     * @param rates the currency rates to set
     */
    public void set(List<CurrencyRate> rates) {
        this.rates.set(rates);
    }
}
