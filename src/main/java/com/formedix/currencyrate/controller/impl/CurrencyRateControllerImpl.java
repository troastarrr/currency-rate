package com.formedix.currencyrate.controller.impl;

import com.formedix.currencyrate.controller.CurrencyRateController;
import com.formedix.currencyrate.dto.AverageExchangeRateDto;
import com.formedix.currencyrate.dto.ConvertCurrencyDto;
import com.formedix.currencyrate.dto.GetCurrencyRateDto;
import com.formedix.currencyrate.dto.HighestExchangeRateDto;
import com.formedix.currencyrate.service.CurrencyRateService;
import com.formedix.currencyrate.validator.CurrencyRateValidator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Implementation of the Currency Rate Controller.
 */
@RestController
@AllArgsConstructor
public class CurrencyRateControllerImpl implements CurrencyRateController {

    private final CurrencyRateService currencyRateService;

    /**
     * Retrieves the reference rate data for a given date for all available currencies.
     *
     * @param date the date for which to retrieve the currency rates
     *
     * @return the response entity containing the currency rate data
     */
    @Override
    public ResponseEntity<GetCurrencyRateDto> getCurrencyRatesByDate(final LocalDate date) {
        return ResponseEntity.ok(currencyRateService.getCurrencyRatesByDate(date));
    }

    /**
     * Converts an amount from the source currency to the target currency on a specific date.
     *
     * @param date           the date of the currency conversion
     * @param sourceCurrency the source currency code
     * @param targetCurrency the target currency code
     * @param amount         the amount to convert
     *
     * @return the response entity containing the converted currency amount
     */
    @Override
    public ResponseEntity<ConvertCurrencyDto> convertCurrency(final LocalDate date,
                                                              final String sourceCurrency,
                                                              final String targetCurrency,
                                                              final BigDecimal amount) {
        return ResponseEntity.ok(currencyRateService.convertCurrency(date, sourceCurrency, targetCurrency, amount));
    }

    /**
     * Retrieves the highest reference exchange rate achieved by a currency within a specified period.
     *
     * @param startDate the start date of the period
     * @param endDate   the end date of the period
     * @param currency  the currency code
     *
     * @return the response entity containing the highest exchange rate information
     */
    @Override
    public ResponseEntity<HighestExchangeRateDto> getHighestExchangeRate(final LocalDate startDate,
                                                                         final LocalDate endDate,
                                                                         final String currency) {
        CurrencyRateValidator.checkIfValidDates(startDate, endDate);
        return ResponseEntity.ok(currencyRateService.getHighestExchangeRate(startDate, endDate, currency));
    }

    /**
     * Calculates the average reference exchange rate of a currency within a specified period.
     *
     * @param startDate the start date of the period
     * @param endDate   the end date of the period
     * @param currency  the currency code
     *
     * @return the response entity containing the average exchange rate information
     */
    @Override
    public ResponseEntity<AverageExchangeRateDto> getAverageExchangeRate(final LocalDate startDate,
                                                                         final LocalDate endDate,
                                                                         final String currency) {
        CurrencyRateValidator.checkIfValidDates(startDate, endDate);
        return ResponseEntity.ok(currencyRateService.getAverageExchangeRate(startDate, endDate, currency));
    }
}