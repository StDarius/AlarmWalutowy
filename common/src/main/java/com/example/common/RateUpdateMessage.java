
package com.example.common;

import java.math.BigDecimal;
import java.time.Instant;

public class RateUpdateMessage {
    private String base;
    private String quote;
    private BigDecimal rate;
    private BigDecimal changePercent;
    private Instant timestamp;

    public RateUpdateMessage() {}

    public RateUpdateMessage(String base, String quote, BigDecimal rate, BigDecimal changePercent, Instant timestamp) {
        this.base = base;
        this.quote = quote;
        this.rate = rate;
        this.changePercent = changePercent;
        this.timestamp = timestamp;
    }

    public String getBase() { return base; }
    public void setBase(String base) { this.base = base; }
    public String getQuote() { return quote; }
    public void setQuote(String quote) { this.quote = quote; }
    public BigDecimal getRate() { return rate; }
    public void setRate(BigDecimal rate) { this.rate = rate; }
    public BigDecimal getChangePercent() { return changePercent; }
    public void setChangePercent(BigDecimal changePercent) { this.changePercent = changePercent; }
    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
}
