package com.formedix.currencyrate.service;

import com.formedix.currencyrate.domain.CurrencyRate;
import com.formedix.currencyrate.dto.AverageExchangeRateDto;
import com.formedix.currencyrate.dto.ConvertCurrencyDto;
import com.formedix.currencyrate.dto.GetCurrencyRateDto;
import com.formedix.currencyrate.dto.HighestExchangeRateDto;
import com.formedix.currencyrate.error.exception.CurrencyRateNotFoundException;
import com.formedix.currencyrate.mapper.CurrencyRateMapper;
import com.formedix.currencyrate.repository.CurrencyRateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.formedix.currencyrate.service.CurrencyRateService.CURRENCY_NOT_FOUND_FOR_DATE_AND_CURRENCY_ERROR_MESSAGE;
import static com.formedix.currencyrate.service.CurrencyRateService.CURRENCY_NOT_FOUND_FOR_DATE_ERROR_MESSAGE;
import static com.formedix.currencyrate.service.CurrencyRateService.CURRENCY_NOT_FOUND_FOR_TARGET_AND_SOURCE_CURRENCY_ERROR_MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyRateServiceTest {

    private final CurrencyRateMapper currencyRateMapper = Mappers.getMapper(CurrencyRateMapper.class);
    @Mock
    private CurrencyRateRepository<CurrencyRate> currencyRateRepository;
    private CurrencyRateService currencyRateService;

    @BeforeEach
    public void setUp() {
        currencyRateService = new CurrencyRateService(currencyRateMapper, currencyRateRepository);
    }

    @Test
    @DisplayName("Should return currency rates DTO when rates are available for the specified date")
    void getCurrencyRatesByDate_WhenRatesAvailable_ReturnDto() {
        // Given
        LocalDate requestDate = LocalDate.parse("2020-12-01");

        Map<String, BigDecimal> currencies = new HashMap<>();
        currencies.put("USD", BigDecimal.valueOf(1.0));
        currencies.put("EUR", BigDecimal.valueOf(0.85));
        CurrencyRate existingCurrencyRate = new CurrencyRate(requestDate, currencies);

        when(currencyRateRepository.findByDate(requestDate)).thenReturn(Optional.of(existingCurrencyRate));

        // When
        GetCurrencyRateDto result = currencyRateService.getCurrencyRatesByDate(requestDate);

        // Then
        assertThat(result.getCurrencies()).isEqualTo(existingCurrencyRate.currencies());
        assertThat(result.getDate()).isEqualTo(existingCurrencyRate.date());
        verify(currencyRateRepository, times(1)).findByDate(requestDate);
    }

    @Test
    @DisplayName("Should throw CurrencyRateNotFoundException when rates are not available for the specified date")
    void getCurrencyRatesByDate_WhenRatesNotAvailable_ThrowNotFoundException() {
        // Given
        LocalDate date = LocalDate.now();

        when(currencyRateRepository.findByDate(date)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> currencyRateService.getCurrencyRatesByDate(date))
                .isInstanceOf(CurrencyRateNotFoundException.class)
                .hasMessage(String.format(CURRENCY_NOT_FOUND_FOR_DATE_ERROR_MESSAGE, date));

        verify(currencyRateRepository, times(1)).findByDate(date);
    }

    @ParameterizedTest
    @MethodSource("provideCurrencyRateCombinations")
    @DisplayName("Should convert currency successfully when rates are available for the specified date and currencies")
    void givenRatesAvailableAndCurrenciesValid_whenConvertCurrency_thenReturnConvertCurrencyDto(
            LocalDate date, String sourceCurrency, String targetCurrency,
            BigDecimal amount, CurrencyRate existingCurrencyRate, BigDecimal expectedConvertedAmount) {
        // Given
        when(currencyRateRepository.findByDate(date)).thenReturn(Optional.of(existingCurrencyRate));

        // When
        ConvertCurrencyDto result = currencyRateService.convertCurrency(date, sourceCurrency, targetCurrency, amount);

        // Then
        assertThat(result.getConvertedAmount()).isEqualByComparingTo(expectedConvertedAmount);

        // Verify
        verify(currencyRateRepository, times(1)).findByDate(date);
    }

    @Test
    @DisplayName("Should throw CurrencyRateNotFoundException when rates are not available for the specified date")
    void givenRatesNotAvailable_whenConvertCurrency_thenThrowNotFoundException() {
        // Given
        LocalDate date = LocalDate.now();
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100);

        when(currencyRateRepository.findByDate(date)).thenReturn(Optional.empty());

        // Then
        assertThatThrownBy(() -> currencyRateService.convertCurrency(date, sourceCurrency, targetCurrency, amount))
                .isInstanceOf(CurrencyRateNotFoundException.class)
                .hasMessageContaining(String.format(CURRENCY_NOT_FOUND_FOR_DATE_ERROR_MESSAGE, date));

        // Verify
        verify(currencyRateRepository, times(1)).findByDate(date);
    }

    @Test
    @DisplayName("Should throw CurrencyRateNotFoundException when rates are available but source or target currency is not found")
    void givenRatesAvailableButCurrenciesInvalid_whenConvertCurrency_thenThrowNotFoundException() {
        // Given
        LocalDate date = LocalDate.now();
        String sourceCurrency = "USD";
        String targetCurrency = "EUR";
        BigDecimal amount = BigDecimal.valueOf(100);

        Map<String, BigDecimal> currencies = new HashMap<>();
        currencies.put("USD", BigDecimal.valueOf(1.0));
        currencies.put("PHP", BigDecimal.valueOf(1.0)); // Missing target currency in the map

        CurrencyRate sourceRate = new CurrencyRate(date, currencies);

        when(currencyRateRepository.findByDate(date)).thenReturn(Optional.of(sourceRate));

        // Then
        assertThatThrownBy(() -> currencyRateService.convertCurrency(date, sourceCurrency, targetCurrency, amount))
                .isInstanceOf(CurrencyRateNotFoundException.class)
                .hasMessage(String.format(CURRENCY_NOT_FOUND_FOR_TARGET_AND_SOURCE_CURRENCY_ERROR_MESSAGE, sourceCurrency, targetCurrency));

        // Verify
        verify(currencyRateRepository, times(1)).findByDate(date);
    }

    @ParameterizedTest
    @MethodSource("provideCurrencyRateCombinationsForHighestRate")
    @DisplayName("Should get the highest exchange rate successfully when rates are available for the specified date range and currency")
    void givenRatesAvailableAndCurrencyValid_whenGetHighestExchangeRate_thenReturnHighestExchangeRateDto(
            LocalDate startDate, LocalDate endDate, String currency,
            List<CurrencyRate> currencyRateList, BigDecimal expectedHighestRate) {

        // Given
        IntStream.range(0, currencyRateList.size())
                .forEach(i -> when(currencyRateRepository.findByDate(startDate.plusDays(i)))
                        .thenReturn(Optional.of(currencyRateList.get(i))));

        // When
        HighestExchangeRateDto result = currencyRateService.getHighestExchangeRate(startDate, endDate, currency);

        // Then
        assertThat(result.getHighestExchangeRate()).isEqualTo(expectedHighestRate);

        // Verify
        IntStream.range(0, currencyRateList.size())
                .forEach(i ->
                        verify(currencyRateRepository, times(1)).findByDate(startDate.plusDays(i))
                );
    }

    @Test
    @DisplayName("Should throw CurrencyRateNotFoundException when currency is not present in any rate")
    void givenCurrencyNotInAnyRate_whenGetHighestExchangeRate_thenThrowCurrencyRateNotFoundException() {
        // Given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 5);
        String currency = "ABC";  // currency that does not exist

        //When
        when(currencyRateRepository.findByDate(startDate)).thenReturn(Optional.empty());

        //then
        assertThatThrownBy(() -> currencyRateService.getHighestExchangeRate(startDate, endDate, currency))
                .isInstanceOf(CurrencyRateNotFoundException.class)
                .hasMessage(String.format(CURRENCY_NOT_FOUND_FOR_DATE_AND_CURRENCY_ERROR_MESSAGE, currency));

        // Verify
        verify(currencyRateRepository, times(1)).findByDate(startDate);
    }

    @ParameterizedTest
    @MethodSource("provideCurrencyRateCombinationsForAverageRate")
    @DisplayName("Should get the average exchange rate successfully when rates are available for the specified date range and currency")
    void givenRatesAvailableAndCurrencyValid_whenGetAverageExchangeRate_thenReturnAverageExchangeRateDto(
            LocalDate startDate, LocalDate endDate, String currency,
            List<CurrencyRate> currencyRateList, BigDecimal expectedAverageRate) {

        // Given
        IntStream.range(0, currencyRateList.size())
                .forEach(i -> when(currencyRateRepository.findByDate(startDate.plusDays(i)))
                        .thenReturn(Optional.of(currencyRateList.get(i))));

        // When
        AverageExchangeRateDto result = currencyRateService.getAverageExchangeRate(startDate, endDate, currency);

        // Then
        assertThat(result.getAverageExchangeRate()).isEqualTo(expectedAverageRate);
    }

    @Test
    @DisplayName("Should throw CurrencyRateNotFoundException when no rates are available for the specified currency")
    void givenNoRatesAvailableForSpecifiedCurrency_whenGetAverageExchangeRate_thenThrowCurrencyRateNotFoundException() {
        //Given
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 5);
        String currency = "ABC";  // currency that does not exist in the rates

        //When
        when(currencyRateRepository.findByDate(startDate)).thenReturn(Optional.empty());

        //Then
        assertThatThrownBy(() -> currencyRateService.getAverageExchangeRate(startDate, endDate, currency))
                .isInstanceOf(CurrencyRateNotFoundException.class)
                .hasMessage(String.format(CURRENCY_NOT_FOUND_FOR_DATE_AND_CURRENCY_ERROR_MESSAGE, currency));
    }

    private static Stream<Arguments> provideCurrencyRateCombinationsForAverageRate() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 5);

        // Assuming we have currency rates for 5 consecutive days
        List<CurrencyRate> currencyRateList = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> createCurrencyRate(
                        LocalDate.of(2023, 1, i),
                        new Currency("USD", BigDecimal.valueOf(i * 1.55)),
                        new Currency("EUR", BigDecimal.valueOf(i * 2.55)),
                        new Currency("GBP", BigDecimal.valueOf(i * 3.55)),
                        new Currency("JPY", BigDecimal.valueOf(i * 4.55)),
                        new Currency("CAD", BigDecimal.valueOf(i * 5.55))
                ))
                .collect(Collectors.toList());

        return Stream.of(
                Arguments.of(startDate, endDate, "USD", currencyRateList, BigDecimal.valueOf(4.65)),
                Arguments.of(startDate, endDate, "EUR", currencyRateList, BigDecimal.valueOf(7.65)),
                Arguments.of(startDate, endDate, "GBP", currencyRateList, BigDecimal.valueOf(10.65)),
                Arguments.of(startDate, endDate, "JPY", currencyRateList, BigDecimal.valueOf(13.65)),
                Arguments.of(startDate, endDate, "CAD", currencyRateList, BigDecimal.valueOf(16.65))
        );
    }

    private static Stream<Arguments> provideCurrencyRateCombinationsForHighestRate() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 5);

        // Assuming we have currency rates for 5 consecutive days
        List<CurrencyRate> currencyRateList = IntStream.rangeClosed(1, 5)
                .mapToObj(i -> createCurrencyRate(
                        LocalDate.of(2023, 1, i),
                        new Currency("USD", BigDecimal.valueOf(i)),
                        new Currency("EUR", BigDecimal.valueOf(i * 0.85)),
                        new Currency("GBP", BigDecimal.valueOf(i * 0.72)),
                        new Currency("JPY", BigDecimal.valueOf(i * 110.25)),
                        new Currency("CAD", BigDecimal.valueOf(i * 1.21))
                ))
                .toList();

        return Stream.of(
                Arguments.of(startDate, endDate, "USD", currencyRateList, BigDecimal.valueOf(5)),
                Arguments.of(startDate, endDate, "EUR", currencyRateList, BigDecimal.valueOf(5 * 0.85)),
                Arguments.of(startDate, endDate, "GBP", currencyRateList, BigDecimal.valueOf(5 * 0.72)),
                Arguments.of(startDate, endDate, "JPY", currencyRateList, BigDecimal.valueOf(5 * 110.25)),
                Arguments.of(startDate, endDate, "CAD", currencyRateList, BigDecimal.valueOf(5 * 1.21))
        );
    }

    private static Stream<Arguments> provideCurrencyRateCombinations() {
        LocalDate date = LocalDate.now();

        CurrencyRate existingCurrencyRate1 = createCurrencyRate(
                date,
                createCurrency("USD", BigDecimal.valueOf(1.0)),
                createCurrency("EUR", BigDecimal.valueOf(0.85)),
                createCurrency("GBP", BigDecimal.valueOf(0.72)),
                createCurrency("JPY", BigDecimal.valueOf(110.25)),
                createCurrency("CAD", BigDecimal.valueOf(1.21))
        );

        CurrencyRate existingCurrencyRate2 = createCurrencyRate(
                date,
                createCurrency("USD", BigDecimal.valueOf(1.2)),
                createCurrency("EUR", BigDecimal.valueOf(0.9)),
                createCurrency("GBP", BigDecimal.valueOf(0.78)),
                createCurrency("JPY", BigDecimal.valueOf(105.5)),
                createCurrency("CAD", BigDecimal.valueOf(1.28))
        );

        CurrencyRate existingCurrencyRate3 = createCurrencyRate(
                date,
                createCurrency("USD", BigDecimal.valueOf(1.1)),
                createCurrency("EUR", BigDecimal.valueOf(0.95)),
                createCurrency("GBP", BigDecimal.valueOf(0.82)),
                createCurrency("JPY", BigDecimal.valueOf(108.75)),
                createCurrency("CAD", BigDecimal.valueOf(1.25))
        );

        CurrencyRate existingCurrencyRate4 = createCurrencyRate(
                date,
                createCurrency("USD", BigDecimal.valueOf(1.15)),
                createCurrency("EUR", BigDecimal.valueOf(0.88)),
                createCurrency("GBP", BigDecimal.valueOf(0.75)),
                createCurrency("JPY", BigDecimal.valueOf(112.8)),
                createCurrency("CAD", BigDecimal.valueOf(1.18))
        );

        CurrencyRate existingCurrencyRate5 = createCurrencyRate(
                date,
                createCurrency("USD", BigDecimal.valueOf(1.18)),
                createCurrency("EUR", BigDecimal.valueOf(0.92)),
                createCurrency("GBP", BigDecimal.valueOf(0.79)),
                createCurrency("JPY", BigDecimal.valueOf(107.35)),
                createCurrency("CAD", BigDecimal.valueOf(1.32))
        );

        return Stream.of(
                Arguments.of(date, "USD", "EUR", BigDecimal.valueOf(100), existingCurrencyRate1, BigDecimal.valueOf(85.0)),
                Arguments.of(date, "USD", "EUR", BigDecimal.valueOf(150), existingCurrencyRate2, BigDecimal.valueOf(112.50)),
                Arguments.of(date, "GBP", "USD", BigDecimal.valueOf(200), existingCurrencyRate3, BigDecimal.valueOf(268.29)),
                Arguments.of(date, "JPY", "CAD", BigDecimal.valueOf(50), existingCurrencyRate4, BigDecimal.valueOf(0.52)),
                Arguments.of(date, "CAD", "USD", BigDecimal.valueOf(120), existingCurrencyRate5, BigDecimal.valueOf(107.27))
        );
    }

    private static CurrencyRate createCurrencyRate(LocalDate date, Currency... currencies) {
        return new CurrencyRate(date, Arrays.stream(currencies).collect(Collectors.toMap(Currency::code, Currency::rate)));
    }

    private static Currency createCurrency(String code, BigDecimal rate) {
        return new Currency(code, rate);
    }

    private record Currency(String code, BigDecimal rate) {
    }
}