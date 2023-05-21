package com.formedix.currencyrate.service;

import com.formedix.currencyrate.domain.CurrencyRate;
import com.formedix.currencyrate.dto.GetCurrencyRateDto;
import com.formedix.currencyrate.mapper.CurrencyRateMapper;
import com.formedix.currencyrate.parser.CurrencyRateCsvParser;
import com.formedix.currencyrate.repository.CurrencyRateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.util.List;

@Service
@AllArgsConstructor
public class CurrencyRateCsvService {
    private final CurrencyRateMapper currencyRateMapper;
    private final CurrencyRateCsvParser currencyRateCsvParser;
    private final CurrencyRateRepository<CurrencyRate> currencyRatesCurrencyRateRepository;

    /**
     * Updates the currency rates with the data parsed from the provided CSV file.
     *
     * @param inputStream the input stream of the CSV file
     *
     * @return the updated currency rates
     */
    public List<GetCurrencyRateDto> updateCurrencyRates(InputStream inputStream) {
        return currencyRatesCurrencyRateRepository.update(currencyRateCsvParser.parse(inputStream).get())
                .stream().map(currencyRateMapper::toGetCurrencyRateDto).toList();
    }

    /**
     * Retrieves the current currency rates.
     *
     * @return the current currency rates
     */
    public List<GetCurrencyRateDto> getCurrentCurrencyRates() {
        return currencyRatesCurrencyRateRepository.findAll()
                .stream().map(currencyRateMapper::toGetCurrencyRateDto).toList();
    }
}