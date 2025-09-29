
package com.example.datagatherer.service;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RatePollingServiceTest {

    @Test
    void publishesOnFirstObservation() throws Exception {
        RateSource src = base -> Map.of("EUR", new BigDecimal("1.00"));
        AtomicInteger published = new AtomicInteger(0);
        RatePublisher pub = msg -> published.incrementAndGet();

        RatePollingService svc = new RatePollingService(src, pub);
        svc.poll(); // first publish
        assertEquals(1, published.get());
    }
}
