
package com.example.datagatherer.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.datagatherer.service.RateSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "app.provider.mock", havingValue = "false")
public class OpenExchangeRatesSource implements RateSource {

    private final RestTemplate rest = new RestTemplate();
    private final ObjectMapper mapper = new ObjectMapper();
    private final String appId;

    public OpenExchangeRatesSource() {
        this.appId = System.getenv().getOrDefault("OXR_APP_ID", "");
    }

    @Override
    public Map<String, BigDecimal> fetchLatest(String base) throws Exception {
        if (appId == null || appId.isEmpty()) {
            throw new IllegalStateException("OXR_APP_ID env var not set. Set it or use mock provider.");
        }
        String url = "https://openexchangerates.org/api/latest.json?app_id=" + appId + "&base=" + base;
        ResponseEntity<String> resp = rest.getForEntity(url, String.class);
        JsonNode root = mapper.readTree(resp.getBody());
        JsonNode rates = root.get("rates");
        Map<String, BigDecimal> out = new HashMap<>();
        Iterator<String> it = rates.fieldNames();
        while (it.hasNext()) {
            String k = it.next();
            out.put(k, rates.get(k).decimalValue());
        }
        return out;
    }
}
