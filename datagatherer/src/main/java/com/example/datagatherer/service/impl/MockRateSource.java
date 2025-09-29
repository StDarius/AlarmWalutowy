
package com.example.datagatherer.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.datagatherer.service.RateSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

@Service
@ConditionalOnProperty(name = "app.provider.mock", havingValue = "true", matchIfMissing = true)
public class MockRateSource implements RateSource {

    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public Map<String, BigDecimal> fetchLatest(String base) throws Exception {
        InputStream is = new ClassPathResource("sample-openexchangerates.json").getInputStream();
        JsonNode root = mapper.readTree(is);
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
