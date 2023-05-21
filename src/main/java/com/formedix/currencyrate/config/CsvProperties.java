package com.formedix.currencyrate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "csv")
@Data
public class CsvProperties {
    private String defaultCurrencyRateFilePath;
}
