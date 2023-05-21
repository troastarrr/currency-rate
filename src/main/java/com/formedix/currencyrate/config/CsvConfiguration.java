package com.formedix.currencyrate.config;

import com.formedix.currencyrate.domain.CurrencyRates;
import com.formedix.currencyrate.parser.CurrencyRateCsvParser;
import jakarta.servlet.MultipartConfigElement;
import lombok.AllArgsConstructor;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.unit.DataSize;

import java.io.IOException;

@Configuration
@AllArgsConstructor
public class CsvConfiguration {
    private final CurrencyRateCsvParser currencyRateCsvParser;
    private final CsvProperties csvProperties;

    @Bean
    @Primary
    public CurrencyRates currencyRateData(ResourceLoader resourceLoader) throws IOException {
        Resource resource = resourceLoader.getResource(csvProperties.getDefaultCurrencyRateFilePath());
        return currencyRateCsvParser.parse(resource.getInputStream());
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(csvProperties.getMaxRequestSize()));
        factory.setMaxRequestSize(DataSize.ofMegabytes(csvProperties.getMaxRequestSize()));
        return factory.createMultipartConfig();
    }
}
