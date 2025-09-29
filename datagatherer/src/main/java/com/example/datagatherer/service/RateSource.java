
package com.example.datagatherer.service;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Abstraction over the real HTTP provider so dev profile can use mock data.
 */
public interface RateSource {
    /**
     * Returns a map of quote currency -> rate against base (e.g., base=USD, rates: EUR->0.93, PLN->4.13).
     */
    Map<String, BigDecimal> fetchLatest(String base) throws Exception;
}
