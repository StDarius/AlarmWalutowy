package com.example.dataprovider.dto;

import java.util.List;
public record RateHistoryDTO(String base, String quote, List<RateTickDTO> lastTicks) {}
