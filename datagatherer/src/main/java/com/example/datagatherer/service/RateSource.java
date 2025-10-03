
package com.example.datagatherer.service;

import java.math.BigDecimal;
import java.util.Map;


public interface RateSource {

    Map<String, BigDecimal> fetchLatest(String base) throws Exception;
}
