package com.formedix.currencyrate.config;

import com.formedix.currencyrate.domain.CurrencyRates;
import com.formedix.currencyrate.service.CsvParserService;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.IOException;

@Configuration
@AllArgsConstructor
public class CsvConfiguration {
    private final CsvParserService csvParserService;
    private final CsvProperties csvProperties;

    @Bean
    @Primary
    public CurrencyRates currencyRateData(ResourceLoader resourceLoader) throws IOException {
        Resource resource = resourceLoader.getResource(csvProperties.getDefaultCurrencyRateFilePath());
        return csvParserService.parse(resource.getInputStream());
    }
}
