package com.formedix.currencyrate.repository;

import com.formedix.currencyrate.domain.CurrencyRate;
import com.formedix.currencyrate.domain.CurrencyRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Repository
public class InMemoryCurrencyRateRepositoryImpl implements CurrencyRateRepository<CurrencyRate, CurrencyRates> {
    @Autowired
    private CurrencyRates currencyRates;

    /**
     * Finds a currency rate by the specified date.
     *
     * @param date the date to search for
     *
     * @return an Optional containing the currency rate if found, or an empty Optional if not found
     */
    @Override
    public Optional<CurrencyRate> findCurrencyRateByDate(LocalDate date) {
        return currencyRates.getRates().stream()
                .filter(rate -> Objects.equals(date, rate.getDate()))
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
    public CurrencyRates update(CurrencyRates updatedCurrencyRates) {
        currencyRates.setRates(updatedCurrencyRates.getRates());
        return currencyRates;
    }

    /**
     * Retrieves the current currency rates.
     *
     * @return the current CurrencyRates object
     */
    @Override
    public CurrencyRates get() {
        return currencyRates;
    }
}