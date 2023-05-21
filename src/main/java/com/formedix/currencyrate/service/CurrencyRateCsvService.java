package com.formedix.currencyrate.service;

import com.formedix.currencyrate.domain.CurrencyRate;
import com.formedix.currencyrate.domain.CurrencyRates;
import com.formedix.currencyrate.parser.CurrencyRateCsvParser;
import com.formedix.currencyrate.repository.CurrencyRateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;

@Service
@AllArgsConstructor
public class CurrencyRateCsvService {

    private final CurrencyRateCsvParser currencyRateCsvParser;
    private final CurrencyRateRepository<CurrencyRate, CurrencyRates> currencyRatesCurrencyRateRepository;

    /**
     * Updates the currency rates with the data parsed from the provided CSV file.
     *
     * @param inputStream the input stream of the CSV file
     *
     * @return the updated currency rates
     */
    public CurrencyRates updateCurrencyRates(InputStream inputStream) {
        return currencyRatesCurrencyRateRepository.update(currencyRateCsvParser.parse(inputStream));
    }

    /**
     * Retrieves the current currency rates.
     *
     * @return the current currency rates
     */
    public CurrencyRates getCurrentCurrencyRates() {
        return currencyRatesCurrencyRateRepository.get();
    }
}