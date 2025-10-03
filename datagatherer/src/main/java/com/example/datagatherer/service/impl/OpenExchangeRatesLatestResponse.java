package com.example.datagatherer.service.impl;

import java.math.BigDecimal;
import java.util.Map;

public class OpenExchangeRatesLatestResponse {
    private String base;                   // "USD" on free tier
    private long timestamp;                // epoch seconds
    private Map<String, BigDecimal> rates; // e.g. {"PLN": 4.28, "EUR": 0.92}

    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }
    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }
    public Map<String, BigDecimal> getRates() { return rates; }
    public void setRates(Map<String, BigDecimal> rates) { this.rates = rates; }
}
