package com.formedix.currencyrate.repository;

import com.formedix.currencyrate.domain.CurrencyRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Holds the current currency rates in memory repository.
 */
@Repository
public class InMemoryCurrencyRateRepositoryImpl implements CurrencyRateRepository<CurrencyRate> {
    @Autowired
    private CurrencyRatesContextHolder currencyRatesContextHolder;

    /**
     * Finds a currency rate by the specified date.
     *
     * @param date the date to search for
     *
     * @return an Optional containing the currency rate if found, or an empty Optional if not found
     */
    @Override
    public Optional<CurrencyRate> findByDate(LocalDate date) {
        return currencyRatesContextHolder.get().stream()
                .filter(rate -> Objects.equals(date, rate.date()))
                .findFirst();
    }

    /**
     * Updates the currency rates with the provided CurrencyRates object.
     *
     * @param updatedCurrencyRates the CurrencyRates object containing the updated currency rates
     *
     * @return the updated CurrencyRates object
     */
    @Override
    public List<CurrencyRate> update(List<CurrencyRate> updatedCurrencyRates) {
        currencyRatesContextHolder.set(updatedCurrencyRates);
        return currencyRatesContextHolder.get();
    }

    /**
     * Retrieves the current currency rates.
     *
     * @return the current CurrencyRates object
     */
    @Override
    public List<CurrencyRate> findAll() {
        return currencyRatesContextHolder.get();
    }
}