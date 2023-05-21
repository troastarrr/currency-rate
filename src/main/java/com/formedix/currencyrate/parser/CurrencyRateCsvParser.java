package com.formedix.currencyrate.parser;

import com.formedix.currencyrate.domain.CurrencyRate;
import com.formedix.currencyrate.domain.CurrencyRates;
import com.formedix.currencyrate.error.exception.CsvParsingException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class CurrencyRateCsvParser {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Parses the provided CSV file input stream into a {@link CurrencyRates} object.
     *
     * @param inputStream the input stream of the CSV file
     *
     * @return the parsed {@link CurrencyRates} object
     *
     * @throws CsvParsingException if an error occurs while parsing the CSV file
     */
    public CurrencyRates parse(InputStream inputStream) throws CsvParsingException {
        try (CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            List<String[]> rows = reader.readAll();
            validateHeaders(rows);
            return createCurrencyRates(rows);
        } catch (IOException | CsvException e) {
            throw new CsvParsingException("Error occurred while parsing the CSV file", e);
        }
    }

    /**
     * Validates the headers of the CSV file.
     *
     * @param rows the rows of the CSV file
     *
     * @throws CsvParsingException if the CSV file does not have at least 2 columns
     */
    private void validateHeaders(List<String[]> rows) throws CsvParsingException {
        if (rows.isEmpty()) {
            throw new CsvParsingException("CSV file is empty");
        }
        String[] headers = rows.get(0);
        if (headers != null && headers.length < 2) {
            throw new CsvParsingException("CSV file must have at least 2 columns");
        }
    }

    /**
     * Creates a {@link CurrencyRates} object from the rows of the CSV file.
     *
     * @param rows the rows of the CSV file
     *
     * @return the created {@link CurrencyRates} object
     */
    private CurrencyRates createCurrencyRates(List<String[]> rows) {
        CurrencyRates currencyRates = new CurrencyRates();
        List<CurrencyRate> rateList = new ArrayList<>();
        List<String[]> dataRows = rows.stream().skip(1).toList();
        String[] headers = rows.get(0);
        dataRows.forEach(row -> {
            try {
                LocalDate date = parseDate(row[0]);
                Map<String, BigDecimal> currencies = new HashMap<>();
                for (int i = 1; i < row.length; i++) {
                    String currency = getCurrency(i, headers);
                    BigDecimal rate = parseRate(row[i]);
                    if (StringUtils.isNotBlank(currency)) {
                        currencies.put(currency, rate);
                    }
                }
                CurrencyRate currencyRate = new CurrencyRate();
                currencyRate.setDate(date);
                currencyRate.setCurrencies(currencies);
                rateList.add(currencyRate);
            } catch (DateTimeParseException e) {
                String errorMessage = "Unable to parse CSV date with error message: `%s`";
                log.warn(String.format(errorMessage, e.getMessage()));
                throw new CsvParsingException(String.format(errorMessage, e.getMessage()));
            }
        });
        currencyRates.setRates(rateList);
        return currencyRates;
    }

    /**
     * Parses a date string into a {@link LocalDate} object.
     *
     * @param dateString the date string to parse
     *
     * @return the parsed {@link LocalDate} object
     *
     * @throws DateTimeParseException if the date string is not in the expected format
     */
    private LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString, DATE_FORMATTER);
    }

    /**
     * Parses a rate value string into a {@link BigDecimal} object.
     * If the rate value is not a valid number, returns null.
     *
     * @param rateValue the rate value string to parse
     *
     * @return the parsed {@link BigDecimal} object, or null if the rate value is not a valid number
     */
    private BigDecimal parseRate(String rateValue) {
        return NumberUtils.isCreatable(rateValue) ? new BigDecimal(rateValue) : null;
    }

    /**
     * Gets the currency at the specified index in the headers array.
     * If the index is out of bounds or the currency is null, returns an empty string.
     *
     * @param index   the index of the currency in the headers array
     * @param headers the headers array
     *
     * @return the currency at the specified index, or an empty string if not found
     */
    private String getCurrency(int index, String[] headers) {
        return (index >= 0 && index < headers.length) ? headers[index] : StringUtils.EMPTY;
    }
}
