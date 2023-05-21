package com.formedix.currencyrate.config;

import com.formedix.currencyrate.mapper.CurrencyRateMapper;
import com.formedix.currencyrate.mapper.CurrencyRateMapperImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfiguration {
    @Bean
    public CurrencyRateMapper currencyRateMapper() {
        return new CurrencyRateMapperImpl();
    }
}

