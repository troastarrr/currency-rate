package com.formedix.currencyrate.parser;

import com.formedix.currencyrate.domain.CurrencyRate;
import com.formedix.currencyrate.error.exception.CsvParsingException;
import com.formedix.currencyrate.repository.CurrencyRatesContextHolder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(MockitoExtension.class)
class CurrencyRateCsvParserTest {

    private CurrencyRateCsvParser currencyRateCsvParser;

    @BeforeEach
    void setUp() {
        currencyRateCsvParser = new CurrencyRateCsvParser();
    }

    @Test
    @DisplayName("Should parse CSV input stream into CurrencyRatesContextHolder successfully when valid data is provided")
    void shouldParseCSVIntoCurrencyRatesContextHolder() {
        // Given
        String csvData = "date,USD,EUR,GBP,JPY,CAD\n" +
                "2023-01-01,1.2345,1.2345,1.2345,1.2345,1.2345";
        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));

        // When
        CurrencyRatesContextHolder result = currencyRateCsvParser.parse(inputStream);

        // Then
        List<CurrencyRate> currencyRates = result.get();
        assertThat(currencyRates).isNotEmpty();
        CurrencyRate firstCurrencyRate = currencyRates.get(0);
        assertThat(firstCurrencyRate.date().toString()).hasToString("2023-01-01");
        assertThat(firstCurrencyRate.currencies().get("USD")).isEqualByComparingTo("1.2345");
    }

    @Test
    @DisplayName("Should throw CsvParsingException when invalid CSV data is provided")
    void shouldThrowCsvParsingException() {
        // Given
        String csvData = "date\n" +
                "2023-01-01,1.2345,1.2345,1.2345,1.2345,1.2345";
        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));

        // When & Then
        assertThatThrownBy(() -> currencyRateCsvParser.parse(inputStream))
                .isInstanceOf(CsvParsingException.class)
                .hasMessage("CSV file must have at least 2 columns");
    }

    @Test
    @DisplayName("Should throw CsvParsingException when CSV data is empty")
    void shouldThrowCsvParsingExceptionWhenCsvIsEmpty() {
        // Given
        String csvData = "";
        InputStream inputStream = new ByteArrayInputStream(csvData.getBytes(StandardCharsets.UTF_8));

        // When & Then
        assertThatThrownBy(() -> currencyRateCsvParser.parse(inputStream))
                .isInstanceOf(CsvParsingException.class)
                .hasMessage("CSV file is empty");
    }
}
