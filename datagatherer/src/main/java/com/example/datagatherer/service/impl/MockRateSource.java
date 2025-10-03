package com.example.datagatherer.service.impl;

import com.example.datagatherer.service.RateSource;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Map;

@Component
@Profile("!prod")
public class MockRateSource implements RateSource {
    @Override
    public Map<String, BigDecimal> fetchLatest(String base) {

        return Map.of(
                "USD", new BigDecimal("1.0000"),
                "EUR", new BigDecimal("0.9300"),
                "PLN", new BigDecimal("4.3000"),
                "GBP", new BigDecimal("0.8000")
        );
    }
}
