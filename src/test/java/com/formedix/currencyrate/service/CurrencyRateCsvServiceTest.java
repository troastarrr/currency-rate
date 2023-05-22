package com.formedix.currencyrate.service;

import com.formedix.currencyrate.domain.CurrencyRate;
import com.formedix.currencyrate.dto.GetCurrencyRateDto;
import com.formedix.currencyrate.mapper.CurrencyRateMapper;
import com.formedix.currencyrate.parser.CurrencyRateCsvParser;
import com.formedix.currencyrate.repository.CurrencyRateRepository;
import com.formedix.currencyrate.repository.CurrencyRatesContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurrencyRateCsvServiceTest {
    private final CurrencyRateMapper currencyRateMapper = Mappers.getMapper(CurrencyRateMapper.class);
    @Mock
    private CurrencyRateCsvParser currencyRateCsvParser;
    @Mock
    private CurrencyRateRepository<CurrencyRate> currencyRateRepository;
    private CurrencyRateCsvService currencyRateCsvService;

    @BeforeEach
    void setUp() {
        currencyRateCsvService = new CurrencyRateCsvService(currencyRateMapper, currencyRateCsvParser, currencyRateRepository);
    }

    @Test
    @DisplayName("Should update currency rates successfully when valid CSV data is provided")
    void updateCurrencyRates() {
        // Given
        InputStream inputStream = createInputStream();
        CurrencyRate currencyRate = createCurrencyRate();
        CurrencyRatesContextHolder contextHolder = new CurrencyRatesContextHolder();
        contextHolder.set(List.of(currencyRate));

        when(currencyRateCsvParser.parse(inputStream)).thenReturn(contextHolder);
        when(currencyRateRepository.update(List.of(currencyRate))).thenReturn(List.of(currencyRate));

        // When
        List<GetCurrencyRateDto> result = currencyRateCsvService.updateCurrencyRates(inputStream);

        // Then
        verify(currencyRateRepository, times(1)).update(List.of(currencyRate));
        assertThat(result.get(0).getCurrencies()).isEqualTo(currencyRate.currencies());
    }

    @Test
    @DisplayName("Should get current currency rates successfully")
    void getCurrentCurrencyRates() {
        // Given
        CurrencyRate currencyRate = createCurrencyRate();

        when(currencyRateRepository.findAll()).thenReturn(List.of(currencyRate));

        // When
        List<GetCurrencyRateDto> result = currencyRateCsvService.getCurrentCurrencyRates();

        // Then
        verify(currencyRateRepository, times(1)).findAll();
        assertThat(result.get(0).getCurrencies()).isEqualTo(currencyRate.currencies());
    }

    private InputStream createInputStream() {
        String csvData = "date,USD,EUR,GBP,JPY,CAD\n" +
                "2023-01-01,1.2345,1.2345,1.2345,1.2345,1.2345";
        return new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));
    }

    private CurrencyRate createCurrencyRate() {
        LocalDate date = LocalDate.of(2023, 1, 1);
        Map<String, BigDecimal> currencies = new HashMap<>();
        currencies.put("USD", BigDecimal.valueOf(1.2345));
        currencies.put("EUR", BigDecimal.valueOf(1.2345));
        currencies.put("GBP", BigDecimal.valueOf(1.2345));
        currencies.put("JPY", BigDecimal.valueOf(1.2345));
        currencies.put("CAD", BigDecimal.valueOf(1.2345));
        return new CurrencyRate(date, currencies);
    }

}