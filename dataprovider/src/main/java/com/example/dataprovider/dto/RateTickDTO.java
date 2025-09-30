package com.example.dataprovider.dto;

import java.math.BigDecimal;
import java.time.Instant;
public record RateTickDTO(String base, String quote, BigDecimal rate, Instant timestamp) {}

