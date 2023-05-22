package com.formedix.currencyrate.repository;

import com.formedix.currencyrate.domain.CurrencyRate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Holds the current currency rates in memory repository.
 */
@Repository
@AllArgsConstructor
public class InMemoryCurrencyRateRepositoryImpl implements CurrencyRateRepository<CurrencyRate> {
    private final CurrencyRatesContextHolder currencyRatesContextHolder;

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
     * Finds currency rates between the specified start and end dates (inclusive).
     *
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     *
     * @return a list of currency rates within the specified date range
     */
    @Override
    public List<CurrencyRate> findBetweenDates(LocalDate startDate, LocalDate endDate) {
        return currencyRatesContextHolder.get().stream()
                .filter(rate -> !rate.date().isBefore(startDate) && rate.date().isBefore(endDate.plusDays(1)))
                .toList();
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