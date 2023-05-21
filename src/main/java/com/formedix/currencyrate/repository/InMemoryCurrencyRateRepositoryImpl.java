package com.formedix.currencyrate.repository;

import com.formedix.currencyrate.domain.CurrencyRate;
import com.formedix.currencyrate.domain.CurrencyRates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

@Repository
public class InMemoryCurrencyRateRepositoryImpl implements CurrencyRateRepository<CurrencyRate> {
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
    public Optional<CurrencyRate> findByDate(LocalDate date) {
        return currencyRates.getRates().stream()
                .filter(rate -> Objects.equals(date, rate.getDate()))
                .findFirst();
    }
}
