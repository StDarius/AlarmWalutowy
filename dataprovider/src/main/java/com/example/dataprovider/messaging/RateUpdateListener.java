package com.example.dataprovider.messaging;

import com.example.common.RateUpdateMessage;
import com.example.dataprovider.config.MessagingConfig;
import com.example.dataprovider.service.NotificationService;
import com.example.dataprovider.service.RateService;
import com.example.dataprovider.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class RateUpdateListener {

    private final SubscriptionService subscriptionService;
    private final RateService rateService;
    private final NotificationService notificationService;

    @RabbitListener(queues = MessagingConfig.QUEUE_NAME)
    public void onMessage(RateUpdateMessage msg) {
        // 1) Save tick in DB
        rateService.saveTick(msg.getBase(), msg.getQuote(), msg.getRate(), msg.getTimestamp());

        // 2) Check subscriptions
        var subs = subscriptionService.findByCurrency(msg.getQuote());
        for (var sub : subs) {
            double oldRate = rateService.findPreviousRate(msg.getBase(), msg.getQuote())
                    .map(BigDecimal::doubleValue)
                    .orElse(msg.getRate().doubleValue());

            double changePercent = Math.abs((msg.getRate().doubleValue() - oldRate) / oldRate) * 100.0;

            if (changePercent >= sub.getThresholdPercent().doubleValue()) {
                notificationService.sendThresholdExceeded(
                        sub.getUser(),
                        sub,
                        msg.getBase(),
                        msg.getQuote(),
                        oldRate,
                        msg.getRate().doubleValue(),
                        changePercent
                );
            }
        }
    }
}
