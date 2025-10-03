package com.example.datagatherer.service.impl;

import com.example.datagatherer.service.RateSource;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Profile("prod")
@Component
@RequiredArgsConstructor
public class OpenExchangeRatesSource implements RateSource {

    private static final int SCALE = 8;

    private final RestTemplate restTemplate;

    @Value("${app.external.base-url}")
    private String baseUrl;

    @Value("${app.external.oxr.appId}")
    private String appId;

    // e.g. ["EUR/PLN","USD/PLN","GBP/PLN"]
    @Value("#{'${app.pairs:EUR/PLN,USD/PLN,GBP/PLN}'.split(',')}")
    private List<String> pairs;

    @Override
    public Map<String, BigDecimal> fetchLatest(String desiredBase) {
        Set<String> needed = new HashSet<>();
        for (String p : pairs) {
            String[] split = p.trim().split("/");
            if (split.length == 2) {
                needed.add(split[0].trim().toUpperCase());
                needed.add(split[1].trim().toUpperCase());
            }
        }
        needed.add(desiredBase.toUpperCase());

        String symbols = needed.stream().sorted().collect(Collectors.joining(","));

        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/latest.json")
                .queryParam("app_id", appId)
                .queryParam("symbols", symbols)
                .build(true)
                .toUri();

        OxrLatestDto dto = restTemplate.getForObject(uri, OxrLatestDto.class);
        if (dto == null || dto.getRates() == null || dto.getRates().isEmpty()) {
            throw new IllegalStateException("OpenExchangeRates: empty response");
        }

        Map<String, BigDecimal> usdRates = dto.getRates();
        String base = desiredBase.toUpperCase();


        Map<String, BigDecimal> out = new HashMap<>();

        if ("USD".equals(base)) {
            for (String code : needed) {
                BigDecimal v = usdRates.get(code);
                if (v != null) out.put(code, v.setScale(SCALE, RoundingMode.HALF_UP));
            }
            out.put("USD", BigDecimal.ONE.setScale(SCALE, RoundingMode.HALF_UP));
            return out;
        }

        BigDecimal basePerUsd = usdRates.get(base);
        if (basePerUsd == null || basePerUsd.signum() == 0) {
            throw new IllegalStateException("OpenExchangeRates: base currency missing: " + base);
        }

        for (String code : needed) {
            if (code.equals(base)) {
                out.put(code, BigDecimal.ONE.setScale(SCALE, RoundingMode.HALF_UP));
                continue;
            }
            BigDecimal quotePerUsd = usdRates.get(code);
            if (quotePerUsd == null) continue;
            BigDecimal rate = quotePerUsd
                    .divide(basePerUsd, SCALE + 4, RoundingMode.HALF_UP)
                    .setScale(SCALE, RoundingMode.HALF_UP);
            out.put(code, rate);
        }

        return out;
    }

    @Data
    public static class OxrLatestDto {
        private long timestamp;
        private String base;
        private Map<String, BigDecimal> rates;
    }
}
