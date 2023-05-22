package com.formedix.currencyrate.controller.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.formedix.currencyrate.dto.AverageExchangeRateDto;
import com.formedix.currencyrate.dto.ConvertCurrencyDto;
import com.formedix.currencyrate.dto.GetCurrencyRateDto;
import com.formedix.currencyrate.dto.HighestExchangeRateDto;
import com.formedix.currencyrate.service.CurrencyRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class CurrencyRateControllerImplTest {

    @MockBean
    private CurrencyRateService currencyRateService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Set up some default mock responses
        when(currencyRateService.getCurrencyRatesByDate(any(LocalDate.class)))
                .thenReturn(new GetCurrencyRateDto());
        when(currencyRateService.convertCurrency(any(LocalDate.class), any(String.class), any(String.class), any(BigDecimal.class)))
                .thenReturn(new ConvertCurrencyDto());
        when(currencyRateService.getHighestExchangeRate(any(LocalDate.class), any(LocalDate.class), any(String.class)))
                .thenReturn(new HighestExchangeRateDto());
        when(currencyRateService.getAverageExchangeRate(any(LocalDate.class), any(LocalDate.class), any(String.class)))
                .thenReturn(new AverageExchangeRateDto());
    }

    @Test
    @DisplayName("Should return Currency Rates by Date when requested with valid date")
    void shouldReturnCurrencyRatesByDate() throws Exception {
        // When
        mockMvc.perform(get("/currency-rates/v1/{date}", LocalDate.now().toString()))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should not return Currency Rates by Date when requested with invalid date")
    void shouldNotReturnCurrencyRatesByInvalidDate() throws Exception {
        // When
        mockMvc.perform(get("/currency-rates/v1/{date}", "invalid-date"))
                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should convert Currency when requested with valid parameters")
    void shouldConvertCurrency() throws Exception {
        // When
        mockMvc.perform(get("/currency-rates/v1/convert")
                        .param("date", LocalDate.now().toString())
                        .param("sourceCurrency", "USD")
                        .param("targetCurrency", "EUR")
                        .param("amount", "100"))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should not convert Currency when requested with missing parameters")
    void shouldNotConvertCurrencyWithMissingParams() throws Exception {
        // When
        mockMvc.perform(get("/currency-rates/v1/convert")
                        .param("date", LocalDate.now().toString())
                        .param("sourceCurrency", "USD")
                        .param("targetCurrency", "EUR"))
                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return Highest Exchange Rate when requested with valid parameters")
    void shouldReturnHighestExchangeRate() throws Exception {
        // When
        mockMvc.perform(get("/currency-rates/v1/highest-rate")
                        .param("startDate", LocalDate.now().minusDays(1).toString())
                        .param("endDate", LocalDate.now().toString())
                        .param("currency", "USD"))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should not return Highest Exchange Rate when requested with invalid parameters")
    void shouldNotReturnHighestExchangeRateWithInvalidParams() throws Exception {
        // When
        mockMvc.perform(get("/currency-rates/v1/highest-rate")
                        .param("startDate", "invalid-date")
                        .param("endDate", LocalDate.now().toString())
                        .param("currency", "USD"))
                // Then
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Should return Average Exchange Rate when requested with valid parameters")
    void shouldReturnAverageExchangeRate() throws Exception {
        // When
        mockMvc.perform(get("/currency-rates/v1/average-rate")
                        .param("startDate", LocalDate.now().minusDays(1).toString())
                        .param("endDate", LocalDate.now().toString())
                        .param("currency", "USD"))
                // Then
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Should not return Average Exchange Rate when requested with missing parameters")
    void shouldNotReturnAverageExchangeRateWithMissingParams() throws Exception {
        // When
        mockMvc.perform(get("/currency-rates/v1/average-rate")
                        .param("startDate", LocalDate.now().minusDays(1).toString())
                        .param("endDate", LocalDate.now().toString()))
                // Then
                .andExpect(status().isBadRequest());
    }

}