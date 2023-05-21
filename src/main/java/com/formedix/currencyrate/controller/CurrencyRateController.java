package com.formedix.currencyrate.controller;

import com.formedix.currencyrate.dto.AverageExchangeRateDto;
import com.formedix.currencyrate.dto.ConvertCurrencyDto;
import com.formedix.currencyrate.dto.GetCurrencyRateDto;
import com.formedix.currencyrate.dto.HighestExchangeRateDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;
import java.time.LocalDate;

@RequestMapping("/currency-rates/v1")
@Tag(name = "Currency Rate", description = "Manage currency rate transactions")
public interface CurrencyRateController {

    @GetMapping("/{date}")
    @Operation(
            summary = "Get Currency Rates by Date",
            description = "Retrieve the reference rate data for a given date for all available currencies",
            tags = "Currency Rate"
    )
    ResponseEntity<GetCurrencyRateDto> getCurrencyRatesByDate(@PathVariable("date") final LocalDate date);

    @GetMapping("/convert")
    @Operation(
            summary = "Convert Currency",
            description = "Convert an amount from the source currency to the target currency on a specific date",
            tags = "Currency Rate"
    )
    ResponseEntity<ConvertCurrencyDto> convertCurrency(
            @RequestParam("date") final LocalDate date,
            @RequestParam("sourceCurrency") final String sourceCurrency,
            @RequestParam("targetCurrency") final String targetCurrency,
            @RequestParam("amount") final BigDecimal amount);

    @GetMapping("/highest-rate")
    @Operation(
            summary = "Get Highest Exchange Rate",
            description = "Retrieve the highest reference exchange rate achieved by a currency within a specified period",
            tags = "Currency Rate"
    )
    ResponseEntity<HighestExchangeRateDto> getHighestExchangeRate(
            @RequestParam("startDate") final LocalDate startDate,
            @RequestParam("endDate") final LocalDate endDate,
            @RequestParam("currency") final String currency);

    @GetMapping("/average-rate")
    @Operation(
            summary = "Get Average Exchange Rate",
            description = "Calculate the average reference exchange rate of a currency within a specified period",
            tags = "Currency Rate"
    )
    ResponseEntity<AverageExchangeRateDto> getAverageExchangeRate(
            @RequestParam("startDate") final LocalDate startDate,
            @RequestParam("endDate") final LocalDate endDate,
            @RequestParam("currency") final String currency);
}