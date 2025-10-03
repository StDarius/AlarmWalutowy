// src/main/java/com/example/dataprovider/dev/DevEmitRateRequest.java
package com.example.dataprovider.dev;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record DevEmitRateRequest(
        @NotBlank String base,
        @NotBlank String quote,
        @NotNull @DecimalMin("0.0") BigDecimal rate
) {}
