package com.formedix.currencyrate.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@Data
@Component
@ConfigurationProperties(prefix = "spring.cache")
public class CacheProperties {
    private String spec;
    private List<String> cacheNames;
}
