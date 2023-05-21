package com.formedix.currencyrate.service;

import com.formedix.currencyrate.domain.CurrencyRate;
import com.formedix.currencyrate.dto.AverageExchangeRateDto;
import com.formedix.currencyrate.dto.ConvertCurrencyDto;
import com.formedix.currencyrate.dto.GetCurrencyRateDto;
import com.formedix.currencyrate.dto.HighestExchangeRateDto;
import com.formedix.currencyrate.error.exception.CurrencyRateNotFoundException;
import com.formedix.currencyrate.mapper.CurrencyRateMapper;
import com.formedix.currencyrate.repository.CurrencyRateRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@AllArgsConstructor
public class CurrencyRateService {
    private final CurrencyRateMapper currencyRateMapper;
    private final CurrencyRateRepository<CurrencyRate> currencyRatesCurrencyRateRepository;

    /**
     * Retrieves the currency rates for a specific date.
     *
     * @param date the date for which to retrieve the currency rates
     *
     * @return the DTO containing the currency rates for the specified date
     *
     * @throws CurrencyRateNotFoundException if currency rates are not available for the specified date
     */
    public GetCurrencyRateDto getCurrencyRatesByDate(LocalDate date) {
        return currencyRatesCurrencyRateRepository.findByDate(date)
                .map(currencyRateMapper::toGetCurrencyRateDto)
                .orElseThrow(() -> new CurrencyRateNotFoundException("Currency rates not available for the specified date: " + date));
    }

    /**
     * Converts an amount from one currency to another for a specific date.
     *
     * @param date           the date for which to perform the currency conversion
     * @param sourceCurrency the source currency
     * @param targetCurrency the target currency
     * @param amount         the amount to convert
     *
     * @return the DTO containing the converted amount
     *
     * @throws CurrencyRateNotFoundException if currency rates are not available for the specified date or currencies
     */
    public ConvertCurrencyDto convertCurrency(LocalDate date, String sourceCurrency, String targetCurrency, BigDecimal amount) {
        CurrencyRate sourceRate = currencyRatesCurrencyRateRepository.findByDate(date)
                .orElseThrow(() -> new CurrencyRateNotFoundException("Currency rates not available for the specified date: " + date));

        BigDecimal sourceRateValue = sourceRate.getCurrencies().get(sourceCurrency);
        BigDecimal targetRateValue = sourceRate.getCurrencies().get(targetCurrency);

        if (sourceRateValue == null || targetRateValue == null) {
            String errorMessage = "Currency rates not available for the specified currencies source currency `%s` and target currency `%s`";
            throw new CurrencyRateNotFoundException(String.format(errorMessage, sourceRateValue, targetRateValue));
        }

        BigDecimal convertedAmount = amount.multiply(targetRateValue).divide(sourceRateValue, 2, RoundingMode.HALF_UP);
        return currencyRateMapper.toConvertCurrencyDto(date, sourceCurrency, targetCurrency, convertedAmount, sourceRateValue, targetRateValue);
    }

    /**
     * Retrieves the highest exchange rate achieved by a currency within a specified date range.
     *
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     * @param currency  the currency for which to find the highest exchange rate
     *
     * @return the DTO containing the highest exchange rate details
     *
     * @throws CurrencyRateNotFoundException if currency rates are not available for the specified date range or currency
     */
    public HighestExchangeRateDto getHighestExchangeRate(LocalDate startDate, LocalDate endDate, String currency) {
        CurrencyRate highestRate = getCurrencyRateStream(startDate, endDate)
                .filter(rate -> rate.getCurrencies().containsKey(currency))
                .max(Comparator.comparing(rate -> rate.getCurrencies().get(currency)))
                .orElseThrow(() -> new CurrencyRateNotFoundException("Currency rates not available for the specified date range and currency"));

        return currencyRateMapper.toHighestExchangeRateDto(startDate, endDate, currency, highestRate.getCurrencies().get(currency));
    }

    /**
     * Retrieves the average exchange rate achieved by a currency within a specified date range.
     *
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     * @param currency  the currency for which to calculate the average exchange rate
     *
     * @return the DTO containing the average exchange rate details
     *
     * @throws CurrencyRateNotFoundException if currency rates are not available for the specified date range or currency
     */
    public AverageExchangeRateDto getAverageExchangeRate(LocalDate startDate, LocalDate endDate, String currency) {
        Optional<Stream<CurrencyRate>> optionalCurrencyRateStream = Optional.of(getCurrencyRateStream(startDate, endDate));
        BigDecimal averageRateValue = optionalCurrencyRateStream
                .map(stream -> stream.map(rate -> rate.getCurrencies().get(currency))
                        .filter(Objects::nonNull)
                        .collect(Collectors.collectingAndThen(
                                Collectors.averagingDouble(BigDecimal::doubleValue), BigDecimal::valueOf))
                        .setScale(2, RoundingMode.HALF_UP))
                .orElseThrow(() -> new CurrencyRateNotFoundException("No currency rates available for the specified date range and currency"));

        if (averageRateValue.compareTo(BigDecimal.ZERO) <= 0) {
            throw new CurrencyRateNotFoundException("No currency rates available for the specified date range and currency");
        }

        return currencyRateMapper.toAverageExchangeRateDto(startDate, endDate, currency, averageRateValue);
    }

    /**
     * Retrieves a stream of currency rates within a specified date range.
     *
     * @param startDate the start date of the range
     * @param endDate   the end date of the range
     *
     * @return the stream of currency rates within the specified date range
     */
    private Stream<CurrencyRate> getCurrencyRateStream(LocalDate startDate, LocalDate endDate) {
        return Stream.iterate(startDate, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(startDate, endDate.plusDays(1)))
                .flatMap(date -> currencyRatesCurrencyRateRepository.findByDate(date).stream());
    }
}
