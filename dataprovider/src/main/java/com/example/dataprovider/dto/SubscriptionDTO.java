package com.example.dataprovider.dto;

import java.math.BigDecimal;

public record SubscriptionDTO(
        Long id,
        String currency,
        BigDecimal thresholdPercent
) {}
