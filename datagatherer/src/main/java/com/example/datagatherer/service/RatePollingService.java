package com.example.datagatherer.service;

import com.example.common.RateUpdateMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
@RequiredArgsConstructor
public class RatePollingService {

    private final RateSource source;
    private final RatePublisher publisher;

    /** Last published rate per quote (so we can compute % change and throttle). */
    private final Map<String, BigDecimal> last = new ConcurrentHashMap<>();

    @Value("${app.base:USD}")
    private String base;

    /** Publish only when absolute change >= this percent. First observation always publishes. */
    @Value("${app.changeThresholdPercent:1.0}")
    private BigDecimal changeThresholdPercent;

    @Scheduled(
            fixedDelayString = "${app.poll.period-ms:60000}",
            initialDelayString = "${app.poll.initial-delay-ms:3000}"
    )
    public void poll() {
        long t0 = System.currentTimeMillis();
        int published = 0;

        try {
            Map<String, BigDecimal> rates = source.fetchLatest(base);

            for (Map.Entry<String, BigDecimal> e : rates.entrySet()) {
                String quote = e.getKey();
                BigDecimal current = e.getValue();

                BigDecimal previous = last.get(quote);
                BigDecimal changePct = BigDecimal.ZERO;
                boolean significant;

                if (previous != null && previous.signum() != 0) {
                    changePct = current.subtract(previous)
                            .divide(previous, 8, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .abs()
                            .setScale(4, RoundingMode.HALF_UP);
                    significant = (changePct.compareTo(changeThresholdPercent) >= 0);
                } else {
                    // First observation: publish so consumers have a baseline.
                    significant = true;
                }

                if (significant) {
                    RateUpdateMessage msg =
                            new RateUpdateMessage(base, quote, current, changePct, Instant.now());
                    publisher.publish(msg);
                    last.put(quote, current);
                    published++;
                    log.debug("Published {} -> {} rate={} Δ%={}", base, quote, current, changePct);
                }
            }

            log.info("Poll OK — published {} messages in {} ms",
                    published, System.currentTimeMillis() - t0);

        } catch (Exception ex) {
            // Catch EVERYTHING so the scheduled thread stays alive.
            log.error("Poll FAILED: {}", ex.toString(), ex);
        }
    }
}
