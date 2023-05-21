package com.formedix.currencyrate.repository;

import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface CurrencyRateRepository<T> {

    Optional<T> findByDate(LocalDate date);

    List<T> update(List<T> currencyRates);

    List<T> findAll();
}
