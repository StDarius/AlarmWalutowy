package com.example.dataprovider.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record CreateSubscriptionDTO(
        @NotBlank String currency,
        @DecimalMin("0.0") BigDecimal thresholdPercent
) {}
