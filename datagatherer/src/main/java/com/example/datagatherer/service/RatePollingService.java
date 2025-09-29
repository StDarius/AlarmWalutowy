
package com.example.datagatherer.service;

import com.example.common.RateUpdateMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Service
public class RatePollingService {
    private static final Logger log = LoggerFactory.getLogger(RatePollingService.class);

    private final RateSource source;
    private final RatePublisher publisher;

    private final Map<String, BigDecimal> last = new HashMap<>();

    @Value("${app.base:USD}")
    private String base;

    @Value("${app.changeThresholdPercent:1.0}")
    private BigDecimal changeThresholdPercent;

    public RatePollingService(RateSource source, RatePublisher publisher) {
        this.source = source;
        this.publisher = publisher;
    }

    @Scheduled(fixedRateString = "${app.pollFixedRateMs:3600000}", initialDelay = 2000)
    public void poll() {
        try {
            Map<String, BigDecimal> rates = source.fetchLatest(base);
            for (Map.Entry<String, BigDecimal> e : rates.entrySet()) {
                String quote = e.getKey();
                BigDecimal newRate = e.getValue();
                BigDecimal prev = last.get(quote);
                BigDecimal changePct = BigDecimal.ZERO;
                boolean significant = false;

                if (prev != null && prev.compareTo(BigDecimal.ZERO) != 0) {
                    changePct = newRate.subtract(prev)
                            .divide(prev, 6, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .abs();
                    significant = changePct.compareTo(changeThresholdPercent) >= 0;
                } else {
                    significant = true; // first observation
                }

                if (significant) {
                    RateUpdateMessage msg = new RateUpdateMessage(base, quote, newRate, changePct, Instant.now());
                    log.info("Publishing {}", msg);
                    publisher.publish(msg);
                    last.put(quote, newRate);
                }
            }
        } catch (Exception ex) {
            log.error("Polling failed: {}", ex.getMessage(), ex);
        }
    }
}
