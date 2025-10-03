package com.example.dataprovider.messaging;

import com.example.common.RateUpdateMessage;
import com.example.common.messaging.MQ;
import com.example.dataprovider.service.NotificationService;
import com.example.dataprovider.service.RateService;
import com.example.dataprovider.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Component
@RequiredArgsConstructor
public class RateUpdateListener {

    private static final Logger log = LoggerFactory.getLogger(RateUpdateListener.class);

    private final SubscriptionService subscriptionService;
    private final RateService rateService;
    private final NotificationService notificationService;

    @RabbitListener(queues = MQ.QUEUE)
    public void onMessage(RateUpdateMessage msg) {
        try {
            final String base  = msg.getBase().toUpperCase();
            final String quote = msg.getQuote().toUpperCase();
            final BigDecimal current = msg.getRate();


            final BigDecimal previous = rateService.findPreviousRate(base, quote).orElse(null);


            BigDecimal changePct = msg.getChangePercent();
            if (changePct == null && previous != null && previous.signum() != 0) {
                changePct = current.subtract(previous)
                        .divide(previous, 8, RoundingMode.HALF_UP)
                        .multiply(BigDecimal.valueOf(100));
            }


            final BigDecimal absChange = (changePct == null ? BigDecimal.ZERO : changePct.abs())
                    .setScale(4, RoundingMode.HALF_UP);

            // Persist tick with computed/received change%
            rateService.saveTick(base, quote, current, msg.getTimestamp(), absChange);

            // Notify subscribers whose threshold is met
            subscriptionService.findByCurrency(quote).forEach(sub -> {
                BigDecimal threshold = sub.getThresholdPercent();
                if (threshold == null) return;

                if (previous == null || absChange.compareTo(threshold) >= 0) {
                    notificationService.sendThresholdExceeded(
                            sub.getUser(),
                            sub,
                            base,
                            quote,
                            previous != null ? previous.doubleValue() : current.doubleValue(),
                            current.doubleValue(),
                            absChange.doubleValue()
                    );
                    log.info("Notified user={} subId={} {}->{} prev={} curr={} Δ%={}",
                            sub.getUser().getId(), sub.getId(),
                            base, quote, previous, current, absChange);
                }
            });

        } catch (Exception e) {
            log.error("Failed to process RateUpdateMessage: {}", e.getMessage(), e);
        }
    }
}
