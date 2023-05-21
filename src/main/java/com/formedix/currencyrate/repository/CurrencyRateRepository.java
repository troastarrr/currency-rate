package com.formedix.currencyrate.repository;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface CurrencyRateRepository<T, R> {

    Optional<T> findCurrencyRateByDate(LocalDate date);

    R update(R currencyRates);

    R get();
}
