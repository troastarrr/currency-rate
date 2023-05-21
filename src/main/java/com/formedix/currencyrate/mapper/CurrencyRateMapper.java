package com.formedix.currencyrate.mapper;

import com.formedix.currencyrate.domain.CurrencyRate;
import com.formedix.currencyrate.dto.AverageExchangeRateDto;
import com.formedix.currencyrate.dto.ConvertCurrencyDto;
import com.formedix.currencyrate.dto.GetCurrencyRateDto;
import com.formedix.currencyrate.dto.HighestExchangeRateDto;
import org.mapstruct.Mapper;

import java.math.BigDecimal;
import java.time.LocalDate;

@Mapper
public interface CurrencyRateMapper {
    /**
     * Maps a CurrencyRate entity to GetCurrencyRateDto.
     *
     * @param currencyRate the CurrencyRate entity
     *
     * @return the mapped GetCurrencyRateDto
     */
    GetCurrencyRateDto toGetCurrencyRateDto(CurrencyRate currencyRate);
    
    /**
     * Maps the given data to a ConvertCurrencyDto.
     *
     * @param date           the date of the conversion
     * @param sourceCurrency the source currency
     * @param targetCurrency the target currency
     * @param amount         the converted amount
     *
     * @return the mapped ConvertCurrencyDto
     */
    default ConvertCurrencyDto toConvertCurrencyDto(LocalDate date, String sourceCurrency, String targetCurrency, BigDecimal amount) {
        ConvertCurrencyDto convertCurrencyDto = new ConvertCurrencyDto();
        convertCurrencyDto.setSourceCurrency(sourceCurrency);
        convertCurrencyDto.setTargetCurrency(targetCurrency);
        convertCurrencyDto.setConvertedAmount(amount);
        convertCurrencyDto.setDate(date);
        return convertCurrencyDto;
    }

    /**
     * Maps the given data to an AverageExchangeRateDto.
     *
     * @param startDate        the start date of the rate period
     * @param endDate          the end date of the rate period
     * @param currency         the currency
     * @param averageRateValue the average exchange rate value
     *
     * @return the mapped AverageExchangeRateDto
     */
    default AverageExchangeRateDto toAverageExchangeRateDto(LocalDate startDate, LocalDate endDate, String currency, BigDecimal averageRateValue) {
        AverageExchangeRateDto exchangeRateDto = new AverageExchangeRateDto();
        exchangeRateDto.setStartDate(startDate);
        exchangeRateDto.setEndDate(endDate);
        exchangeRateDto.setCurrency(currency);
        exchangeRateDto.setAverageExchangeRate(averageRateValue);
        return exchangeRateDto;
    }

    /**
     * Maps the given data to a HighestExchangeRateDto.
     *
     * @param startDate                the start date of the rate period
     * @param endDate                  the end date of the rate period
     * @param currency                 the currency
     * @param highestExchangeRateValue the highest exchange rate value
     *
     * @return the mapped HighestExchangeRateDto
     */
    default HighestExchangeRateDto toHighestExchangeRateDto(LocalDate startDate, LocalDate endDate, String currency, BigDecimal highestExchangeRateValue) {
        HighestExchangeRateDto exchangeRateDto = new HighestExchangeRateDto();
        exchangeRateDto.setStartDate(startDate);
        exchangeRateDto.setEndDate(endDate);
        exchangeRateDto.setCurrency(currency);
        exchangeRateDto.setHighestExchangeRate(highestExchangeRateValue);
        return exchangeRateDto;
    }
}