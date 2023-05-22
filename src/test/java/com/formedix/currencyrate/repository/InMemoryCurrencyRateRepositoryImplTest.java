package com.formedix.currencyrate.repository;

import com.formedix.currencyrate.domain.CurrencyRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/*
 ** Typically, unit tests for Repository classes are not necessary as they are often covered by Integration Testing.
 ** However, in this specific case, we have included unit tests for the InMemoryCurrencyRateRepositoryImpl class
 ** due to the presence of certain logic within our Memory Repository implementation.
 */
class InMemoryCurrencyRateRepositoryImplTest {

    private InMemoryCurrencyRateRepositoryImpl repository;

    @BeforeEach
    public void setUp() {
        List<CurrencyRate> currencyRates = generateCurrencyRatesData();
        CurrencyRatesContextHolder currencyRatesContextHolder = new CurrencyRatesContextHolder();
        currencyRatesContextHolder.set(currencyRates);
        repository = new InMemoryCurrencyRateRepositoryImpl(currencyRatesContextHolder);
    }

    @Test
    @DisplayName("Given currency rate exists for the specified date, when findByDate is called, then it should return the currency rate")
    void givenCurrencyRateExistsForSpecifiedDate_whenFindByDate_thenReturnCurrencyRate() {
        // Given
        LocalDate date = LocalDate.of(2022, 1, 2);

        // When
        Optional<CurrencyRate> result = repository.findByDate(date);

        // Then
        assertThat(result).isPresent();
    }

    @Test
    @DisplayName("Given no currency rate exists for the specified date, when findByDate is called, then it should return an empty optional")
    void givenNoCurrencyRateExistsForSpecifiedDate_whenFindByDate_thenReturnEmptyOptional() {
        // Given
        LocalDate date = LocalDate.of(2023, 1, 3);

        // When
        Optional<CurrencyRate> result = repository.findByDate(date);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Given currency rates exist within the specified date range, when findBetweenDates is called, then it should return the currency rates within the range")
    void givenCurrencyRatesExistWithinDateRange_whenFindBetweenDates_thenReturnCurrencyRatesWithinRange() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 4);

        // When
        List<CurrencyRate> result = repository.findBetweenDates(startDate, endDate);

        // Then
        assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("Given no currency rates exist within the specified date range, when findBetweenDates is called, then it should return an empty list")
    void givenNoCurrencyRatesExistWithinDateRange_whenFindBetweenDates_thenReturnEmptyList() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 1, 5);
        LocalDate endDate = LocalDate.of(2023, 1, 6);

        // When
        List<CurrencyRate> result = repository.findBetweenDates(startDate, endDate);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Given currency rates are updated, when update is called, then it should update the currency rates and return the updated rates")
    void givenCurrencyRatesAreUpdated_whenUpdate_thenCurrencyRatesShouldBeUpdatedAndReturnUpdatedRates() {
        // Given
        List<CurrencyRate> updatedCurrencyRates = generateUpdatedCurrencyRatesData();

        // When
        List<CurrencyRate> result = repository.update(updatedCurrencyRates);

        // Then
        assertThat(result).isEqualTo(updatedCurrencyRates);
        assertThat(repository.findAll()).isEqualTo(updatedCurrencyRates);
    }

    @Test
    @DisplayName("Given currency rates exist, when findAll is called, then it should return all currency rates")
    void givenCurrencyRatesExist_whenFindAll_thenReturnAllCurrencyRates() {
        // When
        List<CurrencyRate> result = repository.findAll();

        // Then
        assertThat(result).hasSize(4);
    }

    @Test
    @DisplayName("Given no currency rates exist, when findAll is called, then it should return an empty list")
    void givenNoCurrencyRatesExist_whenFindAll_thenReturnEmptyList() {
        // Given
        repository.update(new ArrayList<>());

        // When
        List<CurrencyRate> result = repository.findAll();

        // Then
        assertThat(result).isEmpty();
    }

    private List<CurrencyRate> generateCurrencyRatesData() {
        List<CurrencyRate> currencyRates = new ArrayList<>();
        currencyRates.add(new CurrencyRate(LocalDate.of(2021, 1, 1), Map.of("USD", BigDecimal.ONE)));
        currencyRates.add(new CurrencyRate(LocalDate.of(2022, 1, 2), Map.of("PH", BigDecimal.ZERO)));
        currencyRates.add(new CurrencyRate(LocalDate.of(2023, 1, 1), Map.of("UK", BigDecimal.TEN)));
        currencyRates.add(new CurrencyRate(LocalDate.of(2023, 1, 4), Map.of("CNY", BigDecimal.TEN)));
        return currencyRates;
    }

    private List<CurrencyRate> generateUpdatedCurrencyRatesData() {
        List<CurrencyRate> updatedCurrencyRates = new ArrayList<>();
        updatedCurrencyRates.add(new CurrencyRate(LocalDate.of(2021, 1, 1), Map.of("JPY", BigDecimal.TEN)));
        updatedCurrencyRates.add(new CurrencyRate(LocalDate.of(2022, 1, 2), Map.of("SG", BigDecimal.ONE)));
        updatedCurrencyRates.add(new CurrencyRate(LocalDate.of(2023, 1, 1), Map.of("MY", BigDecimal.ZERO)));
        updatedCurrencyRates.add(new CurrencyRate(LocalDate.of(2023, 1, 4), Map.of("PH", BigDecimal.ZERO)));
        return updatedCurrencyRates;
    }

}

